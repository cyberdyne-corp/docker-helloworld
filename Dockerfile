FROM dockerfile/java:oracle-java7

RUN echo "deb http://archive.ubuntu.com/ubuntu trusty-backports main restricted universe multiverse" >> /etc/apt/sources.list

RUN apt-get update && apt-get install -y httpie jq/trusty-backports

COPY target/demo-0.0.1-SNAPSHOT.jar /tmp/demo.jar
COPY scripts/health-check.sh /tmp/health-check.sh

EXPOSE 8080 8081
