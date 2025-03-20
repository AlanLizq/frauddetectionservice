FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/frauddetectservice*.jar app.jar

COPY src/main/resources/application.yml /app/config/application.yml
COPY src/main/resources/fraud-rules/rules-config.yml /app/config/fraud-rules/rules-config.yml
COPY src/main/resources/fraud-rules/suspicious-accounts.txt /app/config/fraud-rules/suspicious-accounts.txt

ENV SPRING_CONFIG_LOCATION=/app/config/

COPY ./omega-palace-454013-m1-6980f02eb9ce.json /app/credentials/service-account-key.json

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials/service-account-key.json

EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
