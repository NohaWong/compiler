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
	echo "Usage : ./test_syntax [option]"
	echo "Options :"
	echo "-h      :       afficher l'aide"
	echo "-v      :       s'arreter à la 1ere erreur et l'afficher" 
	exit 0
elif [ $# -eq 1 ] && [ $1 = "-v" ];then
	DEBUG=1
fi


#tests parsing
echo -e "${ORANGE}-------------Tests syntaxe valide-------------"
for file in $TESTS_DIR/syntax/valid/*
do
	echo -e "${NEUTRE}$(basename $file)"
	./minmlc -p $file > /dev/null 2> /dev/null
	if [ $? -ne 0 ]; then
		echo -e "\t ${RED}[KO] Syntaxe incorrecte"
		if [ $DEBUG -eq 1 ]; then
			./minmlc -p $file
			exit 1
		fi
	else
		echo -e "\t ${GREEN}[OK] Syntaxe correcte"
	fi
done

#invalid
echo -e "${ORANGE}-------------Tests syntaxe invalide-------------"
for file in $TESTS_DIR/syntax/invalid/*
do
	echo -e "${NEUTRE}$(basename $file)"
	./minmlc -p $file > /dev/null 2> /dev/null
	if [ $? -ne 0 ]; then
		echo -e "\t ${GREEN}[OK] Syntaxe incorrecte"
	else
		echo -e "\t ${RED}[KO] Syntaxe correcte"
		if [ $DEBUG -eq 1  ]; then
			./minmlc -p $file
			exit 1
		fi
	fi
done
