FROM openjdk:8-jre
VOLUME /tmp

RUN groupadd -g 999 appuser && \
    useradd -r -u 999 -g appuser appuser
USER appuser

ENV SPRING_PROFILE ut

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Xmx1024m", "-Duser.timezone=Asia/Tokyo", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]