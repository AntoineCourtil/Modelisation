import java.util.ArrayList;

class Test {

    static boolean visite[];

    public static void dfs(Graph g, int u) {
        visite[u] = true;
        System.out.println("Je visite " + u);
        for (Edge e : g.next(u))
            if (!visite[e.to])
                dfs(g, e.to);
    }

    public static void testGraph() {
        int n = 5;
        int i, j;
        GraphArrayList g = new GraphArrayList(n * n + 2);

        for (i = 0; i < n - 1; i++)
            for (j = 0; j < n; j++)
                g.addEdge(new Edge(n * i + j, n * (i + 1) + j, 1664 - (i + j)));

        for (j = 0; j < n; j++)
            g.addEdge(new Edge(n * (n - 1) + j, n * n, 666));

        for (j = 0; j < n; j++)
            g.addEdge(new Edge(n * n + 1, j, 0));

        g.addEdge(new Edge(13, 17, 1337));
        g.writeFile("test.dot");

        visite = new boolean[n * n + 2];
        dfs(g, 3);
    }

    public static void reduceWidth(String pictureName, int columns) {
        int[][] picture = SeamCarving.readpgm(pictureName);
        int height = picture.length;
        int width = picture[0].length;

        int firstCase = height * width;
        int lastCase = firstCase + 1;

        for (int i = 0; i < columns; i++) {


            height = picture.length;
            width = picture[0].length;
            firstCase = height * width;
            lastCase = firstCase + 1;

            int[][] pix_interest = SeamCarving.interest(picture);
            //Graph g = SeamCarving.tograph(pix_interest);

            GraphImplicit g2 = new GraphImplicit(pix_interest, pix_interest[0].length, pix_interest.length);

            //g.writeFile("test.dot");
            //g2.writeFile("testXX"+i+".dot");

            ArrayList<Integer> tritopo = SeamCarving.iterativeTritopo(g2, width*height);

            ArrayList<Integer> ccm = SeamCarving.Bellman(g2, firstCase, lastCase, tritopo);
            picture = SeamCarving.deleteColumn(picture, ccm);
            if (i % 50 == 0) {
                System.out.println("Reduce in process...");
            }
        }


        SeamCarving.writepgm(picture, "" + pictureName + ".reduceBy" + columns + ".pgm");

        System.out.println("\n  Reduce by " + columns + " on " + pictureName + " has finished ! \\o/");
    }

    public static void main(String[] args) {
        //testGraph();

        reduceWidth("bateau.pgm",350);

        /*if (args.length < 2) {
            System.out.println("usage : java -jar modelisation.jar <pictureName> <reduceyBy>");

            System.out.println("List of picture :");
            System.out.println("  - ex1.pgm");
            System.out.println("  - ex2.pgm");
            System.out.println("  - ex3.pgm");
            System.out.println("  - test.pgm");
            System.out.println("  - paris.pgm");
            System.out.println("  - bateau.pgm");
            System.exit(0);
        }

        if (args[0].equals("ex1.pgm") || args[0].equals("ex2.pgm") || args[0].equals("ex3.pgm") || args[0].equals("test.pgm") || args[0].equals("paris.pgm") || args[0].equals("bateau.pgm")) {
            reduceWidth(args[0], Integer.parseInt(args[1]));
        }
        else{
            System.err.println("Wrong inputs");
        }*/

    }
}
