# Todo Prototype

This repository contains a simple in-memory todo service implemented with Spring Boot and a small React UI served by Node.js. The REST API is defined using OpenAPI in `schemas/todo-api.yaml` and an interface is generated via the `openapi-generator-maven-plugin`.

## Running the backend

```bash
cd server
./run_tests.sh # executes `mvn test`
mvn spring-boot:run
```

## Running the frontend

```bash
cd client
npm install
npm start
```

The React app expects the backend to be running on `http://localhost:8080`.

## Continuous Integration

This repository uses a GitHub Actions workflow located in
`.github/workflows/ci.yml` that installs the required test dependencies and then
runs both the Java and Node.js test suites. The workflow executes for all
branches and pull requests. Configure branch protection rules to require the
`CI` workflow to pass before allowing merges.
