package command;

import java.util.Deque;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import diagram.ClassDiagram;

public class SmellCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts,ClassDiagram diagram, Deque<Runnable> undoStack){
        if (parts.length < 3) return "Invalid smell command";
        
        switch (parts[1]) {
            case "detail": return smellDetail(parts[2], diagram);
            default: return "Unknown smell operation";
        }
    }

    private String smellDetail(String elementName, ClassDiagram diagram){
        List<String> allSmells=diagram.getCodeSmells();

        List<String> elementSmells=allSmells.stream()
            .filter(smell->smell.contains(elementName))
            .collect(Collectors.toList());
        
            if(elementName.isEmpty()){
                return "No bad smells found for "+elementName;
            }

            return String.join("\n", elementSmells)+"\n";
    }
}
