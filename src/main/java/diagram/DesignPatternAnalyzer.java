package diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DesignPatternAnalyzer {
    public List<String> analyze(List<ClassInfo> classes, List<InterfaceInfo> interfaces){
        List<String> results=new ArrayList<>();

        results.addAll(analyzeSinglePatterns(classes));

        results.addAll(analyzeStrategyPatterns(classes, interfaces));

        return results;
    }

    private List<String> analyzeSinglePatterns(List<ClassInfo> classes){
        List<String> results=new ArrayList<>();

        for(ClassInfo classInfo:classes){
            if(isSingleton(classInfo)){
                results.add("Possible Design Patterns: Singleton Pattern");
            }
        }
        return results;
    }

    private boolean isSingleton(ClassInfo classInfo){
        //没有子类继承
        boolean hasNoSubClass=classInfo.getSubClasses().isEmpty();

        //没有公共构造函数且存在私有构造函数
        boolean hasNoPublicConstructor=classInfo.getMethods().stream()
                .noneMatch(m->m.getName().equals(classInfo.getName())&&
                        m.getAccessModifier().equals("+"));
        boolean hasPrivateConstructor=classInfo.getMethods().stream()
                .anyMatch(m->m.getName().equals(classInfo.getName())&&
                        m.getAccessModifier().equals("-"));
        
        //包括静态私有字段，类型为自身类
        boolean hasStaticInstanceField=classInfo.getFields().stream()
                .anyMatch(f->f.isStatic()&&f.getAccessModifier().equals("-")&&
                        f.getType().equals(classInfo.getName()));
        
        //提供静态公有方法获取实例
        boolean hasStaticGetInstanceMethod=classInfo.getMethods().stream()
                .anyMatch(m->m.isStatic() && 
                            m.getAccessModifier().equals("+") &&
                            m.getReturnType().equals(classInfo.getName()));
        
        return hasNoSubClass && hasNoPublicConstructor && hasPrivateConstructor 
                && hasStaticInstanceField && hasStaticGetInstanceMethod;
    }

    private List<String> analyzeStrategyPatterns(List<ClassInfo> classes, List<InterfaceInfo> interfaces) {
        List<String> results = new ArrayList<>();
        
        // 1. 查找可能的策略接口（接口或抽象类）
        List<StrategyComponent> strategyComponents = new ArrayList<>();
        
        // 检查接口
        interfaces.stream()
            .filter(this::isValidStrategyComponent)
            .forEach(i -> strategyComponents.add(new StrategyComponent(i.getName(), true)));
        
        // 检查抽象类
        classes.stream()
            .filter(ClassInfo::isAbstract)
            .filter(this::isValidStrategyComponent)
            .forEach(c -> strategyComponents.add(new StrategyComponent(c.getName(), false)));
        
        // 2. 对每个候选策略组件进行检查
        for (StrategyComponent component : strategyComponents) {
            // 查找实现/继承该组件的具体策略类(≥2个)
            List<ClassInfo> concreteStrategies = classes.stream()
                .filter(c -> !c.isAbstract())
                .filter(c -> component.isInterface ? 
                    c.getImplementsInterfaces().contains(component.name) :
                    c.getExtendsClass() != null && c.getExtendsClass().equals(component.name))
                .collect(Collectors.toList());
            
            if (concreteStrategies.size() >= 2) {
                // 查找与策略组件有关联的上下文类
                boolean hasContextClass = classes.stream()
                    .anyMatch(c -> hasAssociationWithStrategy(c, component.name));
                
                if (hasContextClass) {
                    results.add("Possible Design Patterns: Strategy Pattern");
                }
            }
        }
        
        return results;
    }
    
    // 检查是否是有效的策略组件（接口或抽象类）
    private boolean isValidStrategyComponent(InterfaceInfo interfaceInfo) {
        return isStrategyName(interfaceInfo.getName()) && 
               hasNonConstructorMethods(interfaceInfo.getMethods(), interfaceInfo.getName());
    }
    
    private boolean isValidStrategyComponent(ClassInfo classInfo) {
        return isStrategyName(classInfo.getName()) && 
               hasNonConstructorMethods(classInfo.getMethods(), classInfo.getName());
    }
    
    // 检查名称是否符合策略模式命名
    private boolean isStrategyName(String name) {
        return name.endsWith("Strategy") || 
               name.endsWith("Policy") || 
               name.endsWith("Behavior");
    }
    
    // 检查是否有非构造方法
    private boolean hasNonConstructorMethods(List<Method> methods, String componentName) {
        return methods.stream()
            .anyMatch(m -> !m.getName().equals(componentName)); // 排除构造函数
    }
    
    // 检查类是否与策略组件有关联关系
    private boolean hasAssociationWithStrategy(ClassInfo classInfo, String strategyComponentName) {
        return classInfo.getFields().stream()
            .anyMatch(f -> f.getType().equals(strategyComponentName));
    }
    
    // 辅助类，用于统一处理接口和抽象类
    private static class StrategyComponent {
        String name;
        boolean isInterface;
        
        StrategyComponent(String name, boolean isInterface) {
            this.name = name;
            this.isInterface = isInterface;
        }
    }
}