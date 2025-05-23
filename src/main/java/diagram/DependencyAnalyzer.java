package diagram;

import java.util.List;

public class DependencyAnalyzer extends BaseAnalyer{
    public void analyze(List<ClassInfo> classes,List<InterfaceInfo>interfaceInfos,List<EnumInfo> enums){
        analyzeMethods(classes);
        analyzeMethods(interfaceInfos);
        analyzeMethods(enums);
    }
    private void analyzeMethods(List<? extends TypeInfo> types) {
        types.forEach(t -> 
            t.getMethods().forEach(m -> {
                parseType(m.getReturnType(), t.getName());
                m.getParameters().forEach(p -> 
                    parseType(p.getType(), t.getName())
                );
                m.getLocalVariables().forEach(lv->
                    parseType(lv.getType(), t.getName())
                );
            })
        );
    }
}
