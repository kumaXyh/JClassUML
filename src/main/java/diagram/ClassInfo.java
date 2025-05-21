package diagram;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private String name;
    private String extendsClass;
    private boolean isAbstract;
    private List<String> implementsInterfaces=new ArrayList<>();
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Field> getFields() { return fields; }
    public List<Method> getMethods() { return methods; }
    public boolean isAbstract(){return isAbstract;}
    public void setAbstract(boolean isAbstract){this.isAbstract=isAbstract;}
    
    public void setExtendsClass(String extendsClass){
        this.extendsClass=extendsClass;
    }
    public String getExtendsClass(){
        return extendsClass;
    }
    public void addImplementsInterface(String interfaceName){
        implementsInterfaces.add(interfaceName);
    }
    public List<String> getImplementsInterfaces(){
        return implementsInterfaces;
    }
}
