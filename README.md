# Cards Service

SpringBoot Based Cards API with MySQL integration

### Run Instructions

#### Build Application
`mvn clean install -DskipTests`

#### Docker steps
`docker-compose up`

#### Docker rebuild container
`docker-compose up --build`

#### Without Docker
`java -jar cards-0.0.1-SNAPSHOT.jar`

The service should be available on port 8080
Swagger url: 
`http://localhost:8080/swagger-ui/index.html`

Pre-created users with credentials for authentication
Authentication url:
`/api/v1/auth/authenticate`


**email: admin@example.com**, **password: admin123**


**email: user@example.com**, **password: user123**