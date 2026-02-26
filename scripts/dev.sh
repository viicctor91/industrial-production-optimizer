#!/usr/bin/env bash
set -e
docker compose up -d
echo "Start backend in a terminal:"
echo "  cd backend && mvn quarkus:dev"
echo ""
echo "Start frontend in another terminal:"
echo "  cd frontend && npm install && npm run dev"
