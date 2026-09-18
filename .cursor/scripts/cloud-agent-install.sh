#!/usr/bin/env bash
set -euo pipefail

cd /workspace
chmod +x gradlew
./gradlew build --no-daemon
