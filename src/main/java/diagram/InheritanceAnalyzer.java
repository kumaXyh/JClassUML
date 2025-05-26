package diagram;

import java.util.*;

public class InheritanceAnalyzer {
    private final Map<String, List<String>> childToParent=new HashMap<>();
    private final Map<String, List<String>> parentToChildren=new HashMap<>();
    private final Map<String, Integer> depthCache=new HashMap<>();
    private final Map<String, ClassInfo> classNameToClass=new HashMap<>();

    public void buildTree(List<ClassInfo> classes){
        for(ClassInfo classInfo:classes){
            classNameToClass.put(classInfo.getName(), classInfo);
        }
        for(ClassInfo classInfo:classes){
            String className=classInfo.getName();
            String parentName=classInfo.getExtendsClass();

            //initialize
            childToParent.putIfAbsent(className, new ArrayList<>());
            parentToChildren.putIfAbsent(className, new ArrayList<>());

            if(parentName!=null){
                childToParent.get(className).add(parentName);
                parentToChildren.computeIfAbsent(parentName, k->new ArrayList<>()).add(className);

                ClassInfo parentClass=classNameToClass.get(parentName);
                if(parentClass!=null){
                    parentClass.getSubClasses().add(className);
                }
            }
        }
    }

    public List<String> analyze(List<String> smells){
        detectDeepInheritance(smells);
        detectWideInheritance(smells);
        return smells;
    }

    //InheritanceAbuse
    private void detectDeepInheritance(List<String> smells){
        for(String className:childToParent.keySet()){
            int depth=calculateDepth(className);
            if(depth>=5){
                smells.add("Inheritance Abuse: "+buildInheritanceChain(className));
            }
        }
    }

    //Too Many Children
    private void detectWideInheritance(List<String> smells){
        parentToChildren.forEach((parent,children)->{
            if(children.size()>=10){
                smells.add("Too Many Children: "+parent);
            }
        });
    }

    private int calculateDepth(String className){
        if(depthCache.containsKey(className)){
            return depthCache.get(className);
        }

        List<String> parents=childToParent.getOrDefault(className, Collections.emptyList());
        if(parents.isEmpty()){
            depthCache.put(className,0);
            return 0;
        }

        int maxDepth=parents.stream()
                            .mapToInt(this::calculateDepth)
                            .max()
                            .orElse(0)+1;

        depthCache.put(className,maxDepth);
        return maxDepth;
    }
    private String buildInheritanceChain(String className){
        LinkedList<String> chain=new LinkedList<>();
        String current=className;

        while(current!=null){
            chain.addFirst(current);
            List<String> parents=childToParent.getOrDefault(current, Collections.emptyList());
            current=parents.isEmpty()?null:parents.get(0);
        }

        return String.join(" <|-- ", chain);
    }
}
