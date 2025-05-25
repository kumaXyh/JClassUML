package command;

import java.util.Arrays;
import java.util.Deque;

import diagram.*;

public class AddCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        if(parts.length<3)return "Invalid command";

        switch (parts[1]) {
            case "-c": return addClass(parts, diagram, undoStack);
            case "-i": return addInterface(parts, diagram, undoStack);
            case "-e": return addEnum(parts, diagram, undoStack);
            case "field": return addField(parts, diagram, undoStack);
            case "function": return addMethod(parts, diagram, undoStack);
            default: return "Unknown add operation";
        }
    }

    private String addClass(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        boolean isAbstract=commandContains(parts, "--abstract");

        ClassInfo classInfo=new ClassInfo();
        classInfo.setName(className);
        classInfo.setAbstract(isAbstract);

        diagram.addClass(classInfo);
        undoStack.push(()->diagram.getClasses().remove(classInfo));
        
        return "Added class "+className;
    }

    private String addInterface(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String interfaceName=parts[2];
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        interfaceInfo.setName(interfaceName);

        diagram.addInterface(interfaceInfo);
        undoStack.push(()->diagram.getInterfaces().remove(interfaceInfo));

        return "Added interface "+interfaceName;
    }

    private String addEnum(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String enumName=parts[2];
        EnumInfo enumInfo=new EnumInfo();
        enumInfo.setName(enumName);

        String[] values=getValueAfterEqual(parts, "--values=", null).split(",");
        for(String value:values){
            enumInfo.getConstants().add(value);
        }

        diagram.addEnum(enumInfo);
        undoStack.push(()->diagram.getEnums().remove(enumInfo));

        return "Added enum "+enumName;
    }

    private String addField(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        String fieldName=getOptionValue(parts, "-n");
        String fieldType=getOptionValue(parts, "-t");
        String access=getValueAfterEqual(parts, "--access=", "-");
        boolean isStatic=commandContains(parts, "--static");

        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null){return "Class not found";}

        Field field=new Field();
        field.setName(fieldName);
        field.setType(fieldType);
        field.setAccessModifier(access);
        field.setStatic(isStatic);

        classInfo.getFields().add(field);
        undoStack.push(()->classInfo.getFields().remove(field));
        
        return "Added field "+fieldName+" to "+className;
    }

    private String addMethod(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        String methodName=getOptionValue(parts, "-n");
        String returnType=getOptionValue(parts, "-t");
        String access=getValueAfterEqual(parts, "--access=","-");
        boolean isStatic=commandContains(parts, "--static");
        boolean isAbstract=commandContains(parts, "--abstract");

        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null){return "Class not found";}
        
        Method method=new Method();
        method.setName(methodName);
        method.setReturnType(returnType);
        method.setAccessModifier(access);
        method.setStatic(isStatic);
        method.setAbstract(isAbstract);

        String params=getValueAfterEqual(parts, "--params=",null);
        if(params!=null){
            for(String param:params.split(",")){
                String[] nameType=param.split(":");
                Parameter p=new Parameter();
                p.setName(nameType[0]);
                p.setType(nameType[1]);
                method.getParameters().add(p);
            }
        }

        classInfo.getMethods().add(method);
        undoStack.push(()->classInfo.getMethods().remove(method));

        return "Added method "+methodName+" to "+className;
    }
}
