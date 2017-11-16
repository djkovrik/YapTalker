# Src: https://gist.github.com/KioKrofovitch/716e6a681acb33859d16

#!/usr/bin/env bash

function copyEnvVarsToGradleProperties {
    GRADLE_DIR=$HOME"/.gradle"
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES

    echo "Creating Gradle folder..."
    mkdir -p ${GRADLE_DIR}

    echo "Checking Gradle properties file..."
    touch ${GRADLE_PROPERTIES}

    if [ -f "$GRADLE_PROPERTIES" ]; then
        echo "gradle.properties detected."
        echo "Clearing gradle.properties..."
        truncate -s 0 ${GRADLE_PROPERTIES}
    fi

    echo "Adding VK_ACCESS_TOKEN to gradle.properties..."
    echo "VK_ACCESS_TOKEN=\"$VK_ACCESS_TOKEN_ENV\"" >> ${GRADLE_PROPERTIES}

    echo "Adding dummy signing config to gradle.properties..."
    echo "RELEASE_STORE_FILE=\"$DUMMY_PATH_ENV\"" >> ${GRADLE_PROPERTIES}
    echo "RELEASE_STORE_PASSWORD=\"$DUMMY_STRING_ENV\"" >> ${GRADLE_PROPERTIES}
    echo "RELEASE_KEY_ALIAS=\"$DUMMY_STRING_ENV\"" >> ${GRADLE_PROPERTIES}
    echo "RELEASE_KEY_PASSWORD=\"$DUMMY_STRING_ENV\"" >> ${GRADLE_PROPERTIES}

    echo "gradle.properties content:"
    cat ${GRADLE_PROPERTIES}
}
