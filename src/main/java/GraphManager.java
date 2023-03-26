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

}

