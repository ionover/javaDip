FROM azul/zulu-openjdk-alpine:17-jre
LABEL MAINTAINER="ionov <>"

COPY ./target/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar

HEALTHCHECK --interval=30s --timeout=10s --retries=6 --start-period=1m \
    CMD curl -f http://localhost:8084/actuator/health || exit 1

ENTRYPOINT exec java -jar backend-0.0.1-SNAPSHOT.jar
