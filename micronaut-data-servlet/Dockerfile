FROM eclipse-temurin:21-jre
WORKDIR /home/app
COPY build/libs/micronaut-data-servlet-0.1-all-optimized.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
