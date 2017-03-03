package Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Main.Main;

@RunWith(Parameterized.class)
public class ClasseTest extends TestCase {
	private final static String FOLDER_TEST = "../../tests/";
	private final static String FOLDER_ML = FOLDER_TEST + "ml/";
	private final static String FOLDER_ARM = FOLDER_TEST + "arm/";
	private final static String FOLDER_RESULT_ACTUAL = FOLDER_TEST
			+ "result_actual/";
	private final static String FOLDER_RESULT_EXPECTED = FOLDER_TEST
			+ "result_expected/";
	private final static String COMPILER_ARM = FOLDER_ARM + "compiler_arm.sh";

	private final String name_file;

	/*
	 * public ClasseTest(String arg0) { super(arg0); }
	 */
	/**
	 * Constructeur pour chaque test
	 * 
	 * @param name_file
	 *            un nom de fichier obtenu grâce à la fonction params
	 */
	public ClasseTest(final String name_file) {
		this.name_file = name_file;
	}

	/**
	 * Main de JUnit
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		junit.textui.TestRunner.run(ClasseTest.class);
	}

	/**
	 * Instanciation d'un tableau paramettre pour chaque test , ici des noms de
	 * fichiers
	 * 
	 * @return un tableau de nom de fichies (sans le .ml)
	 */
	@Parameters
	public static String[] params() {
		File repertoire = new File(FOLDER_ML);
		return remove_dot_ml(repertoire.list());
	}

	/**
	 * Executé avant le début des tests
	 */
	@BeforeClass
	public static void Init() {
		// System.out.println("Debut des Test ....");
	}

	/**
	 * Executé après le début des tests
	 */
	@AfterClass
	public static void End() {
		// System.out.println("Fin des Tests....");

	}

	/**
	 * @deprecated
	 * Lit un fichier Ml dans test/ml/"nom".ml Le traduit en ARM et ecrit le
	 * resultat dans test/arm/"nom".s L'execute et met le resultat dans
	 * test/result_actual/"nom".actual L'execute le fichier Ml avec MinCaml et
	 * met le resultat dans test/result_expected/"nom".expected Fait la
	 * comparaison Si les 2 resultats sont identique , le test passe
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_a_ml() throws Exception {
		
		
		System.out.println("####################################");
		System.out.println("Test with : " + name_file + ".ml");
		
		Runtime runtime = Runtime.getRuntime();
		//Traduction arm
		String[] args_Trad = {"-allR","-be","-o", FOLDER_ARM + name_file + ".s" ,FOLDER_ML + name_file + ".ml" };
		new Main(args_Trad);
		
		//Execution arm
		
		String[] args_Make = {"/bin/sh","-c","./"+ COMPILER_ARM +" "
				+ FOLDER_ARM +name_file +".s" + " "
				+ FOLDER_ARM + " " 
				+ FOLDER_RESULT_ACTUAL + name_file + ".actual"};
		runtime.exec(args_Make);
		
		
		
		//String[] args_ARM = {"/bin/sh" , "-c" ,"qemu-arm "+ FOLDER_ARM + name_file + ".arm / > " + FOLDER_RESULT_ACTUAL + name_file + ".actual"};
		//runtime.exec(args_ARM);
		//qemu-arm namefile.arm
			
		// Execution Ocaml
		
		String arg = new String("ocaml " + FOLDER_ML + name_file + ".ml / > "
				+ FOLDER_RESULT_EXPECTED + name_file + ".expected");
		String[] args_ocaml = { "/bin/sh", "-c", arg };
		runtime.exec(args_ocaml);

		// System.out.println(args);
		//Comparaison
		FileReader actual = new FileReader(FOLDER_RESULT_ACTUAL + name_file + ".actual");
		FileReader expected = new FileReader(FOLDER_RESULT_EXPECTED + name_file + ".expected");
		
		assert (is_equals(actual,expected));
		actual.close();
		expected.close();

	}
	
	/**
	 * Renvoi vrai si les 2 fichiers passé en parametres sont égaux
	 * @param actual un FileReader
	 * @param expected un FileReader
	 * @return un boolean
	 * @throws IOException si le fichier n'est pas ouvert
	 */
	private boolean is_equals(FileReader actual, FileReader expected) throws IOException {
		int c_actual;
		int c_expected;
		
		do 
		{
			c_actual = actual.read();
			c_expected =expected.read();
		}while(c_actual != -1 && c_expected !=-1 && c_actual == c_expected);
		
		return (c_actual == -1 && c_expected != -1);
	}

	/**
	 * Supprime le .ml à la fin de chaque fichier
	 * 
	 * @param listefichiers
	 *            une liste de fichier .ml sour la forme d'un String[]
	 * @return une nouvelle liste de fichier sans l'extention .ml
	 */
	private static String[] remove_dot_ml(String[] listefichiers) {

		int i;
		for (i = 0; i < listefichiers.length; i++) {
			if (listefichiers[i].endsWith(".ml") == true) {
				listefichiers[i] = listefichiers[i].replaceAll(".ml", "");
			}
		}
		return listefichiers;
	}

}
