package diagram;

import java.util.*;

public class CircularAnalyzer {
    public String analyze(List<String[]> relations){
        //构建邻接表
        Map<String, Set<String>> graph=new HashMap<>();
        for(String[] relation:relations){
            String from=relation[0];
            String to=relation[1];
            graph.computeIfAbsent(from, k->new HashSet<>()).add(to);
            graph.putIfAbsent(to, new HashSet<>());
        }

        //DFS找环
        for(String node : graph.keySet()){
            List<String> cycle=findCycle(graph,node,new HashSet<>(), new ArrayList<>());
            if(!cycle.isEmpty()){
                return formatCycle(cycle);
            }
        }
        return "";
    }

    private static List<String> findCycle(Map<String, Set<String>> graph, String current, Set<String> visited, List<String> path){
        if(path.contains(current)){
            int startIdx=path.indexOf(current);
            List<String> cycle=new ArrayList<>(path.subList(startIdx, path.size()));
            cycle.add(current);
            return cycle;
        }

        if(visited.contains(current)){
            return Collections.emptyList();
        }

        visited.add(current);
        path.add(current);

        for(String neighbor:graph.getOrDefault(current, Collections.emptySet())){
            List<String> cycle=findCycle(graph, neighbor, visited, path);
            if(!cycle.isEmpty()){
                return cycle;
            }
        }

        path.remove(path.size()-1);
        return Collections.emptyList();
    }

    private static String formatCycle(List<String> cycle){
        return "Circular Dependency: "+String.join(" <.. ",cycle);
    }

}
