# docker-helloworld

Dynamic scaling of an app, based on the app health check status.

## Stack

TODO : docker / consul / ...

## Dummy app for testing

The dummy app exposes:
* APP_PORT (runs localy on 8080): applicative layer that can be load-balanced
* MANAGEMENT_PORT (runs localy on 8081): management port that give the health indicators for this specific instance of the app

A REST access has been exposed to fake the internal health status of the application.

### Build and start the app locally

```
$ mvn clean install
$ java -jar target/demo-${VERSION}.jar
```

### Build and start the app using the maven docker image

If you don't have maven installed on your system, use the offical maven docker image:

```
$ docker run -it --rm --name mvn-app \
    -v "$PWD":/usr/src/app \
    -w /usr/src/app maven:3.2-jdk-7 \
    mvn clean install
```

### Check the state of the application

```
$ http http://localhost:APP_PORT/state
```

You can also access the application state by using the monitoring port:

```
$ http http://localhost:MONITORING_PORT/state
```

Using the management port (under its endpoint `/health`), we can retrieve the "STATUS" element, and check it is "UP"

The shell command used to return a 0/1 status code based on the JSON returned object is:

```
http --body http://localhost:MONITORING_PORT/health \
    | jq --exit-code 'contains( { status: "UP" } )'
```

Shorten version:

```
http -b :MONITORING_PORT/health | jq -e 'contains({status:"UP"})'
```

### Simulate an application failure

Put the application state to DOWN:

```
$ http :APP_PORT/off
```

### Simulate an application respawn

Put the application state to UP:

```
$ http :APP_PORT/on
```

## Automation

Build and run the CRN stack (Consul-Registrator-Nginx), see: https://github.com/deviantony/docker-bg

### Build the docker image to host the application

```
$ docker build -t helloworld .
```

### Run the application

In order to simply run the application, use the following command:

```
$ docker run -d -P \
    -e "SERVICE_NAME=my_service" \
    -e "SERVICE_TAGS=my_tag" \
    -e "SERVICE_8081_IGNORE=1" \
    helloworld \
    java -jar /tmp/demo.jar
```

This will map the application port 8080 with a Consul service called my_service. 

The monitoring port of the application (8081) will not be mapped as a Consul service. To determine the monitoring port, use the `docker inspect` command on the container.

### Consul health check

To run the application and add a Consul health check, use the following command:

```
$ docker run -d -P \
    -e "SERVICE_NAME=my_service" \
    -e "SERVICE_TAGS=my_tag" \
    -e "SERVICE_8081_IGNORE=1" \
    -e "SERVICE_8080_CHECK_CMD=/tmp/health-check.sh" \
    -e "SERVICE_8080_CHECK_INTERVAL=15s" \
    helloworld \
    java -jar /tmp/demo.jar
```

