from amazoncorretto:11.0.11

from maven:3.6.3

copy . /app

run cd app && mvn clean package

expose 8081

entrypoint cd app && java -jar boss-transaction-service/target/boss-transaction-service-0.0.1-SNAPSHOT.jar