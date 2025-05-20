package diagram;

public class Member {
    protected String accessModifier;
    protected boolean isStatic;
    protected String name;

    //访问修饰符权重计算
    public int getAccessModifierWeight(){
        switch (this.accessModifier) {
            case "-": return 0;
            case "#": return 1;
            case "~": return 2;
            case "+": return 3;
            default: return 2;
        }
    }
    public String getAccessModifier(){return accessModifier;}
    public void setAccessModifier(String accessModifier){this.accessModifier=accessModifier;}
    public boolean isStatic(){return isStatic;}
    public void setStatic(boolean isStatic){this.isStatic=isStatic;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
}
