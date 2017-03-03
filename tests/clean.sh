#!/bin/bash

# Si appelé avec en paramètre 1, ou sans paramètre, fait un clean total ; si appelé avec 0; ne supprime pas les .s ni .asml

TESTS_DIR=$(dirname $(readlink -f $0))

if [ $# -eq 1 ]
then
	suppr_s=$1
else
	suppr_s=1
fi

function cleanDir(){
	rm -f $1/*.arm
	rm -f $1/*.o
	if [ $suppr_s -ne 0 ]
	then
		rm -f $1/*.s
		rm -f $1/*.asml
	fi
	rm -f $1/*.actual
	rm -f $1/*.expected
	for i in $1/*
	do
		if [ -d $i ]
		then
			cleanDir $i
		fi
	done
}

cleanDir $TESTS_DIR/gencode
cleanDir $TESTS_DIR/gencode_ok
