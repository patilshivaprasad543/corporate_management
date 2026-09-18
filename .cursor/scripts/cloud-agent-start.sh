#!/usr/bin/env bash
set -euo pipefail

cd /workspace

export DB_URL='jdbc:h2:mem:corporate_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL'
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver
export SPRING_DATASOURCE_USERNAME=sa
export SPRING_DATASOURCE_PASSWORD=
export APP_JWT_SECRET='404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970'

PIDFILE=/tmp/corporate-app.pid
LOGFILE=/tmp/corporate-app.log

if [[ -f "${PIDFILE}" ]] && kill -0 "$(cat "${PIDFILE}")" 2>/dev/null; then
  if curl -sf http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "CorporateTravel360 already running on http://localhost:8080"
    exit 0
  fi
  kill "$(cat "${PIDFILE}")" 2>/dev/null || true
  rm -f "${PIDFILE}"
fi

nohup ./gradlew bootRun --no-daemon > "${LOGFILE}" 2>&1 &
echo $! > "${PIDFILE}"

for _ in $(seq 1 90); do
  if curl -sf http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "CorporateTravel360 ready on http://localhost:8080"
    exit 0
  fi
  sleep 2
done

echo "CorporateTravel360 failed to become ready; see ${LOGFILE}" >&2
exit 1
