# docker-helloworld

## Requirements

Build and run the CRN stack (Consul-Registrator-Nginx), see: https://github.com/deviantony/docker-bg

## Application

### Build the application artifact

```
$ mvn clean install
```

If you don't have maven installed on your system, use the offical maven docker image:

```
$ docker run -it --rm --name mvn-app -v "$PWD":/usr/src/app -w /usr/src/app maven:3.2-jdk-7 mvn clean install
```

### Build the docker image to host the application

```
$ docker build -t helloworld .
```

### Run the application

In order to simply run the application, use the following command:

```
$ docker run \
--rm \
-d \
-P \
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
$ docker run \
--rm \
-d \
-P \
-e "SERVICE_NAME=my_service" \
-e "SERVICE_TAGS=my_tag" \
-e "SERVICE_8081_IGNORE=1" \
-e "SERVICE_8080_CHECK_CMD=/tmp/health-check.sh" \
-e "SERVICE_8080_CHECK_INTERVAL=5s" \
helloworld \
java -jar /tmp/demo.jar
```

### Application state
A REST access has been exposed to manage the state of the application, these examples use httpie (https://github.com/jakubroztocil/httpie) to send HTTP requests from the command line.

You'll need to determine the application port mapped by docker first (use the Consul UI).

Check the state of the application:

```
$ http http://localhost:APP_PORT/state
```

You can also access the application state by using the monitoring port:

```
$ http http://localhost:MONITORING_PORT/state
```

Put the application state to DOWN:

```
$ http http://localhost:APP_PORT/off
```

Put the application state to UP:

```
$ http http://localhost:APP_PORT/on
```
