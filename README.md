# Testing Spring Boot API

This project includes Unit tests and Integration tests of a sample Employee Management System API.

## What I learned while doing this project:

In this project, I learnt how to write Unit tests and Integration tests in BDD style for Spring Boot applications using JUnit 5, Mockito, AssertJ, Hamcrest, JsonPath, and Testcontainers frameworks.

- Writing industry-standard Unit and Integration tests in BDD (Behaviour Driven Development) style using Spring Boot Starter Test dependency from scratch
- How to use BDD (Behaviour Driven Development) format that is given/when/then to write Unit tests.
- Writing Unit test the Spring boot application Repository layer, Service layer and Controller layer
- How to do Integration testing for the Spring boot application.
- How to do Integration testing using Testcontainers 
- Using the most important Unit Testing ANNOTATIONS - @SpringBootTest, @WebMvcTest, @DataJpaTest, and @MockBean
- Using Mockito annotations to create mock objects.
- Writing Integration Tests using a MySQL database.
- Writing Independent Integration tests for RESTFUL web services by talking with multiple layers - controller, service, and repository layers.

## Tools and Technologies used in this course:

### Technologies and Libraries:

- Java 11+
- Spring Boot 3+
- Spring Data JPA ( Hibernate)
- JUnit 5 Framework
- Mockito 4 (Latest)
- Testcontainers
- Hamcrest framework
- AssertJ Library
- JsonPath Library
- Docker (for using testcontainers)

### Database:

- H2 In-memory database (for repository layer testing)
- MySQL database (for Integration testing)
