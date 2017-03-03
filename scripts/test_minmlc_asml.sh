#!/bin/bash

#Créé pas Jonathan Granier
#Test 1 fichier ml ecrit en parametres
#Place fichier creer au meme endroit que le fichier fourni avec le meme nom mais des extensions differentes


export PATH=$PATH:/opt/gnu/arm/bin
FILE=$1
DEBUG=0
ARM_AS=arm-eabi-as
ARM_LD=arm-eabi-ld

#Les couleurs
RED='\e[0;31m'
GREEN='\e[1;32m'

if [ "$#" -eq 0 ] ;then 
	echo -e "${RED}Erreur : Spécifier un fichier à compiler"
	exit 1
fi

#Les variable

#Gestion d'option
if [ $# -ne 1 ] && [ $2 != "-v" ]; then
	echo "Usage : ./test_minmlc_asml.sh <file.ml> [option]"
	echo "Options :"
	echo "-h      :       afficher l'aide"
	echo "-v      :       s'arreter à la 1ere erreur et l'afficher" 
	exit 0

elif [ $# -eq 2 ] && [ $2 = "-v" ];then
	DEBUG=1
fi

LIB="libs/libmincaml.S" 
FILECOMPILE=${FILE%.*}
File_asml=$FILECOMPILE".asml"
File_asml=$(readlink -f $File_asml)
File_ml=$(readlink -f $1)
File_expected=$FILECOMPILE".expected"
File_actual=$FILECOMPILE".actual"



#Execute en OCaml
ocaml $File_ml > $File_expected 2> /dev/null
if [ $? -ne 0 ]; then
	echo -e "\t ${RED}[KO] Erreur ocaml"
	if [ $DEBUG -eq 1 ]; then
		ocaml $File_ml
	fi
	exit 1 
fi



#Compile
ant -buildfile ../java/Compiler_Java/build.xml run -Dargs="-allR -allO -as -asml $File_asml $File_ml" > /dev/null 2> /dev/null
if [ $? -ne 0 ]; then
	echo -e "\t ${RED}[KO] Erreur compilateur minml"
	if [ $DEBUG -eq 1 ]; then
		ant -buildfile ../java/Compiler_Java/build.xml run -Dargs="-allR -allO -as -asml $File_asml $File_ml"
	fi
	exit 1
fi

#exécute asml
../tools/asml $File_asml > $File_actual 2> /dev/null
if [ $? -ne 0 ]; then
	echo -e "\t ${RED}[KO] Erreur ASML"
	if [ $DEBUG -eq 1 ]; then
		../tools/asml $File_asml
	fi
	exit 1
fi

#Compare

DIFF=$(diff $File_expected $File_actual)
if [ "$DIFF" != "" ]; then
	echo -e "\t ${RED}[KO] Résultat incorrect"
	if [ $DEBUG -eq 1 ]; then
		echo $DIFF
	fi
	exit 1
else
	echo -e "\t ${GREEN}[OK] Résultat correct"
fi
