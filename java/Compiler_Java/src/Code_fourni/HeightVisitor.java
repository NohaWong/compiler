package Code_fourni;
import Expression.*;
import Expression.Float;


public class HeightVisitor implements ObjVisitor<Integer> {

    public Integer visit(Unit e) {
        // This tree is of height 0
        return 0;
    }

    public Integer visit(Bool e) {
        return 0;
    }

    public Integer visit(Int e) {
        return 0;
    }

    public Integer visit(Float e) { 
        return 0;
    }

    public Integer visit(Not e) {
        return e.getE().accept(this) + 1;
    }

    public Integer visit(Neg e) {
        return e.getE().accept(this) + 1;
    }

    public Integer visit(Add e) {
        int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(Sub e) {
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
   }

    public Integer visit(FNeg e){
        return e.getE().accept(this) + 1;
    }

    public Integer visit(FAdd e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(FSub e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(FMul e) {
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
     }

    public Integer visit(FDiv e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(Eq e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(LE e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(If e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        int res3 = e.getE3().accept(this);
        return Math.max(res1, Math.max(res2, res3)) + 1 ;
    }

    public Integer visit(Let e) {
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(Var e){
        return 0;
    }

    public Integer visit(LetRec e){
        int res1 = e.getE().accept(this);
        int res2 = e.getFd().getE().accept(this);
        return Math.max(res1, res2) + 1 ;
    }

    public Integer visit(App e){
        int res1 = e.getE().accept(this);
        for (Exp exp : e.getEs()) {
            res1 = Math.max(res1, exp.accept(this));
        }
        return res1 + 1;
    }

    public Integer visit(Tuple e){
        int res1 = 0;
        for (Exp exp : e.getEs()) {
            int res = exp.accept(this);
            res1 = Math.max(res, res1);
        }
        return res1 + 1;
    }

    public Integer visit(LetTuple e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Array e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Get e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Put e){
    	int res1 = e.getE1().accept(this);
        int res2 = e.getE2().accept(this);
        int res3 = e.getE3().accept(this);
        return Math.max(res1, Math.max(res2, res3)) + 1 ;
    }
}


