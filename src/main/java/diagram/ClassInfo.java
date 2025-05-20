package diagram;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private String name;
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Field> getFields() { return fields; }
    public List<Method> getMethods() { return methods; }
}
