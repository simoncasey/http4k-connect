#!/bin/bash

set -e
set -o errexit
set -o pipefail
set -o nounset

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

NEW_VERSION=`jq -r .connect.version $DIR/version.json`

echo "Attempting to release $NEW_VERSION"

./gradlew -PreleaseVersion="$NEW_VERSION" test assemble

for i in $(./listProjects.sh); do
    ./gradlew --stacktrace -PreleaseVersion="$NEW_VERSION" :$i:bintrayUpload
done
