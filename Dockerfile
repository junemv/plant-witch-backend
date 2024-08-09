FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /plant-witch-backend
COPY . .
RUN mvn clean package -DskipTests

From openjdk:17-jdk-slim 
WORKDIR /plant-witch-backend
COPY --from=build /plant-witch-backend/target/plant-witch-backend-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]