from amazoncorretto:11.0.11
copy ./boss-transaction-service/target/boss-transaction-service-0.0.1-SNAPSHOT.jar /app
entrypoint java -jar app/boss-transaction-service-0.0.1-SNAPSHOT.jar