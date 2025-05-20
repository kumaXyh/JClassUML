package diagram;

import java.io.IOException;
import java.nio.file.Path;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassDiagramGenerator {
    // 解析源文件路径，返回ClassDiagram对象
    public ClassDiagram parse(Path sourcePath) throws IOException {
        // 返回ClassDiagram对象
        CompilationUnit cu = StaticJavaParser.parse(sourcePath);
        ClassDiagram classDiagram = new ClassDiagram();

        cu.accept(new ClassVisitor(), classDiagram);
        cu.accept(new InterfaceVisitor(), classDiagram);

        return classDiagram;
    }

    private static class ClassVisitor extends VoidVisitorAdapter<ClassDiagram>{
        @Override
        public void visit(ClassOrInterfaceDeclaration decl, ClassDiagram diagram){
            if(decl.isInterface()||decl.isAbstract())return;

            ClassInfo classInfo = new ClassInfo();
            classInfo.setName(decl.getNameAsString());
            
            //解析字段
            decl.getFields().forEach(field -> {
                String accessModifier=convertModifier(field.getAccessSpecifier());
                boolean isStatic=field.isStatic();
                String type=field.getElementType().asString();

                field.getVariables().forEach(v ->{
                    Field f=new Field();
                    f.setAccessModifier(accessModifier);
                    f.setStatic(isStatic);
                    f.setName(v.getNameAsString());
                    f.setType(type);
                    classInfo.getFields().add(f);
                });
            });

            //解析方法
            decl.getMethods().forEach(method ->{
                if (method.getName().asString().equals(decl.getNameAsString())) return; 

                Method m=new Method();
                m.setAccessModifier(convertModifier(method.getAccessSpecifier()));
                m.setStatic(method.isStatic());
                m.setName(method.getNameAsString());
                m.setReturnType(method.getType().asString());

                method.getParameters().forEach(p ->{
                    Parameter param=new Parameter();
                    param.setName(p.getNameAsString());
                    param.setType(p.getType().asString());
                    m.getParameters().add(param);
                });

                classInfo.getMethods().add(m);
            });

            diagram.addClass(classInfo);
        }
        private String convertModifier(AccessSpecifier specifier){
            switch (specifier) {
                case PUBLIC: return "+";
                case PRIVATE: return "-";
                case PROTECTED: return "#";
                default: return "~";
            }
        }
    }

    private static class InterfaceVisitor extends VoidVisitorAdapter<ClassDiagram>{
        @Override
        public void visit(ClassOrInterfaceDeclaration decl,ClassDiagram diagram){
            if(!decl.isInterface())return;

            InterfaceInfo interfaceInfo=new InterfaceInfo();
            interfaceInfo.setName(decl.getNameAsString());
            
            decl.getMethods().forEach(method ->{
                Method m=new Method();
                //接口方法强制为public
                m.setAccessModifier("+");
                m.setStatic(method.isStatic());
                m.setName(method.getNameAsString());
                m.setReturnType(method.getType().asString());

                method.getParameters().forEach(p ->{
                    Parameter param=new Parameter();
                    param.setName(p.getNameAsString());
                    param.setType(p.getType().asString());
                    m.getParameters().add(param);
                });

                interfaceInfo.getMethods().add(m);
            });

            diagram.addInterface(interfaceInfo);
        }
    }
}
