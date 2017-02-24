import java.util.ArrayList;

class GraphImplicitHeight implements Graph {

    int[][] interest;
    int width;
    int height;
    int N;


    GraphImplicitHeight(int[][] interest, int width, int height) {

        this.interest = interest;
        this.width = width;
        this.height = height;
        this.N = height * width + 2;


    }

    public int vertices() {
        return N;
    }


    public Iterable<Edge> next(int v) { //v = numero sommet
        ArrayList<Edge> edges = new ArrayList();
        int line, column;


        if (v == width * height) { //premier noeud
            for (int i = 0; i < height; i++) {
                edges.add(new Edge(v, i * width, 0));
            }
        } else {
            if (v >= width * height + 1) { //dernier noeud
                //ne rien faire
            } else {
                if ((v + 1) % width == 0) { //derniere colonne

                    line = v / width;
                    column = width - 1;
                    edges.add(new Edge(v, height * width + 1, interest[line][column]));
                } else { //cas general


                    line = v / width;
                    column = v % width;

                    if (line == 0) { //bord haut
                        edges.add(new Edge(v, v + width + 1, interest[line][column]));
                        edges.add(new Edge(v, v + 1, interest[line][column]));
                    }

                    if (line == height - 1) { //bord bas
                        edges.add(new Edge(v, v + 1, interest[line][column]));
                        edges.add(new Edge(v, v - width + 1, interest[line][column]));
                    }

                    if (line > 0 && line < height - 1) { //au milieu

                        edges.add(new Edge(v, v - width + 1, interest[line][column]));
                        edges.add(new Edge(v, v + 1, interest[line][column]));
                        edges.add(new Edge(v, v + width + 1, interest[line][column]));
                    }
                }
            }
        }


        return edges;

    }


    public Iterable<Edge> prev(int v) {
        ArrayList<Edge> edges = new ArrayList();
        int line, column;

        line = v / width;
        column = v % width;


        if (v == width * height) { //premier noeud
            //pas de prev
        } else {
            if (v == width * height + 1) { //dernier noeud
                for (int i = 0; i < height; i++) {
                    edges.add(new Edge(v - 2 - (i * width), v, interest[(v - 2 - (i * width)) / width][(v - 2 - (i * width)) % width]));
                }
            } else {
                if (v % width == 0) { //premiere colonne
                    edges.add(new Edge(width * height, v, 0));


                } else { //cas general


                    if (line == 0) { //bord haut
                        edges.add(new Edge(v - 1, v, interest[(v - 1) / width][(v - 1) % width]));
                        edges.add(new Edge(v + width - 1, v, interest[(v + width - 1) / width][(v + width - 1) % width]));

                    }

                    if (line == height - 1) { //bord bas
                        edges.add(new Edge(v - 1, v, interest[(v - 1) / width][(v - 1) % width]));
                        edges.add(new Edge(v - width - 1, v, interest[(v - width - 1) / width][(v - width - 1) % width]));

                    }

                    if (line > 0 && line < height - 1) { //au milieu

                        edges.add(new Edge(v - width - 1, v, interest[(v - width - 1) / width][(v - width - 1) % width]));
                        edges.add(new Edge(v - 1, v, interest[(v - 1) / width][(v - 1) % width]));
                        edges.add(new Edge(v + width - 1, v, interest[(v + width - 1) / width][(v + width - 1) % width]));

                    }
                }
            }
        }

        return edges;
    }
}
