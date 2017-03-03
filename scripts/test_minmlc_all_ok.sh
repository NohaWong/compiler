#!/bin/bash
#Créé par Jonathan Granier
#test tout les fichiers ml contenu dans les dossiers de tests : syntax , typechecking et gencode


SCRIPTS_DIR=$(dirname $(readlink -f $0))
GENERAL_DIR=$(dirname $SCRIPTS_DIR)
TESTS_DIR="$GENERAL_DIR/tests"

cd $SCRIPTS_DIR

function Test_gencode(){
	echo -e "${ORANGE}---------- Dans le dossier $1 -------------"
	for file in $1/* 
	do
		if [ -d $file ]; then
			Test_gencode $file $2
		elif [ ${file##*.} = "ml" ]; then
			echo -e "${NEUTRE}$(basename $file)"
			./test_minmlc.sh $file $2
			if [ $? -ne 0 ] && [ $# -eq 2 ] && [ $2 = "-v" ]; then
				clean
				exit 1
			fi
		fi
	done

}


#Gestion d'option
if [ $# -ne 0 ] && [ $1 != "-v" ]; then
	echo "Usage : ./test_minmlc_all.sh [option]"
	echo "Options :"
	echo "-h      :       afficher l'aide"
	echo "-v      :       s'arreter à la 1ere erreur et l'afficher" 
	exit 0
fi

export PATH=$PATH:../test
#Les couleurs
RED='\e[0;31m'
GREEN='\e[1;32m'
ORANGE='\e[0;33m'
NEUTRE='\e[0;m'

#tests syntax
#valid



echo -e "${ORANGE}-------------Tests gencode-------------"
Test_gencode $TESTS_DIR/gencode_ok $1
$TESTS_DIR/clean.sh

#tests gencode





