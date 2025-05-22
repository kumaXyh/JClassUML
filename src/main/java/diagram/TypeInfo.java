package diagram;

import java.util.List;
import java.util.ArrayList;

public class TypeInfo {
    protected String name;
    protected List<Method> methods=new ArrayList<>();
    
    //Getters and Setters
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public List<Method> getMethods(){return methods;}
}
