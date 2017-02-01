#!/bin/bash

# Beware that run.profiles is the name chosen by the authors of the maven plugin, which
# is different from spring.profiles.active
mvn -Drun.profiles=dev spring-boot:run

# This also works
# SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run