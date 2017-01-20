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
        //sa valeur sera donc : line * height + col

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {

                //sommet du bas au centre
                if ((line + 1) < height) {
                    g.addEdge(new Edge(
                            (line * height + col),
                            ((line + 1) * height + col),
                            itr[line][col]
                    ));
                }

                //sommet du bas à droite
                if ((line + 1) < height && (col + 1) < width) {
                    g.addEdge(new Edge(
                            (line * height + col),
                            ((line + 1) * height + (col + 1)),
                            itr[line][col]
                    ));
                }

                //sommet du bas à gauche
                if ((line + 1) < height && (col - 1) >= 0) {
                    g.addEdge(new Edge(
                            (line * height + col),
                            ((line + 1) * height + (col - 1)),
                            itr[line][col]
                    ));
                }
            }
        }


        //lien entre la première ligne et le depart du graphe
        for (int i = 0; i < width; i++) {
            g.addEdge(new Edge(
                    -1, //id du depart vaut -1 pour le differencier
                    i,
                    0 //poid à 0
            ));
        }

        //lien entre la dernière ligne et la fin du graphe
        for (int i = 0; i < width; i++) {
            g.addEdge(new Edge(
                    (height - 1) * width + i,
                    height * height + width, //aucun sommet ne peut avoir cette valeur, valeur maximale
                    itr[height - 1][i]
            ));
        }

        return g;
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
