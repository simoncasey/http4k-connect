#!/bin/bash

set -e
set -o errexit
set -o pipefail
set -o nounset

NEW_VERSION=$1

git stash

BINTRAY_VERSION=$(curl -s https://bintray.com/api/v1/packages/http4k/maven/http4k-connect-bom/versions/_latest | tools/jq -r .name)

sed -i '' s/"$BINTRAY_VERSION"/"$NEW_VERSION"/g README.md
sed -i '' s/"$BINTRAY_VERSION"/"$NEW_VERSION"/g version.json

git add README.md
git commit -am"upgrade"
git tag -a "$NEW_VERSION" -m "http4k-connect version $NEW_VERSION (was $BINTRAY_VERSION)"
git push origin "$NEW_VERSION"

git push

git stash apply
