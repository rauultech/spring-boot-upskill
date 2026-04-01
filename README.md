# Spring Boot Demo App (v1.0)

A production-ready Spring Boot API featuring JWT security, PostgreSQL persistence, and a fully automated CI/CD pipeline.

## Live Demo
- **API Base URL:** `https://demo-springboot-app-latest-nok4.onrender.com/`
- **Health Check:** `https://your-app-name.onrender.com/actuator/health`

## Tech Stack
- **Backend:** Java 17, Spring Boot 3, Spring Security, Hibernate
- **Database:** PostgreSQL (Managed via Render)
- **Migrations:** Flyway
- **DevOps:** Docker, GitHub Actions (CI/CD), Docker Hub

## System Architecture
1. **Code Push:** Developer pushes to `main`.
2. **CI/CD:** GitHub Actions builds the JAR, runs tests, and creates a Docker Image.
3. **Registry:** Image is pushed to Docker Hub.
4. **Deploy:** Render pulls the latest image and restarts the web service.

## Key Features
- **Stateless Auth:** JWT-based authentication with auto-injected secrets.
- **Automated Schema:** Flyway handles database versioning on startup.
- **Dockerized:** Entire environment is containerized for "run anywhere" capability.

## How to Run Locally
1. Clone the repo.
2. Run `docker-compose up`.
3. Access at `localhost:8080`.