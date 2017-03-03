package Asml;

import Expression_asml.*;

public interface ObjVisitor_asml<E> {
    E visit(Unit_asml e);
    E visit(Bool_asml e);
    E visit(Int_asml e);
    E visit(Float_asml e);
    E visit(Not_asml e);
    E visit(Neg_asml e);
    E visit(Add_asml e);
    E visit(Sub_asml e);
    E visit(FNeg_asml e);
    E visit(FAdd_asml e);
    E visit(FSub_asml e);
    E visit(FMul_asml e);
    E visit(FDiv_asml e);
    E visit(Eq_asml e);
    E visit(LE_asml e);
    E visit(If_asml e);
    E visit(Let_asml e);
    E visit(Var_asml e);
    E visit(LetRec_asml e);
    E visit(App_asml e);
    E visit(Array_asml e);
    E visit(Get_asml e);
    E visit(Put_asml e);
    E visit(AppC_asml e);
    E visit(Let_memory_load_asml e);
    E visit(Let_memory_store_asml e);
    E visit(Let_memory_alloc_asml e);
}


