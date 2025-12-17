#!/usr/bin/env bash
# Grails Migration Script
if [[ "$1" != *".groovy"* ]]; then
 echo "Filename needs .groovy"
 exit 0 
fi

if [[  -z "$1" ]]; then
	echo "Need filename parameter"
	exit 0
fi

java -jar grails-wrapper.jar dbm-gorm-diff $1 --add