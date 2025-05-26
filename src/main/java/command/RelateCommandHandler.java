package command;

import java.util.Deque;

import diagram.*;

public class RelateCommandHandler extends CommandHandler{
    @Override
    public String handle(String[] parts,ClassDiagram diagram, Deque<Runnable> undoStack){
        if(parts.length<3)return "Invalid query command";

        return relate(parts,diagram);
    }

    private String relate(String[] parts, ClassDiagram diagram){
        String elementA=parts[1];
        String elementB=parts[2];

        if(!diagram.getRelationshipAnalyzer().isAnalyzed()){
            diagram.getRelationshipAnalyzer().analyzeRelations(diagram.getClasses(), diagram.getInterfaces(), diagram.getEnums());
        }

        String relation=diagram.getRelationshipAnalyzer().getRelationsBetween(elementA,elementB);
        return relation.isEmpty() ? "No relation found" : relation;
    }
}
