#!/bin/sh
# Apache Maven Wrapper startup script, simplified.

BASEDIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_DIR="$BASEDIR/.mvn/wrapper"
JAR="$WRAPPER_DIR/maven-wrapper.jar"
PROPS="$WRAPPER_DIR/maven-wrapper.properties"

if [ ! -f "$JAR" ]; then
  echo "Missing $JAR. Please ensure .mvn/wrapper is present." >&2
  exit 1
fi

JAVA_CMD=${JAVA_HOME:+"$JAVA_HOME/bin/java"}
JAVA_CMD=${JAVA_CMD:-java}

# Official wrapper jar doesn't include a Main-Class in MANIFEST.MF.
# It must be launched by specifying its main class.
exec "$JAVA_CMD" \
  -Dmaven.multiModuleProjectDirectory="$BASEDIR" \
  -classpath "$JAR" \
  org.apache.maven.wrapper.MavenWrapperMain \
  "$@"
