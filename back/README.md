# Yoga App !

## 1. Installation Guide

Ensure that you have :

- Java 8 (check it with java -version)
- Apache Maven 3.x.x (check it with mvn -v)
- MySQL

After clone the rpository, you can follow the steps to install and run the application:

1. Install Dependencies with Maven

> mvn clean install

2. Configure application.properties

Locate the `env_template` file. Make a copy in the same folder and rename it .env. Add your MySQL root's username and password.

3. Initialize the Database Schema With MySQL installed and running, load the initial database schema:

> mysql -u root -p < ./src/main/resources/schema.sql

You will be prompted for the MySQL root password.

## 2. Test running
Launching test:

> mvn clean verify

Report for **all tests** coverage here:

> back/target/jacoco-merged-test-coverage-report/index.html

Report for **integration tests** coverage here:

> /back/target/jacoco-integration-test-coverage-report/index.html
