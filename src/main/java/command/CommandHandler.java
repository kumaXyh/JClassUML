package command;

import diagram.*;
import java.util.Deque;

public class CommandHandler {
    protected String handle(String[] parts,ClassDiagram diagram, Deque<Runnable> undoStack){
        return "";
    }

    protected ClassInfo findClass(String name, ClassDiagram diagram){
        for(ClassInfo classInfo:diagram.getClasses()){
            if(classInfo.getName().equals(name))return classInfo;
        }
        return null;
    }

    protected InterfaceInfo findInterface(String name, ClassDiagram diagram){
        for(InterfaceInfo interfaceInfo:diagram.getInterfaces()){
            if(interfaceInfo.getName().equals(name))return interfaceInfo;
        }
        return null;
    }

    protected EnumInfo findEnum(String name, ClassDiagram diagram) {
        for (EnumInfo enumInfo : diagram.getEnums()) {
            if (enumInfo.getName().equals(name)) return enumInfo;
        }
        return null;
    }

    protected String getOptionValue(String[] parts, String option){
        for(int i=0;i<parts.length-1;i++){
            if(parts[i].equals(option)){
                return parts[i+1];
            }
        }
        return null;
    }

    protected String getValueAfterEqual(String[] parts, String option, String defaultValue){
        for(String part:parts){
            if(part.startsWith(option)){
                return part.substring(option.length());
            }
        }
        return defaultValue;
    }

    protected boolean commandContains(String[] parts, String option) {
        for (String part : parts) {
            if (part.equals(option)) return true;
        }
        return false;
    }

    protected Field findField(ClassInfo classInfo, String fieldName){
        for(Field f:classInfo.getFields()){
            if(f.getName().equals(fieldName)){
                return f;
            }
        }
        return null;
    }

    protected Method findMethod(ClassInfo classInfo, String methodName){
        for(Method m:classInfo.getMethods()){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }
}
