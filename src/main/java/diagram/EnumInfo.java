package diagram;

import java.util.ArrayList;
import java.util.List;

public class EnumInfo extends TypeInfo{
    private List<String> constants=new ArrayList<>();
    private List<Field> fields=new ArrayList<>();

    //Getters and Setters
    public List<String> getConstants(){return constants;}
    public List<Field> getFields(){return fields;}
}
