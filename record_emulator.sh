#!/bin/bash
set -x
echo "Record to $1"
adb shell "while true; do screenrecord --output-format=h264 -; done" | ffmpeg -i - $1
