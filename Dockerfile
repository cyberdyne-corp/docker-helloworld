FROM dockerfile/java:oracle-java7

RUN apt-get update && apt-get install -y httpie wget

RUN wget http://stedolan.github.io/jq/download/linux64/jq -P /usr/bin/

RUN chmod +x /usr/bin/jq

COPY target/demo-0.0.1-SNAPSHOT.jar /tmp/demo.jar
COPY scripts/health-check.sh /tmp/health-check.sh

EXPOSE 8080 8081
