#!/bin/bash
# Starts the simulated devices of the Bluetooth device tests on the virtual radio of the running emulator: the
# peripheral the client tests connect to and the central that connects to the server tests. Returns once the
# peripheral advertises and the central scans. Does nothing unless NETSIM is true.
set -euo pipefail

[ "${NETSIM:-false}" = "true" ] || exit 0

PYTHON="${NETSIM_PYTHON:-python3}"
PERIPHERAL_LOG="${NETSIM_LOG:-netsim_peripheral.log}"
CENTRAL_LOG="${NETSIM_CENTRAL_LOG:-netsim_central.log}"

adb shell cmd bluetooth_manager enable || true
adb shell cmd bluetooth_manager wait-for-state:STATE_ON

start() {
  local script="$1" log="$2" ready="$3" pid
  nohup "$PYTHON" "$script" > "$log" 2>&1 &
  pid=$!
  for _ in $(seq 60); do
    if grep -q "$ready" "$log"; then
      echo "$script is ready"
      return 0
    fi
    if ! kill -0 "$pid" 2>/dev/null; then
      break
    fi
    sleep 0.5
  done
  echo "$script did not get ready"
  cat "$log"
  exit 1
}

start bluetooth/client/src/androidDeviceTest/python/netsim_peripheral.py "$PERIPHERAL_LOG" advertising
start bluetooth/server/src/androidDeviceTest/python/netsim_central.py "$CENTRAL_LOG" scanning
