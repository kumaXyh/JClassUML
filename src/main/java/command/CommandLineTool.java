package command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import diagram.ClassDiagram;

public class CommandLineTool {
    private ClassDiagram diagram;
    private Deque<Runnable> undoStack=new ArrayDeque<>();
    private final Map<String, CommandHandler> handlers=new HashMap<>();

    public CommandLineTool(ClassDiagram diagram) {
        this.diagram = diagram;

        handlers.put("add", new AddCommandHandler());
        handlers.put("delete", new DeleteCommandHandler());
        handlers.put("modify", new ModifyCommandHandler());
        handlers.put("undo", new UndoCommandHandler());
    }

    /**
     * @param command 输入的命令
     * @return 如果是查询性质语句，将查询的结果保存在返回值中。Undo语句可能返回的信息也保存在返回值中。
     */
    public String execute(String command) {
        if(command==null||command.trim().isEmpty()){
            return "Empty Command";
        }

        String[] parts=command.split("\\s+");
        String operation=parts[0];

        CommandHandler handler=handlers.get(operation);
        if(handler==null){
            return "Unknown command: "+operation;
        }

        try{
            return handler.handle(parts, diagram, undoStack);
        }catch(Exception e){
            return "Error executing command: "+e.getMessage();
        }
    }
}