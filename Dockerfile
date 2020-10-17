FROM adoptopenjdk/maven-openjdk11:latest AS builder

COPY ./app /app

WORKDIR /app

RUN mvn clean package

FROM jetty:9-jre11

COPY --from=builder /app/target/guacamole-easy-tunnel-1.0.0.war /var/lib/jetty/webapps/root.war

ENTRYPOINT [ "java", "-jar", "/usr/local/jetty/start.jar" ]