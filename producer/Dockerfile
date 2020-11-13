FROM openjdk:11
COPY target/*.jar /app/app.jar
ENTRYPOINT exec java -jar /app/app.jar ${message}