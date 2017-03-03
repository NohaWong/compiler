package Asml;
import java.io.PrintStream;
import java.util.*;

import Code_fourni.Id;
import Type.TClosure;

import Expression_asml.*;

public class PrintAsml implements Visitor_asml {
	private static PrintStream output; 

	private int niveau;
	private List<String> unit_vars;
	private List<String> need_of___vars;
	
	public PrintAsml(PrintStream output){
		PrintAsml.output = output;
		niveau=0;
		unit_vars = new ArrayList<String>();
		need_of___vars = new ArrayList<String>();
	}
	
	public void Start(List<Exp_asml> ASTl){
		for(Exp_asml e : ASTl){
			e.accept(this);
			output.println();
			niveau = 0;
		}
	}
    
    private void niveler(){
    	for(int i=0; i<niveau; i++)
    		output.print("   ");
    }
    
    private void retour_ligne(){
    	output.println();
    	niveler();
    }
	
    public void visit(Unit_asml e) {
        output.print("()");
    }

    public void visit(Bool_asml e) {
        output.print(e.isB());
    }

    public void visit(Int_asml e) {
        output.print(e.getI());
    }

    public void visit(Float_asml e) {
        String s = String.format(Locale.US, "%.2f", e.getF());
        output.print(s);
    }

    public void visit(Not_asml e) {
        output.print("(not ");
        e.getE().accept(this);
        output.print(")");
    }

    public void visit(Neg_asml e) {
        output.print("neg ");
        e.getE().accept(this);
    }

    public void visit(Add_asml e) {
        output.print("add ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(Sub_asml e) {
        output.print("sub ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(FNeg_asml e){
        output.print("fneg ");
        e.getE().accept(this);
    }

    public void visit(FAdd_asml e){
        output.print("fadd ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(FSub_asml e){
        output.print("fsub ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(FMul_asml e) {
        output.print("fmul ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(FDiv_asml e){
        output.print("fdiv ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
    }

    public void visit(Eq_asml e){
        e.getE1().accept(this);
        output.print(" = ");
        e.getE2().accept(this);
    }

    public void visit(LE_asml e){
        e.getE1().accept(this);
        output.print(" <= ");
        e.getE2().accept(this);
    }

    public void visit(If_asml e){
        output.print("if ");
        e.getE1().accept(this);
        output.print(" then");
        niveau ++;
        retour_ligne();
        e.getE2().accept(this);
        niveau --;
        retour_ligne();
        output.print("else");
        niveau ++;
        retour_ligne();
        e.getE3().accept(this);
        niveau--;
    }

    public void visit(Let_asml e) {
    	if(!(e.getE1() instanceof Unit_asml)){
	    	boolean is_main = e.getId().getId().equals("_");
	        output.print("let ");
	        if(e.getE1() instanceof Float_asml)
	        	need_of___vars.add(e.getId().getId());
    		printId(e.getId());
	        output.print(" = ");
	        if(is_main){
	        	niveau++;
	            retour_ligne();
	        }
	        e.getE1().accept(this);
	        if(!is_main && !(e.getE2() instanceof Unit_asml)){
		       if(e.getT().equals(new TClosure())){
		    	   output.print(" in");
		           retour_ligne();
		       }else{
		    	   output.print(" in");
		           retour_ligne();
		       }
		       e.getE2().accept(this);
	        }
	        else{
	        	niveau --;
	        }
    	}
    	else{
    		unit_vars.add(e.getId().getId());
    		e.getE2().accept(this);
    	}
    }
    

    public void visit(Var_asml e){
    	if(unit_vars.contains(e.getId().getId()))
    		new Unit_asml().accept(this);
    	else{
    		printId(e.getId());
    	}
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        output.print(it.next());
        while (it.hasNext()) {
        	E elem = it.next();
        	output.print(op + elem);
        }
    }

    // print sequence of Exp
    void printInfix2(List<Var_asml> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Var_asml> it = l.iterator();
        it.next().accept(this);
        while (it.hasNext()) {
            output.print(op);
            it.next().accept(this);
        }
    }
    
    void printInfix3(List<Id> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Id> it = l.iterator();
        printId(it.next());
        while (it.hasNext()) {
            output.print(op);
            printId(it.next());
        }
    }
    public void visit(LetRec_asml e){
        output.print("let ");
        if(!e.getFd().getId().getId().startsWith("_"))
        	need_of___vars.add(e.getFd().getId().getId());
        printId(e.getFd().getId());
        output.print(" ");
        printInfix3(e.getFd().getArgs(), " ");
        output.print(" = ");
        niveau++;
        retour_ligne();
        e.getFd().getE().accept(this);
        niveau--;
        retour_ligne();
    }

    public void visit(App_asml e){
        output.print("( call ");
        if(!e.getE().getId().getId().startsWith("_"))
        	need_of___vars.add(e.getE().getId().getId());
        printId(e.getE().getId());
        output.print(" ");
        printInfix2(e.getEs(), " ");
        output.print(")");
    }
    
    public void visit(AppC_asml e) {
    	output.print("( call_closure ");
        e.getE().accept(this);
        output.print(" ");
        printInfix2(e.getEs(), " ");
        output.print(")");	}

  

    public void visit(Array_asml e){
        output.print("(Array.create ");
        e.getE1().accept(this);
        output.print(" ");
        e.getE2().accept(this);
        output.print(")");
    }

    public void visit(Get_asml e){
        e.getE1().accept(this);
        output.print(".(");
        e.getE2().accept(this);
        output.print(")");
    }

    public void visit(Put_asml e){
        output.print("(");
        e.getE1().accept(this);
        output.print(".(");
        e.getE2().accept(this);
        output.print(") <- ");
        e.getE3().accept(this);
        output.print(")");
    }

	public void visit(Let_memory_load_asml e) {
		output.print("(let ");
        printId(e.getId());
        output.print(" = mem(");
        printId(e.getAddr().getId());
        output.print("+"+e.getOffset().getI()+")");
        output.print(" in ");
        e.getE2().accept(this);
        output.print(")");
	}

	public void visit(Let_memory_store_asml e) {
		output.print("(let ");
        printId(e.getId());
        output.print(" = mem(");
        printId(e.getAddr().getId());
        output.print("+"+e.getOffset().getI()+") <- ");
        printId(e.getVal().getId());
        output.print(" in ");
        e.getE2().accept(this);
        output.print(")");
		
	}

	@Override
	public void visit(Let_memory_alloc_asml e) {
		output.print("(let ");
        printId(e.getId());
        output.print(" = new "+e.getOffset().getI());
        output.print(" in ");
        e.getE2().accept(this);
        output.print(")");
		
	}

	private void printId(Id id){
		if(need_of___vars.contains(id.getId()))
			output.print("_");
		
		if(id.getId().startsWith("?"))
			output.print(id.getId().substring(1));
		else
			output.print(id.getId());
	}
	
}


