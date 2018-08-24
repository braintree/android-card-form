#!/bin/bash

set -e

echo "Make sure to update the versions in build.gradle and the README."
echo "Press enter when you are ready to release."
read

if [[ $(./gradlew :CardForm:properties | grep version) == *-SNAPSHOT ]]; then
  echo "Stopping release, the version is a snapshot"
  exit 1
fi

echo -n "Enter Sonatype username:"
read username

echo -n "Enter Sonatype password:"
read -s password
echo

./gradlew --info clean lint test
SONATYPE_USERNAME=$username SONATYPE_PASSWORD=$password ./gradlew :CardForm:uploadArchives :CardForm:closeAndPromoteRepository

echo "Release complete. Be sure to commit, tag and push your changes."
echo "After the tag has been pushed, update the releases tab on GitHub with the changes for this release."
echo "Remember to bump the version and add '-SNAPSHOT' to the version after the release."
echo
read
