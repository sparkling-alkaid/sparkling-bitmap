#!/usr/bin/env bash

set -ex

sed_in_place() {
    OSTYPE=`uname`
    if [[ "Darwin" == $OSTYPE ]]; then
        sed -i "" $1 $2
    else
        sed -i $1 $2
    fi
}

VERSION=${VERSION:-0.0.0}


while getopts "v:V:" arg
do
    case $arg in
        v)
            VERSION=$OPTARG;;
        V)
            VERSION=$OPTARG;;
    esac
done


find . -name 'pom.xml' | while read file; do
  echo $file 
  # 替换 version 等操作
  sed_in_place s~0.0.0.0~$VERSION~g $file
 done