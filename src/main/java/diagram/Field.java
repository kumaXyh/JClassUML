package diagram;

public class Field extends Member{
    private String type;

    public String toUMLString() {
        //逗号后加空格
        return accessModifier + (isStatic ? " {static} " : " ") + name + ": " + type;
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
