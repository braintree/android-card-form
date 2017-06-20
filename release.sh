#!/bin/bash

set -e

echo "Make sure to update the version in build.gradle to the correct version and remove the `-SNAPSHOT`."
echo "Also update the version in README.md."
echo "Press enter when you are ready to release."
read

./gradlew --info clean lint test
./gradlew :CardForm:uploadArchives :CardForm:closeAndPromoteRepository

echo "Release complete. Be sure to commit, tag and push your changes."
echo "After the tag has been pushed, update the releases tab on GitHub with the changes for this release."
echo "After committing the release be sure to update the version and version code in the build.gradle and README.md to increase the patch version and add `-SNAPSHOT`. "
echo "\n"
read
