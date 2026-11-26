#!/bin/bash
# Starts the simulated devices of the Bluetooth device tests on the virtual radio of the running emulator: the
# peripheral the client tests connect to and the central that connects to the server tests. Returns once both run on
# the emulator's radio. Does nothing unless NETSIM is true.
set -euo pipefail

[ "${NETSIM:-false}" = "true" ] || exit 0

PYTHON="${NETSIM_PYTHON:-python3}"
PERIPHERAL_LOG="${NETSIM_LOG:-netsim_peripheral.log}"
CENTRAL_LOG="${NETSIM_CENTRAL_LOG:-netsim_central.log}"
ATTEMPTS=3

adb shell cmd bluetooth_manager enable || true
adb shell cmd bluetooth_manager wait-for-state:STATE_ON

# A script reaches netsim through the netsim.ini that bumble finds, which can name another netsimd than the emulator's,
# and then advertises or scans on a radio of its own. Each script therefore first asks the emulator's controller for
# its name over the radio, and exits if no answer comes. A restart reads the ini again.
address=$(adb shell settings get secure bluetooth_address | tr -d '\r')
case "$address" in
  ??:??:??:??:??:??) ;;
  *)
    echo "the emulator reports no Bluetooth address ($address)"
    exit 1
    ;;
esac

diagnose() {
  echo "netsim.ini files:"
  for dir in "${XDG_RUNTIME_DIR:-}" "${TMPDIR:-/tmp}" /tmp; do
    for ini in "${dir%/}"/netsim*.ini; do
      if [ -f "$ini" ]; then echo "$ini: $(tr '\n' ' ' < "$ini")"; fi
    done
  done
  echo "netsimd processes:"
  ps -eo pid,args | awk '/[n]etsimd/'
}

start() {
  local script="$1" log="$2" ready="$3" pid offset attempt
  : > "$log"
  for attempt in $(seq "$ATTEMPTS"); do
    offset=$(wc -l < "$log")
    echo "start_netsim_devices.sh: attempt $attempt" >> "$log"
    nohup "$PYTHON" "$script" --reach "$address" >> "$log" 2>&1 &
    pid=$!
    for _ in $(seq 60); do
      if tail -n "+$((offset + 1))" "$log" | grep -q "$ready"; then
        echo "$script is ready on the emulator's radio"
        return 0
      fi
      if ! kill -0 "$pid" 2>/dev/null; then
        break
      fi
      sleep 0.5
    done
    kill "$pid" 2>/dev/null || true
    echo "$script did not get ready (attempt $attempt of $ATTEMPTS)"
    sleep 5
  done
  cat "$log"
  diagnose
  exit 1
}

start bluetooth/client/src/deviceScenarios/python/peripheral.py "$PERIPHERAL_LOG" advertising
start bluetooth/server/src/deviceScenarios/python/central.py "$CENTRAL_LOG" scanning
