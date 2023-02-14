# Easy Bus Tickets

## Microservice API for buying bus tickets

### Description

This project is a microservice API for purchasing bus tickets. It consists of several microservices, including:

- Discovery Server
- Api Gateway
- Routes Service
- Ticket Buying Service
- Ticket Info Service
- Payment Service
- Payment Status Service
- Ticket Status Checker

### Key Endpoints

#### GET:/api/routes

Used to select a bus route.

#### POST:/api/tickets

Used to purchase a ticket on a route. The request body should include the following information:

    {
        "personName":"name",
        "routeId":"id"
    }

#### GET:/api/ticket-info/{ticketId}

Used to get information about a ticket and its payment. It accepts a ticket identifier as a path variable and returns information about the ticket and payment status.

### Includes:

- Discovery Server: responsible for managing the service registry and facilitating 
communication between microservices.<br/>


- Api Gateway: acts as a reverse proxy, routing requests to the appropriate microservice.<br/>


- Routes Service: displays bus routes, 
also reduces the number of available tickets on the bus route upon successful ticket payment.<br/>


- Ticket Buying Service: accepts from the customer name and bus route identifier. 
Saves the ticket in the database, calls Payment Service to create a payment 
and calls Routes Service to change the number of tickets upon successful payment.<br/>


- Ticket Info Service: accepts from the customer the ticket identifier. 
Displays information about the ticket and payment status. Upon successful payment, 
displays route details and payment information. If the ticket is not paid, 
it displays the number of available tickets on the bus route and payment status.<br/>


- Payment Service: assigns a payment identifier when a ticket is purchased and passes the identifier 
to Payment Status Service.<br/>


- Payment Status Service: assigns one of three statuses to a payment: NEW, DONE, FAILED. 
Saves the payment in the database.<br/>


- Ticket Status Checker: checks the status of all tickets with payment status NEW. 
Changes the status again to DONE or FAILED. Works on schedule.<br/>

### Running the Project

To run the project, start the services in the following order:

1. Discovery Server
2. Api Gateway
3. Other services

### Technologies

This project uses the following technologies:
- Spring Boot
- Spring Cloud
- Spring Data
- Webflux
- H2
- Flyway
- Lombok
- JUnit