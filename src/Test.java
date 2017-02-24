import java.util.ArrayList;

import static jdk.nashorn.internal.objects.NativeArray.reduce;

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

    public static void reduceWidth(String pictureName, int columns, boolean continu) {
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
            //g2.writeFile("testWidth_"+i+".dot");

            ArrayList<Integer> tritopo = SeamCarving.iterativeTritopo(g2, width * height);

            ArrayList<Integer> ccm = SeamCarving.Bellman(g2, firstCase, lastCase, tritopo);
            picture = SeamCarving.deleteColumn(picture, ccm);
            if (i % 50 == 0 && !continu) {
                System.out.println("Reduce in process...");
            }
        }

        if (!continu) {
            SeamCarving.writepgm(picture, "" + pictureName + ".reduceWidthBy" + columns + ".pgm");
            System.out.println("\n  Reduce Width by " + columns + " on " + pictureName + " has finished ! \\o/");
        } else {
            SeamCarving.writepgm(picture, "res/" + pictureName + ".reducing.pgm");
        }
    }


    public static void reduceHeight(String pictureName, int lines, boolean continu) {
        int[][] picture = SeamCarving.readpgm(pictureName);
        int height = picture.length;
        int width = picture[0].length;

        int firstCase = height * width;
        int lastCase = firstCase + 1;

        for (int i = 0; i < lines; i++) {


            height = picture.length;
            width = picture[0].length;
            firstCase = height * width;
            lastCase = firstCase + 1;

            int[][] pix_interest = SeamCarving.interestWidth(picture);
            //Graph g = SeamCarving.tograph(pix_interest);

            GraphImplicitHeight g3 = new GraphImplicitHeight(pix_interest, pix_interest[0].length, pix_interest.length);

            //g.writeFile("test.dot");
            //g3.writeFile("testHeight_"+i+".dot");

            ArrayList<Integer> tritopo = SeamCarving.iterativeTritopo(g3, width * height);

            ArrayList<Integer> ccm = SeamCarving.Bellman(g3, firstCase, lastCase, tritopo);
            picture = SeamCarving.deleteLine(picture, ccm);
            if (i % 50 == 0 && !continu) {
                System.out.println("Reduce in process...");
            }
        }


        if (!continu) {
            SeamCarving.writepgm(picture, "" + pictureName + ".reduceHeightBy" + lines + ".pgm");

            System.out.println("\n  Reduce Height by " + lines + " on " + pictureName + " has finished ! \\o/");
        } else {
            SeamCarving.writepgm(picture, "res/" + pictureName + ".reducing.pgm");
        }
    }

    public static void reduce(String pictureName, int pixels) {

        String pictureNameReducing = pictureName;

        for (int pixel = 0; pixel < pixels; pixel++) {
            System.out.println(pixel);
            System.out.println(pictureNameReducing);

            reduceWidth(pictureNameReducing, 1, true);

            if (pictureNameReducing.equals(pictureName)) {
                pictureNameReducing = pictureName + ".reducing.pgm";
            }

            reduceHeight(pictureName, 1, true);

            if (pixel % 50 == 0) {
                System.out.println("Reduce in process...");
            }
        }

        //SeamCarving.writepgm(picture, "" + pictureName + ".reduceHeightBy" + lines + ".pgm");

        System.out.println("\n  Reduce by " + pixels + " on " + pictureName + " has finished ! \\o/");
    }

    public static void main(String[] args) {
        //testGraph();

        reduceWidth("bateau.pgm",300, false);
        reduceHeight("bateau.pgm",200, false);
        //reduce("bateau.pgm", 50);

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
