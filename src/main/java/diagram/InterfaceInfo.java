package diagram;

import java.util.ArrayList;
import java.util.List;

public class InterfaceInfo {
    private String name;
    private List<String> extendsInterfaces=new ArrayList<>();
    private List<Method> methods = new ArrayList<>();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Method> getMethods() { return methods; }

    public void addExtendsInterface(String interfaceName){
        extendsInterfaces.add(interfaceName);
    }
    public List<String> getExtendsInterfaces(){
        return extendsInterfaces;
    }
}
