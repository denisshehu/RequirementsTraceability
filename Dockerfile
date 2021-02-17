FROM openjdk:15
COPY ./target/classes/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","RequirementsTraceability"]