#!/bin/bash
#
# Copyright 2015 SmartBear Software
#

# find jar to run
JAR=$(find -name "*testserver-cucumber*.jar" -type f)

# extract additional JVM options from ENV vars
OPTS=()
while read -r ITEM; do
    OPTS+=("${ITEM}")
done < <(env | grep -E "^JVM_OPT_[^=]+=" | sed -r "s/^[^=]+=//")

java "${OPTS[@]}" -Dsoapui.log4j.config=/maven/soapui-log4j.xml -jar "${JAR}" "$@"
