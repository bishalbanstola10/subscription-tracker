# Subscription Tracker

A microservices-based application to manage and track user subscriptions, featuring secure authentication, Kafka-driven event processing, and automated reminders.

---
## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running Locally](#running-locally)
- [Deployment](#deployment)
  - [Docker Compose](#docker-compose)
  - [AWS EC2 & ALB](#aws-ec2--alb)
  - [CI/CD Pipeline](#ci-cd-pipeline)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)
- [License](#license)

---
## Features

- **User Authentication**: Register and login with JWT-based security.
- **Subscription Management**: Create, read, update, and delete subscription records.
- **Event Streaming**: Kafka-backed event processing for decoupled microservices.
- **Automated Reminders**: Sends reminders via a dedicated microservice.
- **API Gateway**: Central routing, CORS, and global filters using Spring Cloud Gateway.
- **Robust CI/CD**: GitHub Actions for build, test, container publishing, and infrastructure deployment.

---
## Architecture

```
[React SPA] --HTTPS--> [CloudFront] --HTTP--> [ALB] --> [API Gateway (Spring Cloud)]
                                                        |
                                                        --> [Auth Service]
                                                        --> [Subscription Service]
                                                        --> [Reminder Service] --> [Kafka/Event Stream]

      [RDS (Postgres)] <-------------------------------------/
```

- **Frontend**: React & Vite, hosted on S3 + CloudFront
- **Gateway & Services**: Spring Boot microservices running in Docker on EC2 behind an ALB
- **Messaging**: Apache Kafka & Schema Registry via Docker Compose (or Confluent Cloud)
- **Database**: Amazon RDS for Postgres

---
## Technologies

| Layer               | Technology                                           |
|--------------------:|:-----------------------------------------------------|
| Frontend            | React, Vite, Axios                                   |
| API Gateway         | Spring Cloud Gateway, JWT                            |
| Services            | Spring Boot, Spring Data JPA                         |
| Messaging           | Apache Kafka, Confluent Schema Registry              |
| Database            | PostgreSQL (Amazon RDS)                              |
| Containerization    | Docker, Docker Compose                               |
| Cloud Infrastructure| AWS EC2, Application Load Balancer, S3, CloudFront, IAM, ACM |
| CI/CD               | GitHub Actions                                       |

---
## Getting Started

### Prerequisites

- **Node.js** (v18+)
- **Java JDK** (v22+)
- **Docker** & **Docker Compose**
- **AWS CLI** configured with appropriate credentials

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/<USERNAME>/subscription-tracker.git
   cd subscription-tracker
   ```

2. **Install frontend dependencies**
   ```bash
   cd frontend
   npm install
   ```

3. **Build backend services**
   ```bash
   cd ..
   mvn clean package -DskipTests
   ```

### Configuration

Create a `.env` file at the project root with values for:

```dotenv
# Docker Hub
DOCKERHUB_USERNAME=your-dockerhub-username

# RDS
AUTH_DB_URL=jdbc:postgresql://<auth-db-endpoint>:5432/auth_db
AUTH_DB_USER=<username>
AUTH_DB_PASS=<password>

SUB_DB_URL=jdbc:postgresql://<subscription-db-endpoint>:5432/subscription_db
SUB_DB_USER=<username>
SUB_DB_PASS=<password>

# Kafka & Schema Registry
KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SCHEMA_REGISTRY_URL=http://schema-registry:8081

# Ports
SUB_SERVICE_PORT=4000
AUTH_SERVICE_PORT=4005
API_GATEWAY_PORT=4004
REMINDER_SERVICE_PORT=4002
```

### Running Locally

```bash
docker compose -f docker-compose.yml up -d
```

Visit `http://localhost:5173` for the frontend and interact with the API at `http://localhost:4004`.

---
## Deployment

### Docker Compose

Use the production compose file:

```bash
docker compose -f docker-compose.prod.yml up -d
```

### AWS EC2 & ALB

1. Launch an Amazon Linux 2 EC2 instance with Docker & Docker Compose.
2. Attach an IAM role with SSM permissions.
3. Configure an ALB forwarding port 80 to 4004.
4. Set up CloudFront behaviors for static assets (S3) and API routes (ALB).

### CI/CD Pipeline

- **Build**: Frontend (`npm run build`) and backend (`mvn package -DskipTests`)
- **Publish**: Docker images to Docker Hub
- **Deploy**: SSH into EC2 via GitHub Actions and run `docker compose`
- **Invalidate**: CloudFront cache for new frontend releases

---
## Environment Variables

Refer to `.env.example` for all required variables.

---
## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

---
## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
