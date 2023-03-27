import guru.nidi.graphviz.parse.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.engine.*;

import static guru.nidi.graphviz.model.Factory.*;

import java.io.*;
import java.util.*;
import java.util.Iterator;

public class GraphManager {

    public MutableGraph graph;

    // Feature 1
    public void parseGraph(String filepath) throws IOException {

        graph = new Parser().read(new File(filepath));
    }

    @Override
    public String toString() {
        System.out.println("Number of nodes: " + getNumNodes());
        System.out.print("Label of nodes: ");
        System.out.println(getNodeLabels());
        System.out.println("Number of edges: " + getNumEdges());
        System.out.println("Node-edge directions: ");
        System.out.println(getEdges());
        System.out.print("\n");
        return graph.toString();
    }

    public int getNumNodes() {

        return graph.nodes().size();
    }

    public String getNodeLabels() {

        String nodes = "";
        for (MutableNode node: graph.nodes()) {

            nodes = nodes + node.name() + " ";
        }

        return nodes;
    }

    public int getNumEdges() {

        return graph.edges().size();
    }

    public String getEdges() {

        String nodes = "";
        for (Link link: graph.edges()) {

            nodes = nodes + link.from().name() + "->" + link.to().name() + " ";
        }

        return nodes;
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

        Graphviz.fromGraph(graph).width(700).render(Format.DOT).toFile(new File(filename));
    }

    public void outputGraphics(String filename, String format) throws IOException {

        switch (format) {

            case "PNG":
            case "png":

                Graphviz.fromGraph(graph).width(700).render(Format.PNG).toFile(new File(filename));
                break;
            case "svg":
            case "SVG":

                Graphviz.fromGraph(graph).width(700).render(Format.SVG).toFile(new File(filename));
                break;
        }
    }

    public String GraphSearch(MutableNode src, MutableNode dst, Algorithm algo) {

        if (algo == Algorithm.DFS) {

            String path = "";
            Map<MutableNode, MutableNode> parent = new HashMap<>();
            Stack<MutableNode> stack = new Stack<>();

            if (src == null || dst == null) {

                return null;
            }

            stack.push(src);

            while (!stack.isEmpty()) {

                MutableNode current = stack.pop();

                if (current.equals(dst)) {

                    while (parent.containsKey(current)) {

                        current = parent.get(current);
                        path = current.name() + " -> " + path;
                    }

                    path = path + dst.name();
                    return path;
                }

                for (Link link : current.links()) {

                    LinkTarget neighLink = link.to();
                    MutableNode neighbor = graph.nodes().stream().filter(node -> node.name().toString().equals(neighLink.name().toString())).findFirst().orElse(null);
                    if (!parent.containsKey(neighbor)) {

                        stack.push(neighbor);
                        parent.put(neighbor, current);
                    }
                }
            }

            return path;
        } else if (algo == Algorithm.BFS) {

            String path = "";
            Map<MutableNode, MutableNode> parent = new HashMap<>();
            Queue<MutableNode> queue = new LinkedList<>();

            if (src == null || dst == null) {

                return null;
            }

            queue.add(src);

            int count = 0;
            while (!queue.isEmpty()) {

                MutableNode current = queue.poll();
                if (current.equals(dst)) {

                    if (count == 0) {

                        path = path + current.name();
                    } else {

                        path = path + current.name();
                    }

                    while (parent.containsKey(current)) {

                        current = parent.get(current);
                        if (current != null) {

                            path = current.name().toString() + " -> " + path;
                        }

                    }

                    return path;
                }


                for (Link link : current.links()) {

                    LinkTarget neighLink = link.to();
                    MutableNode neighbor = graph.nodes().stream().filter(node -> node.name().toString().equals(neighLink.name().toString())).findFirst().orElse(null);
                    if (!parent.containsKey(neighbor)) {

                        queue.add(neighbor);
                        parent.put(neighbor, current);
                    }
                }

                count++;
            }

            return path;
        }

        return null;
    }

    enum Algorithm {

        BFS,
        DFS
    }
}

