package K_Normal_Expression;

public interface KVisitor {

    void visit(KUnit e);
    void visit(KBool e);
    void visit(KInt e);
    void visit(KFloat e);
    void visit(KNot e);
    void visit(KNeg e);
    void visit(KAdd e);
    void visit(KSub e);
    void visit(KFNeg e);
    void visit(KFAdd e);
    void visit(KFSub e);
    void visit(KFMul e);
    void visit(KFDiv e);
    void visit(KEq e);
    void visit(KLE e);
    void visit(KIf e);
    void visit(KLet e);
    void visit(KVar e);
    void visit(KLetRec e);
    void visit(KApp e);
    void visit(KTuple e);
    void visit(KLetTuple e);
    void visit(KArray e);
    void visit(KGet e);
    void visit(KPut e);
}


