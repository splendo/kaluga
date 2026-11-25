#!/bin/bash
# Starts the simulated peripheral of the Bluetooth client device tests on the virtual radio of the
# running emulator, and returns once it advertises. Does nothing unless NETSIM is true.
set -euo pipefail

[ "${NETSIM:-false}" = "true" ] || exit 0

PYTHON="${NETSIM_PYTHON:-python3}"
LOG="${NETSIM_LOG:-netsim_peripheral.log}"

adb shell cmd bluetooth_manager enable || true
adb shell cmd bluetooth_manager wait-for-state:STATE_ON

nohup "$PYTHON" bluetooth/client/src/androidDeviceTest/python/netsim_peripheral.py > "$LOG" 2>&1 &
PERIPHERAL=$!

for _ in $(seq 60); do
  if grep -q advertising "$LOG"; then
    echo "Simulated peripheral is advertising"
    exit 0
  fi
  if ! kill -0 "$PERIPHERAL" 2>/dev/null; then
    break
  fi
  sleep 0.5
done

echo "Simulated peripheral did not start advertising"
cat "$LOG"
exit 1
