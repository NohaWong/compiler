------------------------------------------------------------------------------------------------------------------------------------------
		Minmlc - Compilateur de MinCaml en Java - Equipe BugPerture Science
------------------------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------------------------
			Compilation 
------------------------------------------------------------------------------------------------------------------------------------------

Faire make à la racine pour compiler le projet (make clean pour le nettoyer)

------------------------------------------------------------------------------------------------------------------------------------------
			 Execution
------------------------------------------------------------------------------------------------------------------------------------------

Dans le dossier scripts faire ./minmlc pour executer le compilateur
Aide et options:
usage : ./minmlc [options] <file.ml>
		Available options :
		-o <file>           :       set the output file - "default.s" by default
		-h                  :       display this help
		-v                  :       display version
		-t                  :       do type check ONLY
		-p                  :       parse ONLY
		-asml <file>        :       set an output file for the generated asml code
		-ps                 :       print all the compilation and AST steps

		## Reductions ##
		-allR               :       do all reductions (K-Normalization, Alpha-Conversion, Beta-Reduction, Let-Reduction
		-kn                 :       do K-Normalization
		-ac                 :       do Alpha-Conversion
		-br                 :       do Beta-Reduction
		-lr                 :       do Let-Reduction

		## Optimisations ##
		-allO               :       do all optimisations (Inline Expansion, Constant Folding, Unused Definition Suppression
		-io                 :       do Inline Expansion Optimisation
		-io_c <v>           :       do Inline Expansion Optimisation with a function size threshold of v
		-cfo                :       do Constant Folding Optimisation
		-udo                :       do Unused Definition Suppression Optimisation
		-iter <v>           :       do all optimisations v times

		## BackEnd ##
		-as                 :       do asml translate
		-be                 :       do Back-End




------------------------------------------------------------------------------------------------------------------------------------------
								Ensemble de tests
------------------------------------------------------------------------------------------------------------------------------------------

Pour faire tous les tests, aller à la racine et faire
		> $ make test

------------------------------------------------------------------------------------------------------------------------------------------
								Tests Unitaires
------------------------------------------------------------------------------------------------------------------------------------------

test_syntax.sh teste syntaxiquement tous les fichiers ".ml" dans le dossier tests/syntax/
		
		Usage : ./test_syntax [option]
		Options :
		-h      :       afficher l'aide
		-v      :       s'arreter à la 1ere erreur et l'afficher 

test_typecheck.sh test le typage tous les fichiers ".ml" dans le dossier tests/typechecking/

		Usage : ./test_typecheck [option]
		Options :
		-h      :       afficher l'aide
		-v      :       s'arreter à la 1ere erreur et l'afficher 

test_minmlc.sh fait un test complet sur un fichier ".ml" , compilation , execution et comparaison avec l'éxécution ocaml
 
		Usage : ./test_minmlc.sh <file.ml> [option]
		Options :
		-h      :       afficher l'aide"
		-v      :       s'arreter à la 1ere erreur et l'afficher

test_minmlc_all.sh fexécute le script ci-dessus sur tous les fichiers de test (dossier tests/gencode)

		Usage : ./test_minmlc_all.sh [option]
		Options :
		-h      :       afficher l'aide
		-v      :       s'arreter à la 1ere erreur et l'afficher

test_minmlc_asml.sh fait un test complet sur un fichier ".ml" sur la génération asml - c'est un test du front-end
 
		Usage : ./test_minmlc.sh <file.ml> [option]
		Options :
		-h      :       afficher l'aide"
		-v      :       s'arreter à la 1ere erreur et l'afficher

test_minmlc_asml_all.sh exécute le script ci-dessus sur tous les fichiers de test (dossier tests/gencode)

		Usage : ./test_minmlc_all.sh [option]
		Options :
		-h      :       afficher l'aide
		-v      :       s'arreter à la 1ere erreur et l'afficher














