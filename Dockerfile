# Container no. 0
FROM maven
COPY . /tmp
WORKDIR /tmp
RUN mvn install
CMD mvn package

# Container no. 1
FROM openjdk:15-slim
COPY --from=0 /tmp/target/RequirementsTraceability-1.0-SNAPSHOT-jar-with-dependencies.jar run.jar
CMD java -jar run.jar