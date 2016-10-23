#!/usr/bin/env bash

jar="lexis-service.jar"

nohup java -jar $jar > application.log &
