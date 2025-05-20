package diagram;

public class Field {
    private String accessModifier;
    private boolean isStatic;
    private String name;
    private String type;

    public String toUMLString() {
        return accessModifier + (isStatic ? " {static} " : " ") + name + ": " + type;
    }

    // Getters and Setters
    public String getAccessModifier() { return accessModifier; }
    public void setAccessModifier(String accessModifier) { this.accessModifier = accessModifier; }
    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
