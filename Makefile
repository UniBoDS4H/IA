SHELL:=/bin/bash

build_x86:
	mvn install:install-file \
        -Dfile="src/main/resources/opencv/x86_jar/opencv-455.jar" \
        -DgroupId="org" \
        -DartifactId="opencv" \
        -Dversion="4.5.5" \
        -Dpackaging=jar \
        -DgeneratePom=true


build_mac:
	mvn install:install-file \
        -Dfile="src/main/resources/opencv/arm_jar/opencv-455.jar" \
        -DgroupId="org" \
        -DartifactId="opencv" \
        -Dversion="4.5.5" \
        -Dpackaging=jar \
        -DgeneratePom=true


build_linux:
	mvn install:install-file \
        -Dfile="src/main/resources/opencv/linux_jar/opencv-455.jar" \
        -DgroupId="org" \
        -DartifactId="opencv" \
        -Dversion="4.5.5" \
        -Dpackaging=jar \
        -DgeneratePom=true


build_windows:
	mvn install:install-file \
        -Dfile="src/main/resources/opencv/win_jar/opencv-455.jar" \
        -DgroupId="org" \
        -DartifactId="opencv" \
        -Dversion="4.5.5" \
        -Dpackaging=jar \
        -DgeneratePom=true

create_release:
	mvn package -P uberjar

create_jar: create_release
	echo "Jar created successfully!!"

create_jar_linux: create_release
	echo "JAR for linux created"

create_jar_windows: build_windows create_release
	echo "JAR for windows created"

create_jar_mac_intel: build_x86 create_release
	echo "JAR for mac intel created"

create_jar_mac_silicon: build_mac create_release
	echo "JAR for mac silicon created"
