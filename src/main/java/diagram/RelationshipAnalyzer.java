package diagram;

import java.util.*;

public class RelationshipAnalyzer {
    private final List<String[]> relations=new ArrayList<>();
    private boolean isAnalyzed=false;

    public List<String[]> getRelations(){
        return new ArrayList<>(relations);
    }
    public boolean isAnalyzed(){return isAnalyzed;}

    public void analyzeRelations(List<ClassInfo> classes,List<InterfaceInfo> interfaces,List<EnumInfo> enums){
        relations.clear();

        //分析继承关系
        analyzeInheritance(classes, interfaces);
        //分析实现关系
        analyzeImplementations(classes);
        //分析关联/依赖关系
        analyzeAssociationsAndDependencies(classes,interfaces,enums);

        isAnalyzed=true;
    }

    public String toUMLString(){
        String relationStr=new String();
        for(String[] relation:relations){
            relationStr+=relation[0]+relation[2]+relation[1]+"\n";
        }
        return relationStr;
    }

    private void analyzeInheritance(List<ClassInfo> classes,List<InterfaceInfo> interfaces){
        for(ClassInfo classInfo : classes){
            if(classInfo.getExtendsClass()!=null){
                relations.add(new String[]{classInfo.getExtendsClass(),classInfo.getName()," <|-- "});
            }
        }
        for(InterfaceInfo interfaceInfo : interfaces){
            for (String parent : interfaceInfo.getExtendsInterfaces()) {
                relations.add(new String[]{parent, interfaceInfo.getName()," <|-- "});
            }
        }
    }

    private void analyzeImplementations(List<ClassInfo> classes){
        for(ClassInfo classInfo : classes){
            for(String interfaceName:classInfo.getImplementsInterfaces()){
                relations.add(new String[]{interfaceName, classInfo.getName()," <|.. "});
            }
        }
    }

    private void analyzeAssociationsAndDependencies(List<ClassInfo> classes,List<InterfaceInfo> interfaces,List<EnumInfo> enums){
        //分析关联关系
        AssociationAnalyzer associationAnalyzer=new AssociationAnalyzer();
        associationAnalyzer.analyze(classes, enums);
        //分析依赖关系
        DependencyAnalyzer dependencyAnalyzer=new DependencyAnalyzer();
        dependencyAnalyzer.analyze(classes, interfaces, enums);
        //移除重复依赖关系
        Set<String> dependencies=new HashSet<>(dependencyAnalyzer.getRelations());
        dependencies.removeAll(associationAnalyzer.getRelations());
        dependencyAnalyzer.getRelations().clear();
        dependencies.forEach(s->dependencyAnalyzer.getRelations().add(s));

        associationAnalyzer.getRelations().forEach(relation->{
            String[] parts=relation.split(" ");
            relations.add(new String[]{parts[0],parts[1]," <-- "});
        });
        dependencyAnalyzer.getRelations().forEach(relation->{
            String[] parts=relation.split(" ");
            relations.add(new String[]{parts[0],parts[1]," <.. "});
        });
    }
}
