FROM openjdk:21
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} Proyecto-Tingeso.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./Proyecto-Tingeso.jar"]