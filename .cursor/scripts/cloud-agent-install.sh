#!/usr/bin/env bash
set -euo pipefail

JAVA17_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
if [[ ! -x "${JAVA17_HOME}/bin/java" ]]; then
  if command -v apt-get >/dev/null 2>&1; then
    sudo apt-get update -qq
    sudo DEBIAN_FRONTEND=noninteractive apt-get install -y -qq openjdk-17-jdk curl
  fi
fi

export JAVA_HOME="${JAVA17_HOME}"
export PATH="${JAVA_HOME}/bin:${PATH}"

cd /workspace
mkdir -p data
chmod +x gradlew
./gradlew build --no-daemon
