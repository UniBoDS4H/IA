#!/bin/bash
DFILE="$(pwd)/src/main/resources/opencv/library/opencv-455.jar"

mvn install:install-file \
  -Dfile="$DFILE" \
  -DgroupId=ds4h.org.opencv \
  -DartifactId=opencv \
  -Dversion=4.5.5 \
  -Dpackaging=jar
