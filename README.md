# skynet-backend

Dynamic scaling of an app, based on the app health check status.

## Dummy app used to test the Skynet stack

The dummy app exposes:
* APP_PORT (runs localy on 8080): applicative layer that can be load-balanced
* MANAGEMENT_PORT (runs localy on 8081): management port that give the health indicators for this specific instance of the app

A REST access has been exposed to fake the internal health status of the application.

### Build and start the app locally

```
$ mvn clean install
$ java -jar target/demo-${VERSION}.jar
```

### Build the app using the maven docker image

If you don't have maven installed on your system, use the official maven docker image:

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


## Make the application un-responsive

To simulate slow queries, use the `/sleep` endpoint:


To get the help for this endpoint, perform:

```
$ http :APP_PORT/sleep
```

To make the request sleepy during a fix duration, perform


```
$ http :APP_PORT/sleep?duration=5
```

Default time unit is SECONDS.
To change the time unit, use


```
$ http :APP_PORT/sleep?duration=5&unit=MINUTES
```


To wait for a random amount of time, within an acceptable range, use


```
$ http :APP_PORT/sleep?max=50
```

This will wait for a random amount of seconds, between 1 and 50.

To change the lower bound, use:


```
$ http :APP_PORT/sleep?min=4&max=15
```

This will wait for a random amount of time, between 4 and 15 seconds.

To change the time unit, use


```
$ http :APP_PORT/sleep?min=4&max=15&unit=MINUTES
```

This will wait for a random amount of time, between 4 and 15 minutes.
