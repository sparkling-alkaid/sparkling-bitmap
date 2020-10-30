#!/usr/bin/env bash

OSTYPE=`uname`

if [[ "Darwin" == $OSTYPE ]]; then
	alias mysed='sed -i .bak'
else
	alias mysed='sed -i'
fi

# common or starter
MODULE_TYPE=common
MODULE_NAME=default

while getopts "t:n:" arg
do
    case $arg in
        t)
            MODULE_TYPE=$OPTARG;;
        n)
            MODULE_NAME=$OPTARG;;
    esac
done

make_general_project(){
    PROJECT_NAME=$1
    mkdir $PROJECT_NAME
    pushd $PROJECT_NAME
    mkdir -p src/main/java/org/sparling
    mkdir -p src/main/resources
    popd
}

if [[ "common" == $MODULE_TYPE ]]; then
    PROJECT_NAME=sparkling-common-${MODULE_NAME}
    make_general_project $PROJECT_NAME
    cp module-templates/pom-common.template $PROJECT_NAME/pom.xml
    pushd $PROJECT_NAME
    mysed s~__MODULE_NAME__~${MODULE_NAME//_/-}~g pom.xml
    ls | grep .bak | xargs -I {} rm -f {}
    popd
elif [[ "starter" == $MODULE_TYPE ]]; then
    PROJECT_NAME=sparkling-spring-boot-${MODULE_NAME}-starter
    make_general_project $PROJECT_NAME
    cp module-templates/pom-starter.template $PROJECT_NAME/pom.xml
    pushd $PROJECT_NAME
    mkdir -p src/main/resources/META-INF
    cp ../module-templates/spring.factories src/main/resources/META-INF/
    mysed s~__MODULE_NAME__~${MODULE_NAME//_/-}~g pom.xml
    ls | grep .bak | xargs -I {} rm -f {}
    popd
fi