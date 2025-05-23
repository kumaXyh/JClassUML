package diagram;

import java.util.List;

public class ClassAnalyzer {
    private List<ClassInfo> classes;

    public void setClasses(List<ClassInfo> classes){
        this.classes=classes;
    }
    public List<String> analyze(List<String> smells){
        for(ClassInfo classInfo:classes){
            if(classInfo.isAbstract())continue;

            //God Class
            if(isGodClass(classInfo)){
                smells.add("God Class: "+classInfo.getName());
                continue;
            }
            //Lazy Class
            if(isLazyClass(classInfo)){
                smells.add("Lazy Class: "+classInfo.getName());
                continue;
            }
            //Data Class
            if(isDataClass(classInfo)){
                smells.add("Data Class: "+classInfo.getName());
            }
        }
        return smells;
    }

    //判断是否为God Class
    private boolean isGodClass(ClassInfo classInfo){
        return classInfo.getFields().size()>=20||classInfo.getMethods().size()>=20;
    }
    //判断是否为Lazy Class
    private boolean isLazyClass(ClassInfo classInfo){
        long nonConstructorMethods = classInfo.getMethods().stream()
        .filter(m -> !isConstructorMethod(m, classInfo.getName())).count();
        return classInfo.getFields().size()==0||nonConstructorMethods<=1;
    }
    // 判断是否为构造方法
    private boolean isConstructorMethod(Method method, String className) {
        return method.getName().equals(className);
    }
    //判断是否为Data Class
    private boolean isDataClass(ClassInfo classInfo){
        if(classInfo.getMethods().isEmpty())return false;

        for(Method method : classInfo.getMethods()){
            String methodName=method.getName();
            //不是Getters和Setters且不是构造函数
            if(!methodName.startsWith("get")&&!methodName.startsWith("set")
                    &&!methodName.equals(classInfo.getName())){
                return false;
            }
        }
        return true;
    }
}
