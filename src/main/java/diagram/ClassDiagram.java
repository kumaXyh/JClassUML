package diagram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ClassDiagram {

    private final List<ClassInfo> classes = new ArrayList<>();
    private final List<InterfaceInfo> interfaces=new ArrayList<>();
    private final List<EnumInfo> enums=new ArrayList<>();

    public void addClass(ClassInfo classInfo){
        classes.add(classInfo);
    }
    public void addInterface(InterfaceInfo interfaceInfo){
        interfaces.add(interfaceInfo);
    }
    public void addEnum(EnumInfo enumInfo){
        enums.add(enumInfo);
    }

    public String generateUML() {
        StringBuilder sb=new StringBuilder();
        sb.append("@startuml\n");

        //输出类和接口的定义(按访问修饰符排序字段)
        //类
        for(ClassInfo classInfo : classes){
            sb.append(classInfo.isAbstract()?"abstract ":"")
                .append("class ").append(classInfo.getName())
                .append(classInfo.getTypeParameters()!=null ? classInfo.getTypeParameters() : "")
                .append(" {\n");
            
            classInfo.getFields().stream().sorted(Comparator.comparingInt(Field::getAccessModifierWeight))
                    .forEach(f->sb.append("    ").append(f.toUMLString()).append("\n"));
            classInfo.getMethods().stream().sorted(Comparator.comparingInt(Method::getAccessModifierWeight))
                    .forEach(m->sb.append("    ").append(m.toUMLString()).append("\n"));
            
            sb.append("}\n");
        }
        //枚举类
        for(EnumInfo enumInfo : enums){
            sb.append("enum ").append(enumInfo.getName()).append(" {\n");

            enumInfo.getConstants().forEach(c->sb.append("    ").append(c).append("\n"));

            enumInfo.getFields().stream().sorted(Comparator.comparingInt(Field::getAccessModifierWeight))
                    .forEach(f->sb.append("    ").append(f.toUMLString()).append("\n"));

            enumInfo.getMethods().stream().sorted(Comparator.comparingInt(Method::getAccessModifierWeight))
                    .forEach(m->sb.append("    ").append(m.toUMLString()).append("\n"));
            
            sb.append("}\n");
        }
        //接口
        for(InterfaceInfo interfaceInfo : interfaces){
            sb.append("interface ").append(interfaceInfo.getName()).append(" {\n");
            for(Method method : interfaceInfo.getMethods()){
                sb.append("    ").append(method.toUMLString()).append("\n");
            }
            sb.append("}\n");
        }

        //输出继承关系
        for(ClassInfo classInfo : classes){
            if(classInfo.getExtendsClass()!=null){
                sb.append(classInfo.getExtendsClass()).append(" <|-- ")
                    .append(classInfo.getName()).append("\n");
            }
        }
        for(InterfaceInfo interfaceInfo : interfaces){
            for(String parentInterface: interfaceInfo.getExtendsInterfaces()){
                sb.append(parentInterface).append(" <|-- ")
                    .append(interfaceInfo.getName()).append("\n");
            }
        }
        //输出实现关系
        for(ClassInfo classInfo : classes){
            for(String interfaceName: classInfo.getImplementsInterfaces()){
                sb.append(interfaceName).append(" <|.. ")
                    .append(classInfo.getName()).append("\n");
            }
        }
        //分析关联关系
        AssociationAnalyzer associationAnalyzer=new AssociationAnalyzer();
        associationAnalyzer.analyze(classes,enums);
        //分析依赖关系
        DependencyAnalyzer dependencyAnalyzer=new DependencyAnalyzer();
        dependencyAnalyzer.analyze(classes, interfaces, enums);
        //移除重复依赖关系
        Set<String> dependencies=new HashSet<>(dependencyAnalyzer.getRelations());
        dependencies.removeAll(associationAnalyzer.getRelations());
        dependencyAnalyzer.getRelations().clear();
        dependencies.forEach(s->dependencyAnalyzer.getRelations().add(s));
        //输出关联、依赖关系
        associationAnalyzer.getRelations().forEach(relation->{
            String[] parts=relation.split(" ");
            sb.append(parts[0]+" <-- "+parts[1]+"\n");
        });
        dependencyAnalyzer.getRelations().forEach(relation->{
            String[] parts=relation.split(" ");
            sb.append(parts[0]+" <.. "+parts[1]+"\n");
        });
        sb.append("@enduml");
        return sb.toString();
    }

    /**
     * 你应当在迭代二中实现这个方法
     * @return 返回代码中的“坏味道”
     */
    public List<String> getCodeSmells() {
        List<String> smells=new ArrayList<>();
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


    /**
     * 你应当在迭代三中实现这个方法
     * @param configFile 配置文件路径
     */
    public void loadConfig(String configFile) {
        return;
    }
}