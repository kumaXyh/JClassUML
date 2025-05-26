package diagram;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassDiagramGenerator {
    // 解析源文件路径，返回ClassDiagram对象
    public ClassDiagram parse(Path sourcePath) throws IOException {
        ClassDiagram classDiagram=new ClassDiagram();

        //目录处理
        if(Files.isDirectory(sourcePath)){
            try(Stream<Path> paths=Files.walk(sourcePath)){
                paths.filter(Files::isRegularFile)
                        .filter(p->p.toString().endsWith(".java"))
                        .forEach(p->{
                            try{
                                parseSingleFile(p, classDiagram);
                            }catch(IOException e){
                                throw new UncheckedIOException("Failed to parse file: "+p.toString(), e);
                            }
                        });
            }
        }else{
            parseSingleFile(sourcePath, classDiagram);
        }
        return classDiagram;
    }

    private void parseSingleFile(Path filePath, ClassDiagram diagram) throws IOException{
        CompilationUnit cu = StaticJavaParser.parse(filePath);

        cu.accept(new ClassVisitor(), diagram);
        cu.accept(new InterfaceVisitor(), diagram);
        cu.accept(new EnumVisitor(), diagram);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<ClassDiagram>{
        @Override
        public void visit(ClassOrInterfaceDeclaration decl, ClassDiagram diagram){
            if(decl.isInterface())return;

            ClassInfo classInfo = new ClassInfo();
            classInfo.setName(decl.getNameAsString());
            classInfo.setAbstract(decl.isAbstract());

            //解析类泛型参数
            if(decl.getTypeParameters().isNonEmpty()){
                classInfo.setTypeParameters(decl.getTypeParameters().toString());
            }

            //解析继承关系
            decl.getExtendedTypes().forEach(type ->{
                classInfo.setExtendsClass(type.getNameAsString());
            });

            //解析接口实现
            decl.getImplementedTypes().forEach(type ->{
                classInfo.addImplementsInterface(type.getNameAsString());
            });
            
            //解析字段
            decl.getFields().forEach(field -> {
                String accessModifier=convertModifier(field.getAccessSpecifier());
                boolean isStatic=field.isStatic();

                field.getVariables().forEach(v ->{
                    Field f=new Field();
                    f.setAccessModifier(accessModifier);
                    f.setStatic(isStatic);
                    f.setName(v.getNameAsString());
                    String type=v.getType().toString();
                    f.setType(type);
                    classInfo.getFields().add(f);
                });
            });

            //解析方法
            decl.getMethods().forEach(method ->{
                Method m=new Method();
                m.setAccessModifier(convertModifier(method.getAccessSpecifier()));
                m.setStatic(method.isStatic());
                m.setAbstract(method.isAbstract());
                m.setName(method.getNameAsString());
                m.setReturnType(method.getType().asString());

                // 解析方法泛型参数
                if (method.getTypeParameters().isNonEmpty()) {
                    m.setTypeParameters("<" + method.getTypeParameters().stream()
                        .map(tp -> tp.getNameAsString())
                        .collect(Collectors.joining(", ")) + ">");
                }

                //解析方法泛型参数
                if(method.getTypeParameters().isNonEmpty()){
                    m.setTypeParameters(method.getTypeParameters().toString());
                }
                //解析参数类型
                method.getParameters().forEach(p ->{
                    Parameter param=new Parameter();
                    param.setName(p.getNameAsString());
                    param.setType(p.getType().asString());
                    m.getParameters().add(param);
                });

                method.getBody().ifPresent(body->{
                    body.findAll(VariableDeclarator.class).forEach(vd->{
                        Parameter lv=new Parameter();
                        lv.setType(vd.getType().asString());
                        lv.setName(vd.getNameAsString());
                        m.getLocalVariables().add(lv);
                    });
                });

                classInfo.getMethods().add(m);
            });

            //解析构造函数
            decl.getConstructors().forEach(constructor -> {
                Method m = new Method();
                m.setName(constructor.getNameAsString()); // 构造函数名 = 类名
                m.setAccessModifier(convertModifier(constructor.getAccessSpecifier()));
                m.setReturnType(""); // 构造函数无返回类型

                // 解析参数
                constructor.getParameters().forEach(p -> {
                    Parameter param = new Parameter();
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

            //解析接口继承
            decl.getExtendedTypes().forEach(type ->{
                interfaceInfo.addExtendsInterface(type.getNameAsString());
            });
            
            //解析方法
            decl.getMethods().forEach(method ->{
                Method m=new Method();
                //接口方法强制为public
                m.setAccessModifier("+");
                m.setStatic(method.isStatic());
                m.setAbstract(method.isAbstract());
                m.setName(method.getNameAsString());
                m.setReturnType(method.getType().asString());

                // 解析方法泛型参数
                if (method.getTypeParameters().isNonEmpty()) {
                    m.setTypeParameters("<" + method.getTypeParameters().stream()
                        .map(tp -> tp.getNameAsString())
                        .collect(Collectors.joining(", ")) + ">");
                }

                method.getParameters().forEach(p ->{
                    Parameter param=new Parameter();
                    param.setName(p.getNameAsString());
                    param.setType(p.getType().asString());
                    m.getParameters().add(param);
                });

                method.getBody().ifPresent(body->{
                    body.findAll(VariableDeclarator.class).forEach(vd->{
                        Parameter lv=new Parameter();
                        lv.setType(vd.getType().asString());
                        lv.setName(vd.getNameAsString());
                        m.getLocalVariables().add(lv);
                    });
                });

                interfaceInfo.getMethods().add(m);
            });

            diagram.addInterface(interfaceInfo);
        }
    }

    private static class EnumVisitor extends VoidVisitorAdapter<ClassDiagram> {
        @Override
        public void visit(EnumDeclaration decl,ClassDiagram diagram){
            EnumInfo enumInfo = new EnumInfo();
            enumInfo.setName(decl.getNameAsString());

            //解析枚举常量
            decl.getEntries().forEach(entry->{
                String constant=entry.getNameAsString();
                if(entry.getArguments().isNonEmpty()){
                    constant+=entry.getArguments().toString();
                }
                enumInfo.getConstants().add(constant);
            });

            //解析字段
            decl.getFields().forEach(field -> {
                boolean isStatic=field.isStatic();
                String type=field.getElementType().asString();

                field.getVariables().forEach(v->{
                    Field f=new Field();
                    //默认private
                    f.setAccessModifier("-");
                    f.setStatic(isStatic);
                    f.setName(v.getNameAsString());
                    f.setType(type);
                    enumInfo.getFields().add(f);
                });
            });

            //解析方法
            decl.getMethods().forEach(method->{
                Method m = new Method();
                m.setAccessModifier("+");
                m.setStatic(method.isStatic());
                m.setName(method.getNameAsString());
                m.setReturnType(method.getType().asString());

                method.getParameters().forEach(p->{
                    Parameter param=new Parameter();
                    param.setName(p.getNameAsString());
                    param.setType(p.getType().asString());
                    m.getParameters().add(param);
                });

                method.getBody().ifPresent(body->{
                    body.findAll(VariableDeclarator.class).forEach(vd->{
                        Parameter lv=new Parameter();
                        lv.setType(vd.getType().asString());
                        lv.setName(vd.getNameAsString());
                        m.getLocalVariables().add(lv);
                    });
                });

                enumInfo.getMethods().add(m);
            });

            diagram.addEnum(enumInfo);
        }
    }
}
