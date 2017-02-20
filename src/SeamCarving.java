import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving {


    public static void writepgm(int[][] image, String filename) {
        File f = new File(filename);
        int height = image.length;
        int width = image[0].length;
        int buff = 0;

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

            //entete
            pw.println("P2");
            pw.println(width + " " + height);
            pw.println("256");

            //corps de l'image
            for (int line = 0; line < height; line++) {
                for (int col = 0; col < width; col++) {
                    pw.print(image[line][col] + " ");
                    buff++;

                    if (buff < 71) { //limite par de pixel par ligne : 70
                        pw.print("\n");
                    }
                }
            }

            pw.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture : " + e.getMessage());
        }

    }


    public static int[][] interest(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] res = new int[height][width];
        int val;

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {


                //deux voisins => difference avec la moyenne des deux
                if ((col - 1) >= 0 && (col + 1) < width) {
                    int moy = (image[line][col - 1] + image[line][col + 1]) / 2;

                    val = Math.abs(image[line][col] - moy);
                }

                //pas de voisin a droite => difference avec celui de gauche
                else if ((col + 1) >= width) {
                    val = Math.abs(image[line][col] - image[line][col - 1]);
                }

                //pas de voisin a gauche => difference avec celui de droite
                else {
                    val = Math.abs(image[line][col] - image[line][col + 1]);
                }

                res[line][col] = val;
            }
        }

        return res;
    }


    public static Graph tograph(int[][] itr) {
        int height = itr.length;
        int width = itr[0].length;


        GraphArrayList g = new GraphArrayList((height * width) + 2); //+2 pour le depart et l'arrive

        //pour eviter que deux sommets aient la meme valeur, comme itr[16][64] et itr[64][16],
        //sa valeur sera donc : line * width + col

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {

                //sommet du bas au centre
                if ((line + 1) < height) {
                    g.addEdge(new Edge(
                            (line * width + col),
                            ((line + 1) * width + col),
                            itr[line][col]
                    ));
                }

                //sommet du bas à droite
                if ((line + 1) < height && (col + 1) < width) {
                    g.addEdge(new Edge(
                            (line * width + col),
                            ((line + 1) * width + (col + 1)),
                            itr[line][col]
                    ));
                }

                //sommet du bas à gauche
                if ((line + 1) < height && (col - 1) >= 0) {
                    g.addEdge(new Edge(
                            (line * width + col),
                            ((line + 1) * width + (col - 1)),
                            itr[line][col]
                    ));
                }
            }
        }


        //lien entre la première ligne et le depart du graphe
        for (int i = 0; i < width; i++) {
            g.addEdge(new Edge(
                    height * width, //id du depart height * width car personne n'a cette valeur
                    i,
                    0 //poid à 0
            ));
        }

        //lien entre la dernière ligne et la fin du graphe
        for (int i = 0; i < width; i++) {
            g.addEdge(new Edge(
                    (height - 1) * width + i,
                    height * width + 1, //id du depart + 1
                    itr[height - 1][i]
            ));
        }

        return g;
    }

    public static void dfs(Graph g, int u, boolean[] visite, ArrayList<Integer> al) {

        visite[u] = true;
        for (Edge e : g.next(u)) {
            if (!visite[e.to]) {
                dfs(g, e.to, visite, al);
            }
        }

        al.add(u);
        System.out.println("Je visite " + u);
    }

    public static ArrayList<Integer> tritopo(Graph g) {

        ArrayList<Integer> al = new ArrayList<Integer>();

        int n = g.vertices();
        //System.out.println("Vertices = "+n);
        boolean[] visite = new boolean[n];

        dfs(g, n - 2, visite, al);

        Collections.reverse(al);

        /*System.out.println("Tri topo : ");
        for (int x : al) {
            System.out.println("    " + x);
        }*/

        return al;
    }

    public static ArrayList<Integer> iterativeTritopo(Graph g, int s) {

        ArrayList<Integer> alSuffixe = new ArrayList<>();
        Iterator<Edge> iteratorEdge;

        boolean visited[] = new boolean[g.vertices()];

        Stack<Integer> stackSommet = new Stack<Integer>(); //pile de sommet
        Stack<Iterator<Edge>> stackIt = new Stack<Iterator<Edge>>(); //pile d'iterateur de voisins

        //ajout dans les piles de s et next(s)
        stackSommet.push(s);
        stackIt.push(g.next(s).iterator());

        int sommet;


        while (!stackSommet.empty()) {

            //recupere la paire sommet, it
            sommet = stackSommet.peek();
            iteratorEdge = stackIt.peek();

            //si voisin de u non teste
            if (iteratorEdge.hasNext()) {
                int voisin = iteratorEdge.next().to;

                //si voisin non visite
                if (!visited[voisin]) {

                    visited[voisin] = true; //v ajoute au sommet visite

                    //ajoute la paire voisin, next(voisin) a la pile
                    stackSommet.push(voisin);
                    stackIt.push(g.next(voisin).iterator());
                }

                //sinon (tous les voisins visites) : retour au de debut de boucle
            }



            else {

                //sinon on retire la paire sommet, it des piles
                stackSommet.pop();
                stackIt.pop();

                //ajoute le sommet a la liste suffixe
                alSuffixe.add(sommet);
            }
        }

        // On prend l'inverse de l'orde suffixe
        Collections.reverse(alSuffixe);

        /*System.out.println("Tri topo : ");
        for (int x : al) {
            System.out.println("    " + x);
        }*/

        return alSuffixe;
    }

    public static ArrayList<Integer> Bellman(Graph g, int s, int t, ArrayList<Integer> order) {

        //initialisation du tableau de distances avec une valeur maximale
        Integer[] distances = new Integer[g.vertices()];
        Arrays.fill(distances, Integer.MAX_VALUE / 2);
        //et sa premiere case a 0
        distances[order.get(0)] = 0;

        //initialisation du tableau des parents avec -1 car aucun point n'a cette valeur
        Integer[] parents = new Integer[g.vertices()];
        Arrays.fill(parents, -1);

        //Integer point;


        //on parcourt chaque point du tri topo
        for (Integer point : order) {

            //on parcourt chaque arete de ce point
            for (Edge e : g.prev(point)) {

                int a = distances[e.getFrom()] + e.getCost();

                //si cette nouvelle valeur est plus petite que celle deja enregistree
                //on met a jour le tableau et on l'ajoute en tant que parent
                if ((distances[e.getFrom()] + e.getCost()) < distances[point]) {
                    distances[point] = distances[e.getFrom()] + e.getCost();
                    parents[point] = e.getFrom();
                }
            }
        }

        ArrayList<Integer> result = new ArrayList<>(g.vertices());

        //initialisation du dernier point du CCM
        Integer parcours = parents[g.vertices() - 1];

        //si le point a parcrourir = -1, alors fin du CCM, sinon on ajoute ce point a la liste
        while (parcours != -1) {
            result.add(parcours);
            parcours = parents[parcours];
        }

        result.remove(result.size() - 1);//On enleve le dernier point factice


        Collections.reverse(result);

        return result;
    }

    public static int[][] deleteColumn(int[][] picture, ArrayList<Integer> list) {
        //une colonne en moins pour la nouvelle image
        int[][] result = new int[picture.length][picture[0].length - 1];
        int height = picture.length;
        int width = picture[0].length;

        // On itère sur les deux tableaux en même temps.
        // Pour itérer sur les lignes, on utilise deux variables différentes
        // Si le pixel courant est dans le chemin le plus court, on incrémente une seule des deux variables
        for (int line = 0; line < height; line++) {
            boolean finded = false;
            for (int col = 0; col < width; col++) {
                if (!list.contains(line * width + col)) {
                    if (finded) {
                        result[line][col - 1] = picture[line][col];
                    } else {
                        result[line][col] = picture[line][col];
                    }

                } else {
                    finded = true;
                    //System.out.println("FIND");
                }
            }
        }

        return result;
    }


    public static int[][] readpgm(String fn) {
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();

            String line = d.readLine();
            while (line.startsWith("#")) {
                line = d.readLine();
            }
            Scanner s = new Scanner(line);
            int width = s.nextInt();
            int height = s.nextInt();
            line = d.readLine();
            s = new Scanner(line);
            int maxVal = s.nextInt();
            int[][] im = new int[height][width];
            s = new Scanner(d);
            int count = 0;
            while (count < height * width) {
                im[count / width][count % width] = s.nextInt();
                count++;
            }
            return im;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return null;
        }
    }


}
