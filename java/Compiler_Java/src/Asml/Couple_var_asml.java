package Asml;

import Expression_asml.Var_asml;

public class Couple_var_asml {
	private Var_asml v1,v2;
	private String path;
	public Couple_var_asml(Var_asml v1, Var_asml v2,String path) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.path = path;
	}
	
	
	public void print(){
		System.out.print(v1.getId());
		System.out.print(" "+v2.getId());
		System.out.println("  "+path);
	}

	public Var_asml getV1() {
		return v1;
	}

	public void setV1(Var_asml v1) {
		this.v1 = v1;
	}

	public Var_asml getV2() {
		return v2;
	}

	public void setV2(Var_asml v2) {
		this.v2 = v2;
	}
	
	public String getPath() {
		return path;
	}

	

	
	
}
