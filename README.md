# FetchProject

## Users of this project must install the following: 
- JDK 11 (https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)
- Maven (https://mkyong.com/maven/how-to-install-maven-in-windows/)

## Run Project
 ```
mvn spring-boot:run
```

Example command line options for Transaction Controller Requests using curl
  
  ### Get all transactions
  ```
  curl -v http://localhost:8080/transactions
  ```
  ### Get payers' points
  ```
  curl -v http://localhost:8080/transactions/points
  ```
  ### Add transaction
  ```
  curl -d '{"payer":"will","points":1000}' -H 'Content-Type: application/json' http://localhost:8080/transactions
  ```
  ### Spend points
  ```
  curl -d '{"points":1000}' -H 'Content-Type: application/json' http://localhost:8080/transactions/spend
  ```
