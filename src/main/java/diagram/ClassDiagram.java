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

    private final RelationshipAnalyzer relationshipAnalyzer=new RelationshipAnalyzer();

    private final InheritanceAnalyzer inheritanceAnalyzer=new InheritanceAnalyzer();
    private final ClassAnalyzer classAnalyzer=new ClassAnalyzer();
    private final CircularAnalyzer circularAnalyzer=new CircularAnalyzer();

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

        if(!relationshipAnalyzer.isAnalyzed()){
            relationshipAnalyzer.analyzeRelations(classes, interfaces, enums);
        }
        sb.append(relationshipAnalyzer.toUMLString());
        sb.append("@enduml");
        return sb.toString();
    }

    /**
     * 你应当在迭代二中实现这个方法
     * @return 返回代码中的“坏味道”
     */
    public List<String> getCodeSmells() {
        List<String> smells=new ArrayList<>();
        //类分析
        classAnalyzer.setClasses(classes);
        classAnalyzer.analyze(smells);
        //继承树分析
        inheritanceAnalyzer.buildTree(classes);
        inheritanceAnalyzer.analyze(smells);
        //循环依赖分析
        if(!relationshipAnalyzer.isAnalyzed()){
            relationshipAnalyzer.analyzeRelations(classes, interfaces, enums);
        }
        if(circularAnalyzer.analyze(relationshipAnalyzer.getRelations())!=""){
            smells.add(circularAnalyzer.analyze(relationshipAnalyzer.getRelations()));
        }
        return smells;
    }

    /**
     * 你应当在迭代三中实现这个方法
     * @param configFile 配置文件路径
     */
    public void loadConfig(String configFile) {
        return;
    }
}