#!/bin/bash
./gradlew clean

./gradlew build -x test

scp build/libs/restful-eshop.war root@titsonfire.store:/opt/tomcat/webapps/core.war

