import guru.nidi.graphviz.parse.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.engine.*;

import static guru.nidi.graphviz.model.Factory.*;

import java.io.*;
import java.util.*;
import java.util.Iterator;

public class GraphManager {

    public MutableGraph graph;
    private final int DEFAULT_GRAPH_WIDTH = 700;
    private static final Format FORMAT_PNG = Format.PNG;
    private static final Format FORMAT_SVG = Format.SVG;
    private static final Format FORMAT_DOT = Format.DOT;

    // Feature 1
    public void parseGraph(String filepath) throws IOException {

        try (InputStream input = new FileInputStream(filepath)) {
            graph = new Parser().read(input);
        }
    }

    @Override
    public String toString() {
        printGraphSummary();
        return graph.toString();
    }

    private void printGraphSummary() {
        System.out.println("Number of nodes: " + getNumNodes());
        System.out.print("Label of nodes: ");
        System.out.println(getNodeLabels());
        System.out.println("Number of edges: " + getNumEdges());
        System.out.println("Node-edge directions: ");
        System.out.println(getEdges());
        System.out.print("\n");
    }


    public int getNumNodes() {

        return graph.nodes().size();
    }

    public String getNodeLabels() {

        String nodeLabelString = "";
        for (MutableNode node: graph.nodes()) {

            nodeLabelString = nodeLabelString + node.name() + " ";
        }

        return nodeLabelString;
    }

    public int getNumEdges() {

        return graph.edges().size();
    }

    public String getEdges() {

        String stringOfEdges = "";
        for (Link link: graph.edges()) {

            stringOfEdges = stringOfEdges + link.from().name() + "->" + link.to().name() + " ";
        }

        return stringOfEdges;
    }

    // Feature 2
    public void addNode(String label) {

        if (!graph.nodes().contains(mutNode(label))) {

            graph.add(mutNode(label));
        }
    }

    public void removeNode(String label) {

        for (MutableNode node: graph.rootNodes()) {

            if (node.name().toString().compareTo(label) == 0) {

                Iterator<Link> lin = node.links().iterator();
                while (lin.hasNext()) {

                    removeEdge(node.name().toString(), lin.next().name().toString());
                }
            }
        }

        Iterator<MutableNode> it = graph.rootNodes().iterator();
        while (it.hasNext()) {

            if (it.next().name().toString().compareTo(label) == 0) {

                it.remove();
            }

        }
    }

    public void addNodes(String[] labels) {

        for (String node: labels) {

            graph.add(mutNode(node));
        }
    }

    public void removeNodes(String[] labels) {

        for (String node: labels) {

            removeNode(node);
        }
    }

    // Feature 3
    public void addEdge(String srcLabel, String dstLabel) {

        MutableNode srcNode = null;
        MutableNode destNode = null;
        for (MutableNode node: graph.rootNodes()) {

            if (node.name().toString().compareTo(srcLabel) == 0) {

                srcNode = node;
            }

            if (node.name().toString().compareTo(dstLabel) == 0) {

                destNode = node;
            }
        }

        srcNode.links().add(srcNode.linkTo(destNode.asLinkTarget()));
    }

    public void removeEdge(String srcLabel, String dstLabel) {

        Link removeLink = null;
        for (MutableNode node: graph.nodes()) {

            for (Link link: node.links()) {

                if (link.from().name().toString().compareTo(srcLabel) == 0 && link.to().name().toString().compareTo(dstLabel) == 0) {

                    removeLink = link;
                }
            }
            if (removeLink != null) {

                node.links().remove(removeLink);
                return;
            }
        }
    }

    // Feature 4
    public void outputDOTGraph(String filename) throws IOException {

        Graphviz.fromGraph(graph).width(DEFAULT_GRAPH_WIDTH).render(FORMAT_DOT).toFile(new File(filename));
    }

    public void outputGraphics(String filename, String format) throws IOException {

        switch (format) {

            case "PNG":
            case "png":

                Graphviz.fromGraph(graph).width(DEFAULT_GRAPH_WIDTH).render(FORMAT_PNG).toFile(new File(filename));
                break;
            case "svg":
            case "SVG":

                Graphviz.fromGraph(graph).width(DEFAULT_GRAPH_WIDTH).render(FORMAT_SVG).toFile(new File(filename));
                break;
        }
    }

}

class BFS extends GraphSearchTemplate {
    Queue<MutableNode> queue;

    @Override
    protected void addStartNode(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        this.queue = queue;
        queue.add(node);
    }

    @Override
    protected MutableNode getCurrentNode(Stack<MutableNode> stack, Queue<MutableNode> queue) {
        return queue.poll();
    }

    @Override
    protected void addNodeToSearch(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        queue.add(node);
    }
}

class DFS extends GraphSearchTemplate {
    Stack<MutableNode> stack;

    @Override
    protected void addStartNode(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        this.stack = stack;
        stack.push(node);
    }

    @Override
    protected MutableNode getCurrentNode(Stack<MutableNode> stack, Queue<MutableNode> queue) {
        return stack.pop();
    }

    @Override
    protected void addNodeToSearch(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        stack.push(node);
    }
}

class RandomWalk extends GraphSearchTemplate {
    Queue<MutableNode> queue;

    @Override
    protected void addStartNode(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        this.queue = queue;
        queue.add(node);
    }

    @Override
    protected MutableNode getCurrentNode(Stack<MutableNode> stack, Queue<MutableNode> queue) {
        List<MutableNode> neighbors = new ArrayList<>();
        MutableNode current = queue.poll();
        for (Link link : current.links()) {
            LinkTarget neighLink = link.to();
            MutableNode neighbor = this.graph.nodes().stream().filter(n -> n.name().toString().equals(neighLink.name().toString())).findFirst().orElse(null);
            neighbors.add(neighbor);
        }
        if (neighbors.isEmpty()) {
            return current;
        } else {
            int randomIndex = (int) (Math.random() * neighbors.size());
            return neighbors.get(randomIndex);
        }
    }

    @Override
    protected void addNodeToSearch(MutableNode node, Stack<MutableNode> stack, Queue<MutableNode> queue) {
        queue.add(node);
    }
}

