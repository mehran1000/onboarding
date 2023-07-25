# Onboarding Application

The Onboarding Application provides essential services to enable customers to open their accounts remotely using any
electronic device.

## In this document

- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Run the application](#run-the-application)
    - [Run on local machine](#run-on-local-machine)
    - [Run using docker image](#run-using-docker-image)
- [Call the services](#call-the-services)
    - [Swagger UI](#swagger-ui)
    - [Postman collection](#postman-collection)
- [Implementation tips and assumptions](#implementation-tips-and-assumptions)

## Features

The most important features of this application are:

* **Account Creation:** Customers who are older than eighteen years can register themselves online without visiting a
  physical branch. Upon registration, a **current** account is created for the customer.
* **Secure Password Generation:** A secure password is automatically generated for the customer after a successful
  registration.
* **Token-Based Authentication:** Token service provides secure authentication for customers during logon and account
  access.
* **Supported Countries:** Customers from supported countries can register and create accounts. New countries can be
  added to the supported list easily.
* **Rate Limiting:** A rate limiter is implemented to ensure the API does not put excessive load on the legacy database.

## API Endpoints

The Onboarding Application exposes the following API endpoints:

* **POST /client-api/v1/register:** Allows customers to register and create account. Customers need to provide valid
  information, including name, address, date of birth (dob), ID document, and userName.
  including name, address, dob, ID document, and userName.
* **POST /client-api/v1/logon:** Enables customers to logon to their accounts using their credentials.
* **GET /client-api/v1/overview:** Provides a customer overview containing account information.

## Run the application

Before running the application pleases ensure Java 17 and Maven are installed on your machine.

To run this application, there are two options for running the project.

1. Run on local machine.
2. Run using docker image.

### Run on local machine

To run the Spring Boot application on your local machine, follow these steps:

* Navigate to the project directory:

```
cd [Path to the project]/onboarding
```

* Run the project:

```
mvn spring-boot:run
```

### Run using docker image

To run the Spring Boot application using a Docker image, follow these steps:

* Navigate to the project directory:

```
cd [Path to the project]/onboarding
```

* Build the Docker image:

```
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=onboarding:latest
```

* Verify the image creation:

```
docker images
```

* Run the application:

```
docker run -p 8080:8080 onboarding
```

## Call the services

There are two options for calling the services.

1. Swagger UI.
2. Postman collection

### Swagger UI

To use Swagger UI for interacting with the services, follow these steps:

* Ensure that the application is running.
* Open your web browser and go to [this link](http://localhost:8080/swagger-ui/index.html) to access Swagger UI.
* Use the interactive interface to explore and test the provided REST endpoints.

### Postman collection

To use the Postman collection for calling the services, follow these steps:

* Import the collection located in the **project directory/postman-collection** folder into Postman.
* In Postman, you can find and access the imported collection in the left sidebar under the "Collections" tab.

## Implementation tips and assumptions

Here are some implementation details and considerations for the project:

* The general flow:
    * The customer is registered.
    * In the response, the customer receives their secure password.
    * TThe customer logs in with the received username and password from the registration step and receives a JWT token.
      For the sake of simplicity, we only return the access_token in the response, and we do not support refresh tokens
      or other related data.
    * The customer uses the received JWT token as a Bearer token (in the Authorization header) to retrieve the account
      overview.
* I used [Passy](https://www.passay.org/) library for generating secure password.
* I used [iban4j](https://github.com/arturmkrtchyan/iban4j) library for generating a random IBAN, in real word, we
  should use some formula for generating the Account Number part.
* I used [bucket4j](https://github.com/MarcGiffing/bucket4j-spring-boot-starter) library for rate limitation. For
  simplicity,I defined only one bucket for all the requests sent to our endpoints.
* When a new customer is registered, we create a current account for them upon receiving the CustomerRegistrationEvent.
* To handle the mapping between DTOs (Data Transfer Objects) and model entities, I used MapStruct. This simplifies the
  data transformation process and keeps the code clean and maintainable.
* To ensure better maintainability and versioning, I incorporated versioning into the URIs of the services. This allows
  us to make changes to the API while ensuring backward compatibility with previous versions.
* Depending on the country, we can apply country-specific validations, such as different postal code formats. However,
  for simplicity, I have not implemented these validations in the current version. If you require country-specific
  validations, please let me know, and I can add them accordingly.
* I assumed that the different types of accounts are fixed, so I used an enum to represent them. However, if the set of
  account types is expected to grow or change frequently, using an enum might not be the best option. In such cases,
  using a database-driven approach (e.g., with a lookup table) could offer more flexibility.
* For the sake of simplicity and easy setup, I used an embedded H2 database. In a production environment, we would
  typically use a more robust database like MySQL or PostgreSQL.
* For managing database changes and versioning, we can use Flyway or Liquibase.



