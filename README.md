# simple-submission-form

## Overview
Simple Spring Boot application that accepts submission feedbacks.

## Requirements
- Java 17\+ (or the Java version configured in the project)
- Maven 3.6\+
- Linux (development environment)

## Build
From the project root:
```bash
mvn clean package
```

## Run

From the project root:
```bash
mvn spring-boot:run
```

Or run the generated jar
```bash
java -jar target/simple-submission-form-0.0.1-SNAPSHOT.jar
```

## API Endpoints

- `localhost:8080`: Submit feedback.
- `localhost:8080/admin/feedback`: Retrieve all feedbacks.
