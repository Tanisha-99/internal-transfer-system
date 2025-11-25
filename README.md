# Internal Transfer System

A Spring Boot–based internal transfer service that supports creating accounts, querying balances, and performing atomic internal transfers between accounts.  
The system ensures strong consistency, uses pessimistic locking for balance updates, and persists data in PostgreSQL.

---

## Features

- Create accounts with an initial balance  
- Retrieve current account balance  
- Transfer funds between two accounts  
- All operations stored in PostgreSQL  
- Pessimistic locking to guarantee balance integrity  
- Clear error handling for invalid input, insufficient funds, and missing accounts  

---

## Tech Stack

- **Java 17+**  
- **Spring Boot**  
- **Spring Data JPA / Hibernate**  
- **PostgreSQL**  
- **Docker**  
- **Maven**  

---

## Future Improvements

- For transactions API we can use a message queue so that the service is decoupled and can also handle spikes. The idea is to integrate a message queue and when ever a transaction request come we can just push it in the queue and then process the requests. This will ensure that the requests are not lost even if the database or downstream services are down. Also it would be a great way to handle loads and spikes.

## Setup Instructions

1. Install Java (JDK 17)
2. After installation verify by running command - java --version
3. Install maven and add it in the environment variable.
4. Verify by command - mvn -v
5. Install docker
6. clone the repository
7. run the command docker compose up
8. once the container starts in docker we can start the application

