package diagram;

import java.util.ArrayList;
import java.util.List;

public class ClassDiagram {

    private final List<ClassInfo> classes = new ArrayList<>();
    private final List<InterfaceInfo> interfaces=new ArrayList<>();

    public void addClass(ClassInfo classInfo){
        classes.add(classInfo);
    }

    public void addInterface(InterfaceInfo interfaceInfo){
        interfaces.add(interfaceInfo);
    }

    public String generateUML() {
        StringBuilder sb=new StringBuilder();
        sb.append("@startuml\n");

        //输出类和接口的定义
        for(ClassInfo classInfo : classes){
            sb.append("class ").append(classInfo.getName()).append(" {\n");
            for(Field field : classInfo.getFields()){
                sb.append("    ").append(field.toUMLString()).append("\n");
            }
            for(Method method : classInfo.getMethods()){
                sb.append("    ").append(method.toUMLString()).append("\n");
            }
            sb.append("}\n");
        }
        for(InterfaceInfo interfaceInfo : interfaces){
            sb.append("interface ").append(interfaceInfo.getName()).append(" {\n");
            for(Method method : interfaceInfo.getMethods()){
                sb.append("    ").append(method.toUMLString()).append("\n");
            }
            sb.append("}\n");
        }

        //输出继承和实现关系
        for(ClassInfo classInfo : classes){
            if(classInfo.getExtendsClass()!=null){
                sb.append(classInfo.getExtendsClass()).append(" <|-- ")
                    .append(classInfo.getName()).append("\n");
            }

            for(String interfaceName: classInfo.getImplementsInterfaces()){
                sb.append(interfaceName).append(" <|.. ")
                    .append(classInfo.getName()).append("\n");
            }
        }
        //接口继承关系
        for(InterfaceInfo interfaceInfo : interfaces){
            for(String parentInterface: interfaceInfo.getExtendsInterfaces()){
                sb.append(parentInterface).append(" <|-- ")
                    .append(interfaceInfo.getName()).append("\n");
            }
        }
        sb.append("@enduml");
        return sb.toString();
    }

    /**
     * 你应当在迭代二中实现这个方法
     * @return 返回代码中的“坏味道”
     */
    public List<String> getCodeSmells() {
        return null;
    }

    /**
     * 你应当在迭代三中实现这个方法
     * @param configFile 配置文件路径
     */
    public void loadConfig(String configFile) {
        return;
    }
}