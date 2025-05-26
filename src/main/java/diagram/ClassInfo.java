package diagram;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo extends TypeInfo{
    private String extendsClass;//关系
    private boolean isAbstract;
    private String typeParameters;//泛型
    private List<String> implementsInterfaces=new ArrayList<>();
    private List<Field> fields = new ArrayList<>();
    private List<String> subClasses=new ArrayList<>();

    // Getters and Setters
    public List<Field> getFields() { return fields; }
    public boolean isAbstract(){return isAbstract;}
    public void setAbstract(boolean isAbstract){this.isAbstract=isAbstract;}
    public String getTypeParameters(){return typeParameters;}
    public void setTypeParameters(String typeParameters){
        this.typeParameters=typeParameters.replace("[", "<").replace("]", ">");
    }
    
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
    public List<String> getSubClasses(){
        return subClasses;
    }
    public void addSubClass(String subClass){
        subClasses.add(subClass);
    }
}
