# Crud app in java

- On start page you can log in or create user;
- Based on your role:
    - As a customer - you will see your own orders, you can create new order adding products, download a report of yours
      orders.
    - As a manager - you will see all customers and their orders, you can add and change products, sort orders by
      product,
      changing order statuses or close orders, delete orders before chosen date, download a report of customers and
      their orders.
    - As an admin - you will see all users(including managers and admins), search for user by id and edit or delete
      users, download a report of users.
- You can switch a language on each page and log out

## You can start this app two ways:

### First:

- command: mvn clean install
- docker-compose up --build

### Or you can follow this instruction:

- docker run --name mysql-shop -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=shop -p 3306:3306 -d mysql:latest
- apply V1__init.sql
- to start from idea: Run-> Edit configuration -> TomCat 10,
  Deployment <span style="color:yellow; font-size:15px;">/</span>
