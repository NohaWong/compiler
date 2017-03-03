#!/bin/bash

#Les couleurs
RED='\e[0;31m'
GREEN='\e[1;32m'
ORANGE='\e[0;33m'
NEUTRE='\e[0;m'
DEBUG=0

DIR=$(dirname $(readlink -f $0))
TESTS_DIR="$DIR/../tests"

cd $DIR


#Gestion d'option
if [ $# -ne 0 ] && [ $1 != "-v" ]; then
	echo "Usage : ./test_typecheck [option]"
	echo "Options :"
	echo "-h      :       affiche l'aide"
	echo "-v      :       s'arrete à la 1er erreur et l'affiche" 
	exit 0
elif [ $# -eq 1 ] && [ $1 = "-v" ];then
	DEBUG=1
fi


#tests typechecking
echo -e "${ORANGE}-------------Tests typecheck valide------------"
for file in $TESTS_DIR/typechecking/valid/*
do
	echo -e "${NEUTRE}$(basename $file)"
	./minmlc -t $file > /dev/null 2> /dev/null
	if [ $? -ne 0 ]; then
		echo -e "\t ${RED}[KO] Typage incorrect"
		if [ $DEBUG -eq 1  ]; then
			./minmlc -t $file
			exit 1
		fi
	else
		echo -e "\t ${GREEN}[OK] Typage correct"
	fi
done

#invalid
echo -e "${ORANGE}-------------Tests typecheck invalide------------"
for file in $TESTS_DIR/typechecking/invalid/*
do
	echo -e "${NEUTRE}$(basename $file)"
	./minmlc -t $file > /dev/null 2> /dev/null
	if [ $? -ne 0 ]; then
		echo -e "\t ${GREEN}[OK] Typage incorrect"
	else
		echo -e "\t ${RED}[KO] Typage correct"
		if [ $DEBUG -eq 1  ]; then
			./minmlc -t $file
			exit 1
		fi
	fi
done


