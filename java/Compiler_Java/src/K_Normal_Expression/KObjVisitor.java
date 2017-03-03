package K_Normal_Expression;

public interface KObjVisitor<E> {
    E visit(KUnit e);
    E visit(KBool e);
    E visit(KInt e);
    E visit(KFloat e);
    E visit(KNot e);
    E visit(KNeg e);
    E visit(KAdd e);
    E visit(KSub e);
    E visit(KFNeg e);
    E visit(KFAdd e);
    E visit(KFSub e);
    E visit(KFMul e);
    E visit(KFDiv e);
    E visit(KEq e);
    E visit(KLE e);
    E visit(KIf e);
    E visit(KLet e);
    E visit(KVar e);
    E visit(KLetRec e);
    E visit(KApp e);
    E visit(KTuple e);
    E visit(KLetTuple e);
    E visit(KArray e);
    E visit(KGet e);
    E visit(KPut e);
}


