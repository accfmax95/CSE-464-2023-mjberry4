import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.IOException;
import java.util.*;

public abstract class GraphSearchTemplate extends GraphManager {

    MutableGraph graph;

    public final String GraphSearch(MutableNode src, MutableNode dst, MutableGraph g) {
        String path = "";
        graph = g;
        Map<MutableNode, MutableNode> parent = new HashMap<>();
        Queue<MutableNode> queue = new LinkedList<>();
        Stack<MutableNode> stack = new Stack<>();

        if (src == null || dst == null) {
            return null;
        }

        addStartNode(src, stack, queue);

        while (!stack.isEmpty() || !queue.isEmpty()) {
            MutableNode current = getCurrentNode(stack, queue);
            if (current.equals(dst)) {
                path = getPath(current, parent);
                return path;
            }
            addUnvisitedNeighbors(current, stack, queue, parent);
        }
        return path;
    }

    protected abstract void addStartNode(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue);

    protected abstract MutableNode getCurrentNode(Stack<MutableNode> stack, Queue<MutableNode> queue);

    protected void addUnvisitedNeighbors(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue, Map<MutableNode, MutableNode> parent) {
        for (Link link : node.links()) {
            LinkTarget neighLink = link.to();
            MutableNode neighbor = this.graph.nodes().stream().filter(n -> n.name().toString().equals(neighLink.name().toString())).findFirst().orElse(null);
            if (!parent.containsKey(neighbor)) {
                addNodeToSearch(neighbor, stack, queue);
                parent.put(neighbor, node);
            }
        }
    }

    protected abstract void addNodeToSearch(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue);

    protected String getPath(MutableNode current, Map<MutableNode, MutableNode> parent) {
        String path = current.name().toString();
        while (parent.containsKey(current)) {
            current = parent.get(current);
            path = current.name().toString() + " -> " + path;
        }
        return path;
    }
}



