# docker-helloworld

## Objectives:

Dynamic scaling of an app, based on the app health check status.

The app exposes:
* port 8080: applicative layer that can be load-balanced
* port 8081: management port that give the health indicators for this specific instance of the app

To fake a failure on this instance, call `GET /off` on the 8080 port ; this will change the health indicator exposed on the 8081 port.
To restore the instance, call `GET /on` on the 8080 port

## Build the app using the maven docker image

```
$ docker run -it --rm --name mvn-app \
    -v "$PWD":/usr/src/app \
    -w /usr/src/app maven:3.2-jdk-7 \
    mvn clean install
```

## Build the app docker image

```
$ docker build -t helloworld .
```

## Start the container

How to register a container into consul and skip a specific port (8081):

```
$ docker run -d \
    -e "SERVICE_NAME=my_service" \
    -e "SERVICE_TAGS=my_tag" \
    -e "SERVICE_8081_IGNORE=1" \
    -P helloworld
```

## With health-check

Using the management port (8081), we can retrieve the "STATUS" element, and check it is "UP"

```
$ docker run -d \
    -e "SERVICE_NAME=my_service" \
    -e "SERVICE_TAGS=my_tag" \
    -e "SERVICE_8081_IGNORE=1" \
    -e "SERVICE_8080_CHECK_CMD=http http://localhost:8081/health -b | /usr/bin/jq '.status' | grep UP" \
    -P helloworld
```

But ... it currently fails.

The Consul UI returns the following error:

```
Get http:///var/run/docker.sock/v1.17/images/6ae78eef3208/json: dial unix /var/run/docker.sock: no such file or directory. Are you trying to connect to a TLS-enabled daemon without TLS?Get http:///var/run/docker.sock/v1.17/images/6ae78eef3208/json: dial unix /var/run/docker.sock: no such file or directory. Are you trying to connect to a TLS-enabled daemon without TLS?time="2015-02-22T09:58:39Z" level="fatal" msg="Post http:///var/run/docker.sock/v1.17/containers/create: dial unix /var/run/docker.sock: no such file or directory. Are you trying to connect to a TLS-enabled daemon without TLS?" 
```

### Tests to make the health check working

This is KO too:

```
$ docker run -d \
    -e "SERVICE_NAME=my_service" \
    -e "SERVICE_TAGS=my_tag" \
    -e "SERVICE_8081_IGNORE=1" \
    -e "SERVICE_8080_CHECK_CMD=/bin/true" \
    -e "SERVICE_8080_CHECK_INTERVAL=15s" \
    -P helloworld
```

### Leads

http://sheerun.net/2014/05/17/remote-access-to-docker-with-tls/

