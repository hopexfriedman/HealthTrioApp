FROM openjdk:19
COPY ./target/HealthTrioApp-0.0.1-SNAPSHOT.jar HealthTrioApp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/HealthTrioApp-0.0.1-SNAPSHOT.jar"]