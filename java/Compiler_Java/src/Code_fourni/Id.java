package Code_fourni;
public class Id {
    private String id;
    public Id(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return id;
    }
    static int x = -1;
    public static Id gen() {
        x++;
        return new Id("?v" + x);
    }
    public static Id genf() {
        x++;
        return new Id("?f" + x);
    }
    public static Id genARM(){
        x++;
        return new Id("arm" + x);
    }
	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Id){
			return id.equals(((Id) o).getId());
		}else{
			return false;
		}	
	}

}
