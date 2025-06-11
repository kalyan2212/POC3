# EdgeConvert Microservices

A cloud-native microservices architecture for converting Entity-Relationship Diagrams to Database Definition Language (DDL) statements.

## Architecture Overview

This project refactors the monolithic EdgeConvert application into a domain-driven microservices architecture using AWS cloud-native services.

### Services

1. **API Gateway** (Port 8080)
   - Central entry point for all API requests
   - Routes requests to appropriate microservices
   - Handles CORS and basic authentication

2. **Diagram Parser Service** (Port 8081)
   - Parses Edge Diagrammer (.edg) files
   - Extracts entities, attributes, and relationships
   - Stores parsed diagrams in DynamoDB

3. **Table Management Service** (Port 8082)
   - Manages database table definitions
   - Handles field specifications and constraints
   - CRUD operations for table metadata

4. **Relationship Management Service** (Port 8083)
   - Manages relationships between tables
   - Handles foreign key constraints
   - Validates relationship integrity

5. **DDL Generation Service** (Port 8084)
   - Generates database-specific DDL statements
   - Supports multiple database targets (MySQL, PostgreSQL, etc.)
   - Produces optimized schema scripts

6. **Frontend Service**
   - React.js web application
   - File upload and diagram management UI
   - Real-time parsing and DDL generation

### Technology Stack

- **Framework**: Spring Boot 3.2.0 with Java 17
- **Cloud Provider**: AWS (us-east-1 region)
- **Containers**: Docker with ECS Fargate
- **Database**: DynamoDB (database per service pattern)
- **Load Balancer**: Application Load Balancer
- **IAM Role**: `arn:aws:iam::183295419018:role/GitHubActionsRole`
- **Frontend**: React.js 18
- **CI/CD**: GitHub Actions

### AWS Infrastructure

- **VPC**: Private networking with public/private subnets
- **ECS Fargate**: Serverless container hosting
- **DynamoDB**: NoSQL databases for each service
- **CloudWatch**: Monitoring and logging
- **NAT Gateway**: Outbound internet access for private subnets

## Getting Started

### Prerequisites

- Java 17
- Maven 3.6+
- Docker
- Node.js 18+ (for frontend)
- AWS CLI configured

### Local Development

1. **Build all services**:
   ```bash
   # API Gateway
   cd microservices/api-gateway
   mvn clean package
   
   # Diagram Parser Service
   cd ../diagram-parser-service
   mvn clean package
   
   # Table Management Service
   cd ../table-management-service
   mvn clean package
   ```

2. **Run services locally**:
   ```bash
   # Start API Gateway
   java -jar microservices/api-gateway/target/api-gateway-1.0.0.jar
   
   # Start Diagram Parser Service
   java -jar microservices/diagram-parser-service/target/diagram-parser-service-1.0.0.jar
   
   # Start Table Management Service
   java -jar microservices/table-management-service/target/table-management-service-1.0.0.jar
   ```

3. **Run frontend**:
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Deployment

The application uses GitHub Actions for automated CI/CD:

1. **Infrastructure Deployment**:
   ```bash
   aws cloudformation deploy \
     --template-file infrastructure/cloudformation/infrastructure.yml \
     --stack-name edgeconvert-infrastructure \
     --parameter-overrides Environment=dev \
     --capabilities CAPABILITY_NAMED_IAM \
     --region us-east-1
   ```

2. **Service Deployment**:
   - Push to main branch triggers automated deployment
   - Services are built, containerized, and deployed to ECS Fargate
   - Load balancer routes traffic to healthy service instances

## API Documentation

### Diagram Parser Service

- `POST /api/diagrams/upload` - Upload .edg file
- `POST /api/diagrams/{id}/parse` - Parse uploaded diagram
- `GET /api/diagrams/{id}` - Get diagram details
- `GET /api/diagrams/user/{userId}` - Get user's diagrams
- `DELETE /api/diagrams/{id}` - Delete diagram

### Table Management Service

- `POST /api/tables` - Create table definition
- `GET /api/tables/{diagramId}` - Get tables for diagram
- `PUT /api/tables/{id}` - Update table definition
- `DELETE /api/tables/{id}` - Delete table

### DDL Generation Service

- `POST /api/ddl/generate` - Generate DDL for diagram
- `GET /api/ddl/databases` - List supported databases
- `POST /api/ddl/validate` - Validate schema

## Database Schema

### DynamoDB Tables

1. **diagrams**
   - Partition Key: `id` (String)
   - Attributes: name, content, status, userId, createdAt, updatedAt

2. **tables**
   - Partition Key: `diagramId` (String)
   - Sort Key: `tableId` (String)
   - Attributes: name, fields, userId, createdAt, updatedAt

3. **relationships**
   - Partition Key: `diagramId` (String)
   - Sort Key: `relationshipId` (String)
   - Attributes: sourceTable, targetTable, type, fields

## Monitoring and Logging

- **CloudWatch Metrics**: Custom metrics for each service
- **CloudWatch Logs**: Centralized logging with structured format
- **Health Checks**: Spring Boot Actuator endpoints
- **Distributed Tracing**: Service-to-service request tracking

## Security

- **IAM Roles**: Least privilege access for ECS tasks
- **VPC**: Private networking with controlled internet access
- **Security Groups**: Restricted port access between services
- **HTTPS**: TLS termination at load balancer

## Addressing POC2 Deployment Issues

This architecture addresses common deployment issues from POC2:

1. **Container Resource Limits**: Proper memory/CPU allocation for ECS tasks
2. **Health Check Configuration**: Robust health check endpoints
3. **Service Discovery**: Load balancer handles service routing
4. **Database Connectivity**: VPC configuration for secure DB access
5. **Logging Configuration**: Structured logging to CloudWatch
6. **Environment Variables**: Proper configuration management
7. **Rolling Deployments**: Zero-downtime deployment strategy

## Contributing

1. Create feature branch from main
2. Implement changes with tests
3. Submit pull request
4. Automated CI/CD will deploy after merge

## License

This project is licensed under the MIT License.