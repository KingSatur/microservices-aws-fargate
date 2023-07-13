FROM gradle:jdk11 as cache
RUN echo "Stage 1, generating build with cache"
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -i -x bootJar

FROM gradle:jdk11 as builder
RUN echo "Stage 2, generating bootJar"
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle bootJar

FROM openjdk:11-slim as build
RUN echo "Stage 3, defining entry image entry point"
USER root
WORKDIR /usr/src/java-app
COPY --from=builder /usr/src/java-code/build/libs/*.jar ./app.jar

ENV PORT 8002
EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "app.jar"]
