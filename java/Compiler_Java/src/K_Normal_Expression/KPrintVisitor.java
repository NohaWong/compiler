package K_Normal_Expression;
import java.util.*;

public class KPrintVisitor implements KVisitor {
	
	private int niveau;
	
    public KPrintVisitor() {
    	niveau = 0;
	}
    
    private void niveler(){
    	for(int i=0; i<niveau; i++)
    		System.out.print("   ");
    }

	public void visit(KUnit e) {
		niveler();
        System.out.print("()");
    }

    public void visit(KBool e) {
		niveler();
        System.out.print(e.isB());
    }

    public void visit(KInt e) {
		niveler();
        System.out.print(e.getI());
    }

    public void visit(KFloat e) {
		niveler();
        String s = String.format("%.2f", e.getF());
        System.out.print(s);
    }

    public void visit(KNot e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(not ");
        e.getV().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KNeg e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(- ");
        e.getV().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KAdd e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" + ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KSub e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" - ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KFNeg e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(-. ");
        e.getV().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KFAdd e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" +. ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KFSub e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" -. ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KFMul e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" *. ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KFDiv e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" /. ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KEq e){
		niveler();
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" = ");
        e.getV2().accept(this);
        System.out.print(")");
    }

    public void visit(KLE e){
		niveler();
        System.out.print("(");
        e.getV1().accept(this);
        System.out.print(" <= ");
        e.getV2().accept(this);
        System.out.print(")");
    }

    public void visit(KIf e){
    	int niveau_present = niveau;
    	niveler();
        System.out.print("if ");
        niveau = 0;
        e.getE1().accept(this);
        System.out.print(" then {");
        System.out.println();
        niveau = niveau_present;
        niveau ++;
        e.getE2().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        System.out.println();
        niveler();
        System.out.print("else {");
        System.out.println();
        niveau ++;
        e.getE3().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        //System.out.println();
    }

    public void visit(KLet e) {
    	boolean do_ln = (e.getE1() instanceof KIf ||
    			e.getE1() instanceof KLet ||
    			e.getE1() instanceof KLetRec ||
    			e.getE1() instanceof KLetTuple ||
    			e.getE1() instanceof KArray ||
    			e.getE1() instanceof KPut);
    	int niveau_present = niveau;
		niveler();
        System.out.print("let ");
        System.out.print(e.getId());
        System.out.print(" =");
        if(do_ln){
	        System.out.println();
	        niveau ++;
        }
        else{
        	System.out.print(" ");
        	niveau = 0;
        }
        e.getE1().accept(this);
        if(do_ln){
	        System.out.println();
	        niveau --;
	        niveler();
        }
        else{
        	System.out.print(" ");
        	niveau = niveau_present;
        }
        System.out.println("in");
        //niveau ++;
        e.getE2().accept(this);
        //niveau --;
        //System.out.println();
    }

    public void visit(KVar e){
    	niveler();
    	System.out.print(e.getId());
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        System.out.print(it.next());
        while (it.hasNext()) {
            System.out.print(op + it.next());
        }
    }

    // print sequence of Exp
    void printInfix2(List<KVar> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<KVar> it = l.iterator();
        int niveau_present = niveau;
    	niveau = 0;
        it.next().accept(this);
        while (it.hasNext()) {
            System.out.print(op);
            it.next().accept(this);
        }
        niveau = niveau_present;
    }

    public void visit(KLetRec e){
    	niveler();
        System.out.print("let rec " + e.getFd().getId() + " ");
        printInfix(e.getFd().getArgs(), " ");
        System.out.print(" = {");
        System.out.println();
        niveau ++;
        e.getFd().getE().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        System.out.println();
        niveler();
        System.out.print("in {");
        System.out.println();
        niveau ++;
        e.getE().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        //System.out.println();
    }

    public void visit(KApp e){
    	niveler();
    	int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getV().accept(this);
        System.out.print(" ");
        printInfix2(e.getVs(), " ");
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KTuple e){
    	niveler();
        System.out.print("(");
        printInfix2(e.getVs(), ", ");
        System.out.print(")");
    }

    public void visit(KLetTuple e){
    	boolean do_ln = (e.getE1() instanceof KIf ||
    			e.getE1() instanceof KLet ||
    			e.getE1() instanceof KLetRec ||
    			e.getE1() instanceof KLetTuple ||
    			e.getE1() instanceof KArray ||
    			e.getE1() instanceof KPut);
    	int niveau_present = niveau;
		niveler();
        System.out.print("let (");
        printInfix(e.getIds(),", ");
        System.out.print(") =");
        if(do_ln){
	        System.out.println();
	        niveau ++;
        }
        else{
        	System.out.print(" ");
        	niveau = 0;
        }
        e.getE1().accept(this);
        if(do_ln){
	        System.out.println();
	        niveau --;
	        niveler();
        }
        else{
        	System.out.print(" ");
        	niveau = niveau_present;
        }
        System.out.println("in");
        //niveau ++;
        e.getE2().accept(this);
        //niveau --;
        //System.out.println();
    }

    public void visit(KArray e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        System.out.print("(new Array [");
        e.getV1().accept(this);
        System.out.print("] : ");
        e.getV2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(KGet e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        e.getV1().accept(this);
        System.out.print("[");
        e.getV2().accept(this);
        System.out.print("]");
        niveau = niveau_present;
    }

    public void visit(KPut e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        e.getV1().accept(this);
        System.out.print("[");
        e.getV2().accept(this);
        System.out.print("] <- ");
        System.out.println();
        niveau = niveau_present;
        niveau ++;
        e.getV3().accept(this);
        niveau --;
    }
}


