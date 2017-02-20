import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

class DFS {

    public static void botched_dfs1(Graph g, int s) {
        Stack<Integer> stack = new Stack<Integer>();
        boolean visited[] = new boolean[g.vertices()];
        stack.push(s);
        visited[s] = true;
        while (!stack.empty()) {
            int u = stack.pop();
            System.out.println(u);
            for (Edge e : g.next(u))
                if (!visited[e.to]) {
                    visited[e.to] = true;
                    stack.push(e.to);
                }
        }
    }

    /*
    * Affiche dans l'ordre ou il a push les sommets dans la stack
    *
    * Il push dans l'ordre ou si le fils n'a pas ete visite, alors il le push
    *
    * Ici, il depile 0 et l'affiche, et push 1, 2 , 3
    *
    * Puis depile 3 et l'affiche, et push 5.
    * Alors que 3 n'a pas a etre affiche
    *
    * */

    public static void botched_dfs2(Graph g, int s) {
        Stack<Integer> stack = new Stack<Integer>();
        boolean visited[] = new boolean[g.vertices()];
        stack.push(s);
        System.out.println(s);
        visited[s] = true;
        while (!stack.empty()) {
            int u = stack.pop();
            for (Edge e : g.next(u))
                if (!visited[e.to]) {
                    System.out.println(e.to);
                    visited[e.to] = true;
                    stack.push(e.to);
                }
        }
    }

    /*
    * Il affiche a chaque fois tous les sommets fils  non visites du sommet actuel
    * c'est a dire quasiment dans l'ordre.
    *
    * Il depile 0 et push 1, l'affiche, et fais pareil pour 2 et 3.
    * alors qu'il n'a pas a afficher 3.
    *
    * Le sommet a etre depile est 3 donc
    * il push 5 et l'affiche et etc.
    *
    * */


    public static void botched_dfs3(Graph g, int s) {
        int cpt = 0;

        Stack<Integer> stack = new Stack<Integer>();
        boolean visited[] = new boolean[g.vertices()];
        stack.push(s);
        while (!stack.empty()) {
            System.out.println("Capacité : " + stack.capacity());
            int u = stack.pop();
            if (!visited[u]) {
                visited[u] = true;
                System.out.println(u);
                for (Edge e : g.next(u))
                    if (!visited[e.to]) {
                        stack.push(e.to);
                        cpt++;
                    }
            }
        }
    }

    /*
    * Etant donne que l'on ne met pas visite[s] = true a tous les sommets
    * une fois push dans la stack, on risque de les rajouter une nouvelle fois
    * si un autre sommet pointe vers lui puisqu'il ne sera toujours pas considere comme visite.
    *
    *
    * Exemple d'un graphe à 100 sommets :
    *
    * On depile 0, on met visite[0]=true,
    * 0 est lie de 1 à 99 donc on push de 1 à 99
    *
    * on depile 99, on met visite[99]=true,
    * mais il est lié de 1 à 98 donc on push de 1 à 98
    *
    * ici on a donc des doublons de 1 à 98.
    *
    * on depile 98, on met visite[98]=true,
    * mais il est lié de 1 à 97 donc on push de 1 à 97
    *
    * ici on a donc trois occurences de 1 à 97 dans la pile.
    *
    * etc etc
    *
    * Ceci montre bien que l'on peut facilement depasser la limite de la pile
    *
    * */


    public static void botched_dfs4(Graph g, int s) {
        Stack<Integer> stack = new Stack<Integer>();
        boolean visited[] = new boolean[g.vertices()];
        stack.push(s);
        visited[s] = true;
        System.out.println(s);
        while (!stack.empty()) {
            boolean end = true;
        /* (a) Soit u le sommet en haut de la pile */
        /* (b) Si u a un voisin non visité, alors */
	    /*     (c) on le visite et on l'ajoute sur la pile */
	    /* Sinon */
	    /*     (d) on enlève u de la pile */
	   
	    /* (a) */
            int u = stack.peek();
            for (Edge e : g.next(u))
                if (!visited[e.to]) /* (b) */ {
                    visited[e.to] = true;
                    System.out.println(e.to);
                    stack.push(e.to); /*(c) */
                    end = false;
                    break;
                }
            if (end) /*(d)*/
                stack.pop();
        }
        System.out.println("Capacité : " + stack.capacity());
    }


    public static void testGraph() {
        int n = 5;
        int i, j;
        GraphArrayList g = new GraphArrayList(6);
        g.addEdge(new Edge(0, 1, 1));
        g.addEdge(new Edge(0, 2, 1));
        g.addEdge(new Edge(0, 3, 1));
        g.addEdge(new Edge(1, 4, 1));
        g.addEdge(new Edge(4, 3, 1));
        g.addEdge(new Edge(3, 5, 1));
        g.addEdge(new Edge(5, 1, 1));


        g.writeFile("testDFS.dot");


        boolean Bonvisited[] = new boolean[g.vertices()];
        ArrayList<Integer> al = new ArrayList<Integer>();

        System.out.println("Bon :");
        SeamCarving.dfs(g, 0, Bonvisited, al);

        System.out.println("\n\nBotched DFS1\n");
        botched_dfs1(g, 0);
        System.out.println("\n\nBotched DFS2\n");
        botched_dfs2(g, 0);
        System.out.println("\n\nBotched DFS3\n");
        botched_dfs3(g, 0);
        System.out.println("\n\nBotched DFS4\n");
        botched_dfs4(g, 0);

        System.out.println("\n\nGood Iterative DFS\n");


    }

    public static void main(String[] args) {
        testGraph();
    }
}
