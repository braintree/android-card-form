#!/bin/bash

set -e

echo "Make sure to update the version, README and CHANGELOG."
echo "Press enter when you are ready to release, be sure to have an emulator running or device connected to run the tests before release."
read

./gradlew --info clean lint connectedAndroidTest
./gradlew :CardForm:uploadArchives :CardForm:closeAndPromoteRepository

echo "Release complete. Be sure to commit, tag and push your changes."
echo "\n"
