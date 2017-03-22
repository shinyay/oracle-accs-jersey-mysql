#!/bin/bash

# https://maven-repository.com/artifact/org.glassfish.jersey.archetypes/jersey-quickstart-grizzly2
JERSEY_QUICKSTART_GRIZZLY2_VERSION=2.26-b02

# usage
cmdname=`basename $0`
function usage()
{
  echo "Usage: ${cmdname} <groupId> <artifactId>" 1>&2
}

# check arguments
if [ $# -ne 2 ]; then
  usage
  exit 1
fi
GROUP_ID="$1"
ARTIFACT_ID="$2"

mvn archetype:generate \
	-DarchetypeGroupId=org.glassfish.jersey.archetypes \
	-DarchetypeArtifactId=jersey-quickstart-grizzly2 \
	-DarchetypeVersion=${JERSEY_QUICKSTART_GRIZZLY2_VERSION} \
	-DinteractiveMode=false \
	-DgroupId=${GROUP_ID} \
	-DartifactId=${ARTIFACT_ID} \
	-Dversion=1.0.0-SNAPSHOT \
	-Dpackage=${GROUP_ID}
