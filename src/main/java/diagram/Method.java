package diagram;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String accessModifier;
    private boolean isStatic;
    private String name;
    private List<Parameter> parameters = new ArrayList<>();
    private String returnType;

    public String toUMLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(accessModifier).append(isStatic ? " {static} " : " ").append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            Parameter p = parameters.get(i);
            sb.append(p.getName()).append(": ").append(p.getType());
            if (i < parameters.size() - 1) sb.append(", ");
        }
        sb.append(")");
        sb.append(": ").append(returnType);
        return sb.toString();
    }

    // Getters and Setters
    public String getAccessModifier() { return accessModifier; }
    public void setAccessModifier(String accessModifier) { this.accessModifier = accessModifier; }
    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Parameter> getParameters() { return parameters; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
}
