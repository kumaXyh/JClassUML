package diagram;

import java.util.ArrayList;
import java.util.List;

public class EnumInfo {
    private String name;
    private List<String> constants=new ArrayList<>();
    private List<Field> fields=new ArrayList<>();
    private List<Method> methods=new ArrayList<>();

    //Getters and Setters
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public List<String> getConstants(){return constants;}
    public List<Field> getFields(){return fields;}
    public List<Method> getMethods(){return methods;}
}
