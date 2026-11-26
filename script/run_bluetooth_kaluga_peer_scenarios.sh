#!/bin/bash
# Runs the Bluetooth device scenarios of one module over the air between an Android device and this Mac, with kaluga
# on the other end playing the scenarios' peer in place of the bumble script:
#   client android: the client scenarios on Android, this Mac plays the test peripheral (KalugaTestPeripheral)
#   client macos:   the client scenarios on this Mac, Android plays the test peripheral
#   server android: the server scenarios on Android, this Mac plays the test central (KalugaTestCentral)
#   server macos:   the server scenarios on this Mac, Android plays the test central
#
# Needs the Android device (the one adb talks to) in radio range of this Mac with Bluetooth on, and Bluetooth permission
# for the app this runs in, which the macOS test binary inherits. The player starts first; the scenarios start once it
# logs that it plays. A player on this Mac is stopped when the scenarios are done, one on Android stops after a minute
# without requests. Logs go to OUT. Exits non-zero when the scenarios or the Android player failed.
#
# Usage: script/run_bluetooth_kaluga_peer_scenarios.sh client|server android|macos [scenario class]
# Environment: GRADLEW (./gradlew), ADB (adb), OUT (build/bluetooth-kaluga-peer)
set -uo pipefail

MODULE="${1:?client or server}"
SCENARIOS_ON="${2:?android or macos}"
CLASS="${3:-}"
cd "$(dirname "$0")/.."
GRADLEW="${GRADLEW:-./gradlew}"
ADB="${ADB:-adb}"
OUT="${OUT:-build/bluetooth-kaluga-peer}"
mkdir -p "$OUT"
LOG_PREFIX="$OUT/$MODULE-on-$SCENARIOS_ON"

if [ "$MODULE" = client ]; then
  PLAYER_MODULE=server; PLAYER_CLASS=com.splendo.kaluga.bluetooth.server.KalugaTestPeripheral
  PLAYER_ARGUMENT=playTestPeripheral; PLAYER_VARIABLE=KALUGA_BLUETOOTH_PLAY_TEST_PERIPHERAL; READY="playing the test peripheral"
  SCENARIO_ARGUMENT=netsimPeripheral; SCENARIO_VARIABLE=KALUGA_BLUETOOTH_TEST_PERIPHERAL; SCENARIO_FILTER="com.splendo.kaluga.bluetooth.Peripheral*"
  PLAYER_TAG=CentralTest
else
  PLAYER_MODULE=client; PLAYER_CLASS=com.splendo.kaluga.bluetooth.KalugaTestCentral
  PLAYER_ARGUMENT=playTestCentral; PLAYER_VARIABLE=KALUGA_BLUETOOTH_PLAY_TEST_CENTRAL; READY="playing the test central"
  SCENARIO_ARGUMENT=netsimCentral; SCENARIO_VARIABLE=KALUGA_BLUETOOTH_TEST_CENTRAL; SCENARIO_FILTER="com.splendo.kaluga.bluetooth.server.Central*"
  PLAYER_TAG=PeripheralTest
fi
kexe() { echo "bluetooth/$1/build/bin/macosArm64/debugTest/test.kexe"; }

PIDS=()
cleanup() { for pid in "${PIDS[@]+"${PIDS[@]}"}"; do kill "$pid" 2>/dev/null; done; }
trap cleanup EXIT

await_ready() {
  local log="$1" pid="$2"
  for _ in $(seq 1200); do
    grep -q "$READY" "$log" 2>/dev/null && return 0
    kill -0 "$pid" 2>/dev/null || { echo "the player ended before it played"; return 1; }
    sleep 0.5
  done
  echo "the player did not get ready"
  return 1
}

"$GRADLEW" ":bluetooth:$PLAYER_MODULE:linkDebugTestMacosArm64" ":bluetooth:$MODULE:linkDebugTestMacosArm64" || exit 1

"$ADB" logcat -c
"$ADB" logcat -v time > "$LOG_PREFIX-logcat.txt" 2>&1 &
PIDS+=("$!")

if [ "$SCENARIOS_ON" = android ]; then
  env "$PLAYER_VARIABLE=true" "$(kexe "$PLAYER_MODULE")" --ktest_filter="$PLAYER_CLASS.*" > "$LOG_PREFIX-player.log" 2>&1 &
  PLAYER=$!
  PIDS+=("$PLAYER")
  await_ready "$LOG_PREFIX-player.log" "$PLAYER" || exit 1
  FILTER=()
  [ -n "$CLASS" ] && FILTER=("-Pandroid.testInstrumentationRunnerArguments.class=$CLASS")
  "$GRADLEW" ":bluetooth:$MODULE:connectedAndroidDeviceTest" "-Pandroid.testInstrumentationRunnerArguments.$SCENARIO_ARGUMENT=kaluga" ${FILTER[@]+"${FILTER[@]}"}
  STATUS=$?
  kill "$PLAYER" 2>/dev/null
  PLAYER_STATUS=0
else
  "$GRADLEW" ":bluetooth:$PLAYER_MODULE:connectedAndroidDeviceTest" "-Pandroid.testInstrumentationRunnerArguments.class=$PLAYER_CLASS" \
    "-Pandroid.testInstrumentationRunnerArguments.$PLAYER_ARGUMENT=true" > "$LOG_PREFIX-player.log" 2>&1 &
  PLAYER=$!
  PIDS+=("$PLAYER")
  await_ready "$LOG_PREFIX-logcat.txt" "$PLAYER" || exit 1
  [ -n "$CLASS" ] && SCENARIO_FILTER="$CLASS.*"
  env "$SCENARIO_VARIABLE=kaluga" "$(kexe "$MODULE")" --ktest_filter="$SCENARIO_FILTER" 2>&1 | tee "$LOG_PREFIX-scenarios.log"
  STATUS=${PIPESTATUS[0]}
  wait "$PLAYER"
  PLAYER_STATUS=$?
  grep "$PLAYER_TAG" "$LOG_PREFIX-logcat.txt" > "$LOG_PREFIX-player-logcat.txt"
fi
echo "scenarios exit $STATUS, player exit $PLAYER_STATUS; logs $LOG_PREFIX-*"
[ "$STATUS" = 0 ] && [ "$PLAYER_STATUS" = 0 ]
