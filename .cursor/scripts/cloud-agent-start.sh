#!/usr/bin/env bash
set -euo pipefail

# H2 in-memory database requires no external service startup.
# Verify the application JAR was built during install.
if [[ ! -f build/libs/corporate_management-1.0.0.jar ]]; then
  echo "Application JAR not found. Run install first." >&2
  exit 1
fi

echo "CorporateTravel360 environment ready (H2 in-memory database)."
