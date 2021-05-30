from amazoncorretto:11.0.11
copy ./boss-transaction-service/target/boss-transaction-service-0.0.1-SNAPSHOT.jar /app
entrypoint cd app && java -jar boss-transaction-service/target/boss-transaction-service-0.0.1-SNAPSHOT.jar