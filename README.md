# docker-helloworld

## Build the app using the maven docker image

```
$ docker run -it --rm --name mvn-app -v "$PWD":/usr/src/app -w /usr/src/app maven:3.2-jdk-7 mvn clean install
```

## Build the app docker image

```
$ docker build -t helloworld .
```

## Start the container

How to register a container into consul and skip a specific port (8081):

```
$ docker run -d -e "SERVICE_NAME=my_service" -e "SERVICE_TAGS=my_tag" -e "SERVICE_8081_IGNORE=1" -P helloworld
```

## With health-check

This is KO: 

```
$ docker run -d -e "SERVICE_NAME=my_service" -e "SERVICE_TAGS=my_tag" -e "SERVICE_8081_IGNORE=1" -e "SERVICE_8080_CHECK_CMD=/bin/true" -e "SERVICE_8080_CHECK_INTERVAL=15s" -P helloworld
```

This is the objective:

```
$ docker run -d -e "SERVICE_NAME=my_service" -e "SERVICE_TAGS=my_tag" -e "SERVICE_8081_IGNORE=1" -e "SERVICE_8080_CHECK_CMD=http http://localhost:8081/health -b | /usr/bin/jq '.status' | grep UP" -P helloworld
```
