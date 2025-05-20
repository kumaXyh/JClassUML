package diagram;

import java.util.ArrayList;
import java.util.List;

public class Method extends Member{
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
    public List<Parameter> getParameters() { return parameters; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
}
