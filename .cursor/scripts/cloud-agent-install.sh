#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/../.."

./gradlew compileJava test bootJar --no-daemon
