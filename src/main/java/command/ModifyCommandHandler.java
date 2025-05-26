package command;

import java.util.Deque;

import diagram.*;

public class ModifyCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack) {
        if (parts.length < 4) return "Invalid command";
        
        switch (parts[1]) {
            case "field": return modifyField(parts, diagram, undoStack);
            case "function": return modifyMethod(parts, diagram, undoStack);
            default: return "Unknown modify operation";
        }
    }

    private String modifyField(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        String fieldName=getOptionValue(parts, "-n");

        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null)return "Class not found";

        Field field=findField(classInfo, fieldName);
        if(field==null)return "Field not found";

        Field original=new Field();
        original.setName(field.getName());
        original.setType(field.getType());
        original.setAccessModifier(field.getAccessModifier());
        original.setStatic(field.isStatic());

        String newName=getValueAfterEqual(parts, "--new-name=", null);
        if(newName!=null) field.setName(newName);

        String newType=getValueAfterEqual(parts, "--new-type=", null);
        if(newType!=null) field.setType(newType);

        String newAccess=getValueAfterEqual(parts, "--new-access=", null);
        if(newAccess!=null) field.setAccessModifier(newAccess);

        field.setStatic(commandContains(parts, "--static"));

        undoStack.push(()->{
            field.setName(original.getName());
            field.setType(original.getType());
            field.setAccessModifier(original.getAccessModifier());
            field.setStatic(original.isStatic());
        });

        return "Modified field "+fieldName;
    } 

    private String modifyMethod(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        String className=parts[2];
        String methodName=getOptionValue(parts, "-n");

        ClassInfo classInfo=findClass(className, diagram);
        if(classInfo==null) return "Class not found";

        Method method=findMethod(classInfo, methodName);
        if(method==null)return "Method not found";

        Method original=new Method();
        original.setName(method.getName());
        original.setReturnType(method.getReturnType());
        original.setAccessModifier(method.getAccessModifier());
        original.setStatic(method.isStatic());
        original.setAbstract(method.isAbstract());
        original.getParameters().addAll(method.getParameters());

        String newName=getValueAfterEqual(parts, "--new-name=", null);
        if(newName!=null)method.setName(newName);

        String newReturnType=getValueAfterEqual(parts, "--new-return=", null);
        if(newReturnType!=null)method.setReturnType(newReturnType);

        String newAccess=getValueAfterEqual(parts, "--new-access=", null);
        if(newAccess!=null) method.setAccessModifier(newAccess);

        method.setStatic(commandContains(parts, "--static"));

        method.setAbstract(commandContains(parts, "--abstract"));

        String newParams=getValueAfterEqual(parts, "--new-params=", null);
        if(newParams!=null){
            method.getParameters().clear();
            for(String param:newParams.split(",")){
                String[] nameType=param.split(":");
                Parameter p=new Parameter();
                p.setName(nameType[0]);
                p.setType(nameType[1]);
                method.getParameters().add(p);
            }
        }

        undoStack.push(()->{
            method.setName(original.getName());
            method.setReturnType(original.getReturnType());
            method.setAccessModifier(original.getAccessModifier());
            method.setStatic(original.isStatic());
            method.setAbstract(original.isAbstract());
            method.getParameters().clear();
            method.getParameters().addAll(original.getParameters());
        });

        return "Modified method "+methodName;
    } 
}
