FROM maven:3.6.3-jdk-8 AS MAVEN_TOOL_CHAIN

ENV PROJECT_DIR_MAVEN=/tmp/library
WORKDIR $PROJECT_DIR_MAVEN

COPY ./pom.xml $PROJECT_DIR_MAVEN
RUN mvn dependency:resolve

COPY ./src/ $PROJECT_DIR_MAVEN/src
RUN mvn install -P docker

FROM openjdk:8-jdk

ENV PROJECT_DIR=/opt/library
ENV PROJECT_DIR_MAVEN=/tmp/library
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=MAVEN_TOOL_CHAIN $PROJECT_DIR_MAVEN/target/*.jar $PROJECT_DIR/library-web-classic.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "/opt/library/library-web-classic.jar"]