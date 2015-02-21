FROM dockerfile/java:oracle-java7

ADD lib/demo-0.0.1-SNAPSHOT.jar /tmp/demo.jar

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "/tmp/demo.jar"]

