package command;

import diagram.*;
import java.util.Deque;

public class DeleteCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        if(parts.length<3)return "Invalid command";

        switch (parts[1]) {
            case "-c": return deleteClass(parts,diagram,undoStack);
            case "-i": return deleteInterface(parts, diagram, undoStack);
            case "-e": return deleteEnum(parts, diagram, undoStack);
            case "field": return deleteFeild(parts, diagram, undoStack);
            case "function": return deleteMethod(parts, diagram, undoStack);
            default: return "Unknown delete operation";
        }
    }

    private String deleteClass(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null)return "Class not found";

        diagram.getClasses().remove(classInfo);
        diagram.getRelationshipAnalyzer().setAnalyzed(false);
        undoStack.push(()->{
            diagram.addClass(classInfo);
            diagram.getRelationshipAnalyzer().setAnalyzed(false);
        });

        return "Deleted class "+className;
    }

    private String deleteInterface(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String interfaceName = parts[2];
        InterfaceInfo interfaceInfo = findInterface(interfaceName, diagram);
        if (interfaceInfo == null) return "Interface not found";

        diagram.getInterfaces().remove(interfaceInfo);
        diagram.getRelationshipAnalyzer().setAnalyzed(false);
        undoStack.push(() -> {
            diagram.addInterface(interfaceInfo);
            diagram.getRelationshipAnalyzer().setAnalyzed(false);
        });

        return "Deleted interface " + interfaceName;
    }

    private String deleteEnum(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String enumName = parts[2];
        EnumInfo enumInfo = findEnum(enumName, diagram);
        if (enumInfo == null) return "Enum not found";

        diagram.getEnums().remove(enumInfo);
        diagram.getRelationshipAnalyzer().setAnalyzed(false);
        undoStack.push(() -> {
            diagram.addEnum(enumInfo);
            diagram.getRelationshipAnalyzer().setAnalyzed(false);
        });

        return "Deleted enum " + enumName;
    }

    private String deleteFeild(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className = parts[2];
        String fieldName = getOptionValue(parts, "-n");

        ClassInfo classInfo = findClass(className, diagram);
        if (classInfo == null) return "Class not found";

        Field field = findField(classInfo, fieldName);
        if (field == null) return "Field not found";

        classInfo.getFields().remove(field);
        undoStack.push(() -> classInfo.getFields().add(field));

        return "Deleted field " + fieldName + " from " + className;
    }

    private String deleteMethod(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className = parts[2];
        String methodName = getOptionValue(parts, "-n");

        ClassInfo classInfo = findClass(className, diagram);
        if (classInfo == null) return "Class not found";

        Method method = findMethod(classInfo, methodName);
        if(method==null)return "Method not found";

        classInfo.getMethods().remove(method);
        undoStack.push(() -> classInfo.getMethods().add(method));

        return "Deleted method " + methodName + " from " + className;
    }
}
