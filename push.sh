#!/bin/sh

./mvnw clean package
docker builder build --platform linux/arm/v7 -t osvasldas97/innovation:1.0-SNAPSHOT .
docker push osvasldas97/innovation:1.0-SNAPSHOT
