# SeismicMonitor

SeismicMonitor is a Spring Boot full-stack web application developed as a 24-hour programming exam project.

## Technologies

* Java 25
* Spring Boot 4
* Spring Security
* Spring Data JPA
* MySQL
* H2 Database
* HTML
* CSS
* JavaScript
* JUnit
* Mockito
* MockMvc
* Docker

## Features

* Receive sensor data through REST API
* Store sensors and sensor readings
* Create earthquake alerts when 'business rules'(forretningsregler) are fulfilled
* Reverse geocoding using OpenStreetMap 
* User reports for earthquake alerts
* Alert status management
* Role-based access control (USER / ADMIN)

## Running the application

Start MySQL:

```bash
docker compose up -d
```

Run the application:

```bash
./mvnw spring-boot:run
```

## Running tests

```bash
./mvnw test
```

## Demo users

USER

```text
Username: user
Password: user123
```

ADMIN

```text
Username: admin
Password: admin123
```

## Security

The application uses Spring Security with roles as access-control

* USER can view active alerts and create user reports.
* ADMIN can manage alerts, view sensor readings and access administrative functionality.

## Exam notes

The project was developed with focus on implementing the exam requirements end-to-end, including database layer, business logic, REST API, frontend integration and automated tests. As I was uncertain on wether or not I'd be able to finish all partassignments, I may have focused too much on finishing each part-assignment-flow from start to finish to make sure I'd have something to show. In retrospect, I believe a more structured plan for the whole project may have saved a lot of time overall.
