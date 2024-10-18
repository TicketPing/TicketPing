FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 10024
EXPOSE 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"]