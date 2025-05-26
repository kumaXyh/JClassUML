package command;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

import diagram.*;

public class QueryCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts,ClassDiagram diagram, Deque<Runnable> undoStack){
        if(parts.length<3)return "Invalid query command";

        switch (parts[1]) {
            case "-c": return queryClass(parts,diagram);
            case "-i": return queryInterface(parts, diagram);
            case "-e": return queryEnum(parts, diagram);
            default: return "Unknown query operation";
        }
    }

    private String queryClass(String[] parts, ClassDiagram diagram){
        String className=parts[2];
        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null)return "Class not found";

        boolean hideFields = Arrays.asList(parts).contains("--hide=field");
        boolean hideMethods = Arrays.asList(parts).contains("--hide=method");

        StringBuilder sb=new StringBuilder();
        sb.append(classInfo.isAbstract()?"abstract ":"")
                .append("class ").append(classInfo.getName())
                .append(classInfo.getTypeParameters()!=null ? classInfo.getTypeParameters() : "")
                .append(" {\n");
        if(!hideFields){
            classInfo.getFields().stream().sorted(Comparator.comparingInt(Field::getAccessModifierWeight))
                    .forEach(f->sb.append("    ").append(f.toUMLString()).append("\n"));
        }
        if(!hideMethods){
            classInfo.getMethods().stream().sorted(Comparator.comparingInt(Method::getAccessModifierWeight))
                    .forEach(m->sb.append("    ").append(m.toUMLString()).append("\n"));
        }
        sb.append("}\n");

        return sb.toString();
    }

    private String queryInterface(String[] parts, ClassDiagram diagram){
        String interfaceName = parts[2];
        InterfaceInfo interfaceInfo = findInterface(interfaceName, diagram);
        if (interfaceInfo == null) return "Interface not found";

        boolean hideMethods = Arrays.asList(parts).contains("--hide=method");

        StringBuilder sb = new StringBuilder();
        sb.append("interface ").append(interfaceInfo.getName()).append(" {\n");
        
        if(!hideMethods){
            for(Method method : interfaceInfo.getMethods()){
                sb.append("    ").append(method.toUMLString()).append("\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    private String queryEnum(String[] parts, ClassDiagram diagram){
        String enumName = parts[2];
        EnumInfo enumInfo = findEnum(enumName, diagram);
        if (enumInfo == null) return "Enum not found";

        boolean hideConstants = Arrays.asList(parts).contains("--hide=constant");
        boolean hideFields = Arrays.asList(parts).contains("--hide=field");
        boolean hideMethods = Arrays.asList(parts).contains("--hide=method");

        StringBuilder sb = new StringBuilder();
        sb.append("enum ").append(enumInfo.getName()).append(" {\n");

        if(!hideConstants){
            enumInfo.getConstants().forEach(c->sb.append("    ").append(c).append("\n"));
        }
        if(!hideFields){
            enumInfo.getFields().stream().sorted(Comparator.comparingInt(Field::getAccessModifierWeight))
                    .forEach(f->sb.append("    ").append(f.toUMLString()).append("\n"));
        }
        if(!hideMethods){
            enumInfo.getMethods().stream().sorted(Comparator.comparingInt(Method::getAccessModifierWeight))
                    .forEach(m->sb.append("    ").append(m.toUMLString()).append("\n"));
        }
        sb.append("}\n");

        return sb.toString();
    }
}
