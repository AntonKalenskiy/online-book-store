# Online-book-store

---
![markdown_logo](https://www.pngall.com/wp-content/uploads/2018/05/Books-PNG-Clipart.png)

### Project description:
This application facilitates user registration, authentication using JSON Web Tokens (JWT Tokens), 
shopping for books, and placing orders. It offers two levels of authorization: User and Admin. 
Users can buy books, sort them, search by criteria (e.g., author or title), and manage their shopping cart. 
Admins have additional privileges, allowing them to create, delete, or update books, categories, and order statuses. 
For more details, explore the application.


### Features:

---
- User registration and login
- CRUD operations for Books with search capabilities
- CRUD operations for Categories
- Shopping cart management (view, add books, update quantities, delete)
- Order creation, order history, order status updates
- Shipping details for orders
- Complete an order

### Project structure (3-layer architecture):

---
1. *DAO* - handle CRUD operations to database.
2. *Service* - there are all business logic.
3. *Controllers* - handle requests, call services and send responses.

### Used technologies and libraries:

---
- Java 17
- Git, GitHub
- Apache Maven, Tomcat
- MySQL, H2
- Safe delete strategy
- Hibernate, Java Persistence API (JPA)
- Spring Boot, Security, Web
- JWT tokens
- Checkstyle plugin
- Docker, Test containers
- Data Transfer Object (DTO) validation
- Liquibase
- Lombok
- Mapstruct
- Swagger
- Strategy Pattern (Design Pattern)
- Mockito, JUnit tests, Integrations tests;

### Instructions to run the project:

---
1. Ensure you have Docker installed on your machine. Download and install it from Docker's official website if needed. [Docker's official website](https://www.docker.com/get-started).
2. Download the repo: [https://github.com/AntonKalenskiy/online-book-store](https://github.com/AntonKalenskiy/online-book-store);
3. Open Command Prompt or Shell, change into the project's directory, start project using "docker-compose up" command.
4. Then follow link "http://localhost:6868/swagger-ui/index.html" and: 
  - if you want to try yourself as admin and create book or category, etc., use these: "login: batman@gothem.org, password: batman"; 
  - if you want to find books, add them to cart or make an order, use these: "login: ironman@newyork.org, password: ironman";)
5. Explore the available endpoints and enjoy the application.

