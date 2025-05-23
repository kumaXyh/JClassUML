package diagram;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;;

public class BaseAnalyer {
    protected final Set<String> relations=new HashSet<>();
    private static final Set<String> BASIC_TYPES=Set.of(
        "String","int","Integer","long","Long","double",
        "Double","boolean","Boolean","float","Float",
        "char","byte","short","void"
    );
    private static final Set<String> CONTAINER_TYPES=Set.of(
        "List","Set","Map","ArrayList","LinkedList","HashMap"
    );

    protected void parseType(String type,String source){
        //type:待判断类型
        //source:type所属类

        if(isBasicOrContainerType(type))return;

        //pattern编译正则表达式:判断不是<>,[]或空白字符，+表示1个或多个字符
        Pattern pattern=Pattern.compile("[^<>,\\s\\[\\]]+");
        //matcher查找匹配pattern（模式）的内容
        Matcher matcher=pattern.matcher(type);
        while(matcher.find()){
            String extractedType=matcher.group();
            if(!isBasicOrContainerType(extractedType)&&!extractedType.equals(source)){
                relations.add(extractedType+" "+source);
            }
        }
    }
    private boolean isBasicOrContainerType(String type){
        return BASIC_TYPES.contains(type)||CONTAINER_TYPES.contains(type);
    }
    //Getter
    public Set<String> getRelations(){return relations;}
}
