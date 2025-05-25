package command;

import java.util.Deque;

import diagram.ClassDiagram;

public class ModifyCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack) {
        if (parts.length < 4) return "Invalid command";
        
        switch (parts[1]) {
            case "field": return modifyField(parts, diagram, undoStack);
            case "function": return modifyMethod(parts, diagram, undoStack);
            default: return "Unknown modify operation";
        }
    }

    private String modifyField(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        return "";
    } 

    private String modifyMethod(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        return "";
    } 
}
