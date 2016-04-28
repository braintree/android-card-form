#!/bin/bash

set -e

echo "Make sure to update the version in build.gradle and the README."
echo "Press enter when you are ready to release."
read

./gradlew --info clean lint test
./gradlew :CardForm:uploadArchives :CardForm:closeAndPromoteRepository

echo "Release complete. Be sure to commit, tag and push your changes."
echo "After the tag has been pushed, update the releases tab on GitHub with the changes for this release."
echo "\n"
read
