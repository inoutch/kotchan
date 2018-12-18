#!/bin/bash

set -e

./gradlew publish
./gradlew uploadArchive