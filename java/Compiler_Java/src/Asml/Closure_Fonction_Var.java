package Asml;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;

public class Closure_Fonction_Var {
	private List<String> list_var_alloc = new ArrayList<String>();
	private String fonction;
	private Id name_closure;
	
	
	public Closure_Fonction_Var(List<String> list_var_alloc, String fonction) {
		super();
		this.list_var_alloc = list_var_alloc;
		this.fonction = fonction;
	}
	
	public List<String> getList_var_alloc() {
		return list_var_alloc;
	}
	public void setList_var_alloc(List<String> list_var_alloc) {
		this.list_var_alloc = list_var_alloc;
	}
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Id getName_closure() {
		return name_closure;
	}

	public void setName_closure(Id name_closure) {
		this.name_closure = name_closure;
	}

	
}
