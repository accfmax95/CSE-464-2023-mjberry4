import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import guru.nidi.graphviz.model.*;

public class test {

    GraphManager g;

    @Before
    public void setup() throws Exception {
        g = new GraphManager();
        g.parseGraph("src/input.dot");
    }

    @Test
    public void testParseGraph() {
        Assert.assertEquals(11, g.getNumNodes());
        Assert.assertEquals(13, g.getNumEdges());

        Assert.assertTrue(g.getNodeLabels().contains("a"));
        Assert.assertTrue(g.getNodeLabels().contains("b"));
        Assert.assertTrue(g.getNodeLabels().contains("c"));
        Assert.assertTrue(g.getNodeLabels().contains("d"));
        Assert.assertTrue(g.getNodeLabels().contains("e"));
        Assert.assertTrue(g.getNodeLabels().contains("f"));
        Assert.assertTrue(g.getNodeLabels().contains("g"));
        Assert.assertTrue(g.getNodeLabels().contains("h"));
        Assert.assertTrue(g.getNodeLabels().contains("i"));
        Assert.assertTrue(g.getNodeLabels().contains("j"));
        Assert.assertTrue(g.getNodeLabels().contains("k"));

        Assert.assertTrue(g.getEdges().contains("a->e"));
        Assert.assertTrue(g.getEdges().contains("a->b"));
        Assert.assertTrue(g.getEdges().contains("b->c"));
        Assert.assertTrue(g.getEdges().contains("c->d"));
        Assert.assertTrue(g.getEdges().contains("c->h"));
        Assert.assertTrue(g.getEdges().contains("d->h"));
        Assert.assertTrue(g.getEdges().contains("h->i"));
        Assert.assertTrue(g.getEdges().contains("i->k"));
        Assert.assertTrue(g.getEdges().contains("j->k"));
        Assert.assertTrue(g.getEdges().contains("e->f"));
        Assert.assertTrue(g.getEdges().contains("g->h"));
        Assert.assertTrue(g.getEdges().contains("f->g"));
        Assert.assertTrue(g.getEdges().contains("h->j"));

        System.out.println(g.toString());
    }

    @Test
    public void testAddNode() throws Exception {
        g.addNode("l");
        System.out.println(g.toString());

        Assert.assertEquals(12, g.getNumNodes());
        Assert.assertTrue(g.getNodeLabels().contains("l"));

        String[] nodes = new String[]{"m", "n", "o", "p"};
        g.addNodes(nodes);
        System.out.println(g.toString());

        Assert.assertEquals(16, g.getNumNodes());
        Assert.assertTrue(g.getNodeLabels().contains("m"));
        Assert.assertTrue(g.getNodeLabels().contains("n"));
        Assert.assertTrue(g.getNodeLabels().contains("o"));
        Assert.assertTrue(g.getNodeLabels().contains("p"));
    }

    @Test
    public void testAddEdge() throws Exception {
        g.addNode("l");
        g.addEdge("l", "a");
        System.out.println(g.toString());

        Assert.assertEquals(12, g.getNumNodes());
        Assert.assertEquals(14, g.getNumEdges());
        Assert.assertTrue(g.getEdges().contains("l->a"));

        g.addEdge("f", "e");
        System.out.println(g.toString());

        Assert.assertEquals(12, g.getNumNodes());
        Assert.assertEquals(15, g.getNumEdges());
        Assert.assertTrue(g.getEdges().contains("f->e"));
    }

    @Test
    public void testRemoveNode() throws Exception {
        g.removeNode("a");
        System.out.println(g.toString());

        Assert.assertFalse(g.getNodeLabels().contains("a"));
        Assert.assertEquals(10, g.getNumNodes());
    }

    @Test
    public void testRemoveNodes() throws Exception {

        String[] nodes = new String[]{"a", "b", "c"};
        g.removeNodes(nodes);
        System.out.println(g.toString());

        Assert.assertFalse(g.getNodeLabels().contains("a"));
        Assert.assertFalse(g.getNodeLabels().contains("b"));
        Assert.assertFalse(g.getNodeLabels().contains("c"));
        Assert.assertEquals(8, g.getNumNodes());
    }

    @Test
    public void testRemoveEdge() {

        g.removeEdge("b", "c");
        System.out.println(g.toString());

        Assert.assertEquals(11, g.getNumNodes());
        Assert.assertEquals(12, g.getNumEdges());
        Assert.assertFalse(g.getEdges().contains("b->c"));

        g.removeEdge("g", "h");
        System.out.println(g.toString());

        Assert.assertEquals(11, g.getNumNodes());
        Assert.assertEquals(11, g.getNumEdges());
        Assert.assertFalse(g.getEdges().contains("g->h"));
    }

    @Test
    public void testOutputDOTGraph() throws IOException {

        g.addEdge("g", "j");
        File file = new File("src/output2.dot");
        g.outputDOTGraph("src/output2.dot");
        Assert.assertTrue(file.exists());

        File file1 = new File("src/graphic.png");
        g.outputGraphics("src/graphic.png", "PNG");
        Assert.assertTrue(file1.exists());
    }

    @Test
    public void testBFSGraphSearch() {

        try {
            g.parseGraph("src/input.dot");

            MutableNode src = g.graph.nodes().stream().filter(node -> node.name().toString().equals("a")).findFirst().orElse(null);
            MutableNode dst = g.graph.nodes().stream().filter(node -> node.name().toString().equals("k")).findFirst().orElse(null);

            String bfsPath = g.GraphSearch(src, dst);

            Assert.assertEquals("a -> b -> c -> h -> j -> k", bfsPath);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}