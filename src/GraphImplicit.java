import java.util.ArrayList;

class GraphImplicit implements Graph {

    int[][] interest;
    int width;
    int height;
    int N;


    GraphImplicit(int[][] interest, int width, int height) {

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
            for (int i = 0; i < width; i++) {
                edges.add(new Edge(v, i, 0));
            }
        } else {
            if (v >= width * height + 1) { //dernier noeud
                //ne rien faire
            } else {
                if (v >= (height - 1) * width && v < width * height) { //derniere ligne

                    line = height - 1;
                    column = v % width;
                    edges.add(new Edge(v, height * width + 1, interest[line][column]));
                } else { //cas general


                    line = v / width;
                    column = v % width;

                    if (column == 0) { //bord gauche
                        edges.add(new Edge(v, v + width, interest[line][column]));
                        edges.add(new Edge(v, v + width + 1, interest[line][column]));
                    }

                    if (column == width - 1) { //bord droit
                        edges.add(new Edge(v, v + width - 1, interest[line][column]));
                        edges.add(new Edge(v, v + width, interest[line][column]));
                    }

                    if (column > 0 && column < width - 1) { //au milieu

                        edges.add(new Edge(v, v + width - 1, interest[line][column]));
                        edges.add(new Edge(v, v + width, interest[line][column]));
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
                for (int i = 0; i < width; i++) {
                    //System.out.println("Line : " + (v - width - 1 + i) / width);
                    //System.out.println("Col: " + (v - width - 1 + i) % width + "\n");
                    edges.add(new Edge(v - width - 1 + i, v, interest[(v - width - 1 + i) / width][(v - width - 1 + i) % width]));
                }
            } else {
                if (v >= 0 && v < width) { //premiere ligne
                    edges.add(new Edge(width * height, v, 0));


                } else { //cas general


                    if (column == 0) { //bord gauche
                        edges.add(new Edge(v - width, v, interest[(v - width) / width][(v - width) % width]));
                        edges.add(new Edge(v - width + 1, v, interest[(v - width + 1) / width][(v - width + 1) % width]));

                    }

                    if (column == width - 1) { //bord droit
                        edges.add(new Edge(v - width - 1, v, interest[(v - width - 1) / width][(v - width - 1) % width]));
                        edges.add(new Edge(v - width, v, interest[(v - width) / width][(v - width) % width]));

                    }

                    if (column > 0 && column < width - 1) { //au milieu
                        edges.add(new Edge(v - width - 1, v, interest[(v - width - 1) / width][(v - width - 1) % width]));
                        edges.add(new Edge(v - width, v, interest[(v - width) / width][(v - width) % width]));
                        edges.add(new Edge(v - width + 1, v, interest[(v - width + 1) / width][(v - width + 1) % width]));

                    }
                }
            }
        }

        return edges;
    }
}
