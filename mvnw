#!/bin/sh
# Maven Wrapper script for Unix systems

set -e

# Find project base dir
find_maven_basedir() {
    local basedir=$(dirname "$0")
    if [ -f "$basedir/pom.xml" ]; then
        echo "$basedir"
    else
        echo "."
    fi
}

BASE_DIR=$(find_maven_basedir)
WRAPPER_JAR="$BASE_DIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$BASE_DIR}"

# Download wrapper jar if not exists
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    
    if [ -f "$WRAPPER_PROPERTIES" ]; then
        WRAPPER_URL=$(grep "wrapperUrl" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    fi
    
    if [ -z "$WRAPPER_URL" ]; then
        WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    fi
    
    mkdir -p "$(dirname "$WRAPPER_JAR")"
    
    if command -v wget > /dev/null; then
        wget -q -O "$WRAPPER_JAR" "$WRAPPER_URL"
    elif command -v curl > /dev/null; then
        curl -s -o "$WRAPPER_JAR" "$WRAPPER_URL"
    else
        echo "Error: wget or curl is required"
        exit 1
    fi
fi

# Get Maven distribution URL
if [ -f "$WRAPPER_PROPERTIES" ]; then
    MAVEN_URL=$(grep "distributionUrl" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
fi

if [ -z "$MAVEN_URL" ]; then
    MAVEN_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
fi

# Maven home directory
MAVEN_USER_HOME="${MAVEN_USER_HOME:-$HOME/.m2}"
MAVEN_HOME="$MAVEN_USER_HOME/wrapper/dists/apache-maven-3.9.6"

# Download and extract Maven if not exists
if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_USER_HOME/wrapper/dists"
    
    MAVEN_ZIP="$MAVEN_USER_HOME/wrapper/dists/maven.zip"
    
    if command -v wget > /dev/null; then
        wget -q -O "$MAVEN_ZIP" "$MAVEN_URL"
    elif command -v curl > /dev/null; then
        curl -s -o "$MAVEN_ZIP" "$MAVEN_URL"
    fi
    
    unzip -q "$MAVEN_ZIP" -d "$MAVEN_USER_HOME/wrapper/dists"
    rm "$MAVEN_ZIP"
fi

# Run Maven
exec "$MAVEN_HOME/bin/mvn" "$@"

