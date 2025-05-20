package diagram;

import java.util.ArrayList;
import java.util.List;

public class InterfaceInfo {
    private String name;
    private List<Method> methods = new ArrayList<>();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Method> getMethods() { return methods; }
}
