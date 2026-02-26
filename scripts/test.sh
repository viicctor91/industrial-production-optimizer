#!/usr/bin/env bash
set -e
docker compose up -d
( cd backend && mvn test )
( cd frontend && npm install && npm test )
