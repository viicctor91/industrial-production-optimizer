.PHONY: up test dev
up:
	 docker compose up -d
test:
	 docker compose up -d
	 cd backend && mvn test
	 cd frontend && npm install && npm test
dev:
	 docker compose up -d
	 @echo "Start frontend in another terminal: cd frontend && npm install && npm run dev"
	 cd backend && mvn quarkus:dev
