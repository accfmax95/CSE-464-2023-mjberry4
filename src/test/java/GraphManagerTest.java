import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import guru.nidi.graphviz.model.*;

public class GraphManagerTest {

    GraphManager g;

    @Before
    public void setup() throws Exception {
        g = new GraphManager();
        g.parseGraph("src/input.dot");
    }

    @Test
    public void testParseGraph() {
        Assert.assertEquals(8, g.getNumNodes());
        Assert.assertEquals(12, g.getNumEdges());

        Assert.assertTrue(g.getNodeLabels().contains("a"));
        Assert.assertTrue(g.getNodeLabels().contains("b"));
        Assert.assertTrue(g.getNodeLabels().contains("c"));
        Assert.assertTrue(g.getNodeLabels().contains("d"));
        Assert.assertTrue(g.getNodeLabels().contains("e"));
        Assert.assertTrue(g.getNodeLabels().contains("f"));
        Assert.assertTrue(g.getNodeLabels().contains("g"));
        Assert.assertTrue(g.getNodeLabels().contains("h"));

        Assert.assertTrue(g.getEdges().contains("a->c"));
        Assert.assertTrue(g.getEdges().contains("a->b"));
        Assert.assertTrue(g.getEdges().contains("b->e"));
        Assert.assertTrue(g.getEdges().contains("b->f"));
        Assert.assertTrue(g.getEdges().contains("c->e"));
        Assert.assertTrue(g.getEdges().contains("c->g"));
        Assert.assertTrue(g.getEdges().contains("d->g"));
        Assert.assertTrue(g.getEdges().contains("d->f"));
        Assert.assertTrue(g.getEdges().contains("e->h"));
        Assert.assertTrue(g.getEdges().contains("f->h"));

        System.out.println(g.toString());
    }

    @Test
    public void testAddNode() throws Exception {
        g.addNode("i");
        System.out.println(g.toString());

        Assert.assertEquals(9, g.getNumNodes());
        Assert.assertTrue(g.getNodeLabels().contains("i"));

        String[] nodes = new String[]{"m", "n", "o", "p"};
        g.addNodes(nodes);
        System.out.println(g.toString());

        Assert.assertEquals(13, g.getNumNodes());
        Assert.assertTrue(g.getNodeLabels().contains("m"));
        Assert.assertTrue(g.getNodeLabels().contains("n"));
        Assert.assertTrue(g.getNodeLabels().contains("o"));
        Assert.assertTrue(g.getNodeLabels().contains("p"));
    }

    @Test
    public void testAddEdge() throws Exception {
        g.addNode("i");
        g.addEdge("a", "i");
        System.out.println(g.toString());

        Assert.assertEquals(9, g.getNumNodes());
        Assert.assertEquals(13, g.getNumEdges());
        Assert.assertTrue(g.getEdges().contains("a->i"));
    }

    @Test
    public void testRemoveNode() throws Exception {
        g.removeNode("a");
        System.out.println(g.toString());

        Assert.assertFalse(g.getNodeLabels().contains("a"));
        Assert.assertEquals(7, g.getNumNodes());
    }

    @Test
    public void testRemoveNodes() throws Exception {

        String[] nodes = new String[]{"a", "b", "c"};
        g.removeNodes(nodes);
        System.out.println(g.toString());

        Assert.assertFalse(g.getNodeLabels().contains("a"));
        Assert.assertFalse(g.getNodeLabels().contains("b"));
        Assert.assertFalse(g.getNodeLabels().contains("c"));
        Assert.assertEquals(5, g.getNumNodes());
    }

    @Test
    public void testRemoveEdge() {

        g.removeEdge("b", "f");
        System.out.println(g.toString());

        Assert.assertEquals(8, g.getNumNodes());
        Assert.assertEquals(11, g.getNumEdges());
        Assert.assertFalse(g.getEdges().contains("b->f"));

        g.removeEdge("g", "h");
        System.out.println(g.toString());

        Assert.assertEquals(8, g.getNumNodes());
        Assert.assertEquals(10, g.getNumEdges());
        Assert.assertFalse(g.getEdges().contains("g->h"));
    }

    @Test
    public void testOutputDOTGraph() throws IOException {

        File file = new File("src/output2.dot");
        g.outputDOTGraph("src/output2.dot");
        Assert.assertTrue(file.exists());

        File file1 = new File("src/graphic.png");
        g.outputGraphics("src/graphic.png", "PNG");
        Assert.assertTrue(file1.exists());
    }

    @Test
    public void testGraphSearch() {

        try {
            BFS bfs = new BFS();
            DFS dfs = new DFS();
            RandomWalk rw = new RandomWalk();
            g.parseGraph("src/input.dot");

            MutableNode src = g.graph.nodes().stream().filter(node -> node.name().toString().equals("a")).findFirst().orElse(null);
            MutableNode dst = g.graph.nodes().stream().filter(node -> node.name().toString().equals("h")).findFirst().orElse(null);

            String bfsPath = bfs.GraphSearch(src, dst, g.graph, Algorithm.BFS);

            Assert.assertEquals("a -> b -> e -> h", bfsPath);
            System.out.println("BFS Output: " + bfsPath);

            String dfsPath = dfs.GraphSearch(src, dst, g.graph, Algorithm.DFS);

            Assert.assertEquals("a -> c -> e -> h", dfsPath);
            System.out.println("DFS Output: " + dfsPath);

            src = g.graph.nodes().stream().filter(node -> node.name().toString().equals("d")).findFirst().orElse(null);
            dst = g.graph.nodes().stream().filter(node -> node.name().toString().equals("h")).findFirst().orElse(null);

            bfsPath = bfs.GraphSearch(src, dst, g.graph, Algorithm.BFS);

            Assert.assertEquals("d -> f -> h", bfsPath);
            System.out.println("BFS Output: " + bfsPath);

            dfsPath = dfs.GraphSearch(src, dst, g.graph, Algorithm.DFS);

            Assert.assertEquals("d -> g -> h", dfsPath);
            System.out.println("DFS Output: " + dfsPath);

            src = g.graph.nodes().stream().filter(node -> node.name().toString().equals("a")).findFirst().orElse(null);
            dst = g.graph.nodes().stream().filter(node -> node.name().toString().equals("h")).findFirst().orElse(null);


            String rwPath1 = rw.GraphSearch(src, dst, g.graph, Algorithm.RANDOM_WALK);
            System.out.println("RW Output: " + rwPath1);
            String rwPath2 = rw.GraphSearch(src, dst, g.graph, Algorithm.RANDOM_WALK);
            System.out.println("RW Output: " + rwPath2);

            Assert.assertNotEquals(rwPath1, rwPath2);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}
