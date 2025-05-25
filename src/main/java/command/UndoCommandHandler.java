package command;

import java.util.Deque;

import diagram.ClassDiagram;

public class UndoCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts, ClassDiagram diagram, Deque<Runnable> undoStack){
        if(undoStack.isEmpty()){
            return "No command to undo";
        }
        undoStack.pop().run();
        return "Undo successful";
    }
}
