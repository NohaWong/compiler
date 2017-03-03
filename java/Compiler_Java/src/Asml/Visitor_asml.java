package Asml;

import Expression_asml.*;

public interface Visitor_asml {

    void visit(Unit_asml e);
    void visit(Bool_asml e);
    void visit(Int_asml e);
    void visit(Float_asml e);
    void visit(Not_asml e);
    void visit(Neg_asml e);
    void visit(Add_asml e);
    void visit(Sub_asml e);
    void visit(FNeg_asml e);
    void visit(FAdd_asml e);
    void visit(FSub_asml e);
    void visit(FMul_asml e);
    void visit(FDiv_asml e);
    void visit(Eq_asml e);
    void visit(LE_asml e);
    void visit(If_asml e);
    void visit(Let_asml e);
    void visit(Var_asml e);
    void visit(LetRec_asml e);
    void visit(App_asml e);

    void visit(Array_asml e);
    void visit(Get_asml e);
    void visit(Put_asml e);
    void visit(AppC_asml e);
    void visit(Let_memory_load_asml e);
    void visit(Let_memory_store_asml e);
    void visit(Let_memory_alloc_asml e);
}


