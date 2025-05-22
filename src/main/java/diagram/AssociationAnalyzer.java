package diagram;

import java.util.List;

public class AssociationAnalyzer extends RelationshipAnalyer{
    public void analyze(List<ClassInfo> classes,List<EnumInfo> enums){
        classes.forEach(c->
            c.getFields().forEach(f->
                parseType(f.getType(), c.getName())
            )
        );

        enums.forEach(e->
            e.getFields().forEach(f->
                parseType(f.getType(), e.getName())
            )
        );
    }
}
