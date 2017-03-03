package Asml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Code_fourni.Id;
import Expression_asml.Add_asml;
import Expression_asml.AppC_asml;
import Expression_asml.App_asml;
import Expression_asml.Array_asml;
import Expression_asml.Bool_asml;
import Expression_asml.Eq_asml;
import Expression_asml.Exp_asml;
import Expression_asml.FAdd_asml;
import Expression_asml.FDiv_asml;
import Expression_asml.FMul_asml;
import Expression_asml.FNeg_asml;
import Expression_asml.FSub_asml;
import Expression_asml.Float_asml;
import Expression_asml.FunDef_asml;
import Expression_asml.Get_asml;
import Expression_asml.If_asml;
import Expression_asml.Int_asml;
import Expression_asml.LE_asml;
import Expression_asml.LetRec_asml;
import Expression_asml.Let_asml;
import Expression_asml.Let_memory_alloc_asml;
import Expression_asml.Let_memory_load_asml;
import Expression_asml.Let_memory_store_asml;
import Expression_asml.Neg_asml;
import Expression_asml.Not_asml;
import Expression_asml.Put_asml;
import Expression_asml.Sub_asml;
import Expression_asml.Unit_asml;
import Expression_asml.Var_asml;
import K_Normal_Expression.KExp;
import K_Normal_Expression.KObjVisitor;
import K_Normal_Expression.KVar;
import Type.TClosure;
import Type.TUnit;
import Type.Type;

public class Ajout_Closure_Rec  implements ObjVisitor_asml<Exp_asml>{
	private List<Closure_Fonction_Var> List_closure_fonction;
	
	public Ajout_Closure_Rec(List<Closure_Fonction_Var> list_closure_fonction) {
		super();
		List_closure_fonction = list_closure_fonction;
	}
	
	
	public Exp_asml Start(Exp_asml ast)
	{
		return ast.accept(this);
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @return une Expression de type List<Exp> modifié 
	 */
	List<Var_asml> accept_list(List<Var_asml> l) {
        List<Var_asml> retour = new ArrayList<Var_asml>();
		if (l.isEmpty()) {
            return retour;
        }
        Iterator<Var_asml> it = l.iterator();
        Var_asml exp = (Var_asml)it.next().accept(this);
        retour.add(exp);
        while (it.hasNext()) {
            exp = (Var_asml)it.next().accept(this);
            retour.add(exp);
        }
        return retour;
    }
	
	
	@Override
	public Exp_asml visit(Unit_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Bool_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Int_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Float_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Not_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Neg_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Add_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Sub_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(FNeg_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(FAdd_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(FSub_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(FMul_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(FDiv_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Eq_asml e) {
		// TODO Auto-generated method stub
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		Eq_asml retour = new Eq_asml(e1,e2);
		return retour;
	}

	@Override
	public Exp_asml visit(LE_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		LE_asml retour = new LE_asml(e1,e2);
		return retour;
	}

	@Override
	public Exp_asml visit(If_asml e) {
		
		Exp_asml e1 = e.getE1().accept(this);
		Exp_asml e2 = e.getE2().accept(this);		
		Exp_asml e3 = e.getE3().accept(this);
		If_asml retour = new If_asml(e1,e2,e3);
		return retour;
	}

	@Override
	public Exp_asml visit(Let_asml e) {
		Exp_asml e1 = e.getE1().accept(this);
		Exp_asml e2 = e.getE2().accept(this);
		
		Exp_asml retour = new Let_asml(e.getId(),e.getT(),e1,e2);
		
		int i = 0;

		

		if( e1 instanceof AppC_asml && e.getE1() instanceof App_asml)
		{
			//System.out.println("COUCOU");
			Id e1_id = ((App_asml)e.getE1()).getE().getId();
			int index = Estdans_list_Closure_Fonction(e1_id.getId());
			
			
			if(index != -1)
			{
				
				
				Exp_asml retour_alloc = e1;
				List<String> list_var_alloc = List_closure_fonction.get(index).getList_var_alloc();
				if(!list_var_alloc.isEmpty()){
				
					
					Let_memory_store_asml alloc;
					Id closure_name = List_closure_fonction.get(index).getName_closure();
					for(int j = list_var_alloc.size()-1;j>=0;j--){
						i++;
						String name = new String();
						name = list_var_alloc.get(j);
						
						Var_asml var = new Var_asml(new Id(name));
						
						if (j == list_var_alloc.size()-1)
						{
							
							alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,retour);
						}
						else
						{
							alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,retour_alloc);
						}
						
						
						retour_alloc = alloc;
					}
					Id id_addr = Id.gen(); 
					//System.out.println("ON FAIT DES RAJOUTS : SUR : "+ closure_name.getId());
					alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml(0),new Var_asml(id_addr),retour_alloc);
					retour_alloc = alloc;
					Let_asml Let_addr = new Let_asml(id_addr,new TUnit(),new Var_asml(e1_id),retour_alloc);
					retour_alloc = Let_addr;
					retour_alloc = new Let_memory_alloc_asml(closure_name,new TClosure(),new Int_asml((i+1)*4),retour_alloc);
					retour = retour_alloc;
				}
			}
		
		}
		
		if( e2 instanceof AppC_asml && e.getE2() instanceof App_asml)
		{
			//System.out.println("COUCOU");
			Id e2_id = ((App_asml)e.getE2()).getE().getId();
			int index = Estdans_list_Closure_Fonction(e2_id.getId());
			
			
			if(index != -1)
			{
				
				
				Exp_asml retour_alloc = e1;
				List<String> list_var_alloc = List_closure_fonction.get(index).getList_var_alloc();
				if(!list_var_alloc.isEmpty()){
				
					
					Let_memory_store_asml alloc;
					Id closure_name = List_closure_fonction.get(index).getName_closure();
					for(int j = list_var_alloc.size()-1;j>=0;j--){
						i++;
						String name = new String();
						name = list_var_alloc.get(j);
						
						Var_asml var = new Var_asml(new Id(name));
						
						if (j == list_var_alloc.size()-1)
						{
							
							alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,e2);
						}
						else
						{
							alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,retour_alloc);
						}
						
						
						retour_alloc = alloc;
					}
					Id id_addr = Id.gen(); 
					//System.out.println("ON FAIT DES RAJOUTS : SUR : "+ closure_name.getId());
					alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml(0),new Var_asml(id_addr),retour_alloc);
					retour_alloc = alloc;
					Let_asml Let_addr = new Let_asml(id_addr,new TUnit(),new Var_asml(e2_id),retour_alloc);
					retour_alloc = Let_addr;
					retour_alloc = new Let_memory_alloc_asml(closure_name,new TClosure(),new Int_asml((i+1)*4),retour_alloc);
					retour = new Let_asml(e.getId(),e.getT(),e1,retour_alloc);
				}
			}
		
		}
		
		
		return retour;
	}

	@Override
	public Exp_asml visit(Var_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(LetRec_asml e) {

		Id id = new Id(e.getFd().getId().toString());
		List<Id> args = new ArrayList<Id>(e.getFd().getArgs());
		Exp_asml e1 = e.getFd().getE().accept(this);
		FunDef_asml fd = new  FunDef_asml(id,new TClosure(),args,e1);
		Exp_asml retour = new LetRec_asml(fd,new Unit_asml());
		
		return retour;
	}

	@Override
	public Exp_asml visit(App_asml e) {
		// TODO Auto-generated method stub
		Var_asml e1 = (Var_asml)e.getE().accept(this);
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getEs()));
		int index = Estdans_list_Closure_Fonction(e1.getId().getId());
		if(index != -1)
		{
			
			Id closure_name = Id.gen();
			Var_asml ret = new Var_asml(closure_name, new TClosure());
			Exp_asml retour = new AppC_asml(ret,le);
			
			List_closure_fonction.get(index).setName_closure(closure_name);
			return retour;
		}
			
			
		
		
		return e;
	}

	@Override
	public Exp_asml visit(Array_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Get_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(Put_asml e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp_asml visit(AppC_asml e) {
		// TODO Auto-generated method stub
		
		
		
		
		
		return e;
	}

	@Override
	public Exp_asml visit(Let_memory_load_asml e) {
		Exp_asml e2 = e.getE2().accept(this);
		Exp_asml retour = new Let_memory_load_asml(e.getId(),e.getT(),e.getAddr(),e.getOffset(),e2);
		return retour;
	}

	@Override
	public Exp_asml visit(Let_memory_store_asml e) {
		Exp_asml e2 = e.getE2().accept(this);
		Exp_asml retour = new Let_memory_store_asml(e.getId(),e.getT(),e.getAddr(),e.getOffset(),e.getVal(),e2);
		return retour;
	}

	@Override
	public Exp_asml visit(Let_memory_alloc_asml e) {
		Exp_asml e2 = e.getE2().accept(this);
		Exp_asml retour = new Let_memory_alloc_asml(e.getId(),e.getT(),e.getOffset(),e2);
		return retour;
	}
	
	
	private int Estdans_list_Closure_Fonction(String p)
	{
		int index = -1;
		for(int i = 0;i < List_closure_fonction.size();i++)
		{
			
			if (List_closure_fonction.get(i).getFonction().equals(p))
			{
				index = i;
			}
		}
		return index;
	}

}
