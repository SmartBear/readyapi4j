#!/bin/bash
#
# Copyright 2015 SmartBear Software
#

# find jar to run
cd maven
JAR=$(find -name "*testserver-cucumber-codegen*.jar" -type f)

# extract additional JVM options from ENV vars
OPTS=()
while read -r ITEM; do
    OPTS+=("${ITEM}")
done < <(env | grep -E "^JVM_OPT_[^=]+=" | sed -r "s/^[^=]+=//")

java "${OPTS[@]}" -jar "${JAR}" generate -l TestServerCucumberCodegen -o /output -i "$@"
