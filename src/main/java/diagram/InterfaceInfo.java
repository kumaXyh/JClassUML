package diagram;

import java.util.ArrayList;
import java.util.List;

public class InterfaceInfo extends TypeInfo{
    private List<String> extendsInterfaces=new ArrayList<>();

    // Getters and Setters
    public void addExtendsInterface(String interfaceName){
        extendsInterfaces.add(interfaceName);
    }
    public List<String> getExtendsInterfaces(){
        return extendsInterfaces;
    }
}
