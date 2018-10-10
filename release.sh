#!/bin/bash

set -e

echo "Make sure to update the versions in build.gradle and the README."
echo "Press enter when you are ready to release."
read

if [[ $(./gradlew :CardForm:properties | grep version) == *-SNAPSHOT ]]; then
  echo "Stopping release, the version is a snapshot"
  exit 1
fi

if [ -z "$SONATYPE_USERNAME" ]; then
  echo "Enter Sonatype username:"
  read username
  export SONATYPE_USERNAME=$(echo "${username}")
fi

if [ -z "$SONATYPE_PASSWORD" ]; then
  echo "Enter Sonatype password:"
  read -s password
  export SONATYPE_PASSWORD=$(echo "${password}")
fi
./gradlew --info clean lint test

./gradlew :CardForm:uploadArchives
./gradlew :CardForm:closeRepository
echo "Sleeping for one minute to allow CardForm module to close"
sleep 60
./gradlew :CardForm:promoteRepository

echo "Release complete. Be sure to commit, tag and push your changes."
echo "After the tag has been pushed, update the releases tab on GitHub with the changes for this release."
echo "Remember to bump the version and add '-SNAPSHOT' to the version after the release."
echo
read
