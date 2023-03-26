public class Part1 {

    public static void main(String[] args) {

        try {

            GraphManager g = new GraphManager();
            g.parseGraph("src/input.dot");
            g.removeNode("a");
            System.out.println(g.toString());
            g.outputGraphics("output.png","png");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
