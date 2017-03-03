all:
	ant -buildfile java/Compiler_Java/build.xml build

test:
	./scripts/test_syntax.sh
	./scripts/test_typecheck.sh
	./scripts/test_minmlc_all.sh

test_asml:
	./scripts/test_minmlc_asml_all.sh

test_gencode:
	./scripts/test_minmlc_all.sh

clean :
	ant -buildfile java/Compiler_Java/build.xml clean
	./tests/clean.sh
