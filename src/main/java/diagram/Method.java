package diagram;

import java.util.ArrayList;
import java.util.List;

public class Method extends Member{
    private List<Parameter> parameters = new ArrayList<>();
    private String returnType;
    private boolean isAbstract;

    public String toUMLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(accessModifier).append(isStatic ? " {static} " : "")
            .append(isAbstract?" {abstract} ":"").append((isAbstract||isStatic)?"":" ").append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            Parameter p = parameters.get(i);
            sb.append(p.getName()).append(": ").append(p.getType().replace(",", ", "));
            if (i < parameters.size() - 1) sb.append(", ");
        }
        sb.append(")");
        sb.append(": ").append(returnType.replace(",", ", "));
        return sb.toString();
    }

    // Getters and Setters
    public List<Parameter> getParameters() { return parameters; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public boolean isAbstract(){return isAbstract;}
    public void setAbstract(boolean isAbstract){this.isAbstract=isAbstract;}
}
