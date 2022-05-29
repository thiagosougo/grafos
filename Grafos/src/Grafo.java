import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    private double[][] matrizAdjacencia;
    private int quant; // numero de vertices

    Grafo(ArrayList<Cidade> cidades) {

        // quant = cidades.size();
        quant = 10;

        matrizAdjacencia = new double[quant][quant];

        for (int i = 0; i < quant; i++) {
            for (int j = i + 1; j < quant; j++) {
                matrizAdjacencia[i][j] = cidades.get(i).distancia(cidades.get(j));
                matrizAdjacencia[j][i] = matrizAdjacencia[i][j];
            }
        }

        printMatrizAdjacencia();

        // deixa somente as 3 menores distancias
        for (int i = 0; i < quant; i++) {
            for (int j = 4; j < quant; j++) {

                double maior = 0; // maior valor na linha i
                int posMaior = 0; // posicao do maior valor na linha i
                for (int k = 0; k < j; k++) {
                    if (matrizAdjacencia[i][j] < matrizAdjacencia[i][k]) {
                        if (maior < matrizAdjacencia[i][k]) {
                            maior = matrizAdjacencia[i][k];
                            posMaior = k;
                        }
                    }
                }
                if (maior == 0) {
                    matrizAdjacencia[i][j] = 0;
                } else {
                    matrizAdjacencia[i][posMaior] = 0;
                }
            }
        }

        // deixar o grafo simetrico depois da remoção de arestas
        // (se a cidade X tem aresta para a cidade Y, Y tem para X)
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                if (matrizAdjacencia[i][j] != 0) {
                    matrizAdjacencia[j][i] = matrizAdjacencia[i][j];
                }
            }
        }

        printMatrizAdjacencia();
    }

    /**
     * Busca em largura a partir do vértice 'raiz'
     * 
     * @param raiz
     */
    void buscaLargura(int raiz) {
        System.out.println("Busca em largura\n");

        boolean foiVisitado[] = new boolean[quant];
        Arrays.fill(foiVisitado, false);

        LinkedList<Integer> fila = new LinkedList<Integer>();

        foiVisitado[raiz] = true;
        fila.add(raiz);

        while (fila.size() != 0) {
            // remove o vertice explorado da fila
            raiz = fila.poll();
            System.out.print(raiz + " ");

            // lista de vertices adjacentes ao vertice atual
            LinkedList<Integer> listaAdjacentes = new LinkedList<Integer>();
            for (int i = 0; i < quant; i++) {
                if (matrizAdjacencia[raiz][i] != 0) {
                    listaAdjacentes.add(i);
                }
            }

            // lpercorre a lista de vertices adjacentes e encontra os nao visitados
            Iterator<Integer> i = listaAdjacentes.listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!foiVisitado[n]) {
                    foiVisitado[n] = true;
                    fila.add(n);
                }
            }
        }
    }

    public boolean encontrarCiclo(int raiz) {

        boolean visited[] = new boolean[quant];

        // Set parent vertex for every vertex as -1.
        int parent[] = new int[quant];
        Arrays.fill(parent, -1);

        // Create a queue for BFS
        Queue<Integer> q = new LinkedList<>();

        // Mark the current node as
        // visited and enqueue it
        visited[raiz] = true;
        q.add(raiz);

        while (!q.isEmpty()) {

            // Dequeue a vertex from queue and print it
            int u = q.poll();

            // lista de vertices adjacentes
            ArrayList<Integer> listaAdjacentes = new ArrayList<>();
            for (int i = 0; i < quant; i++) {
                if (matrizAdjacencia[u][i] != 0) {
                    listaAdjacentes.add(i);
                }
            }

            // Get all adjacent vertices
            // of the dequeued vertex u.
            // If a adjacent has not been
            // visited, then mark it visited
            // and enqueue it. We also mark parent
            // so that parent is not considered for cycle.
            for (int i = 0; i < listaAdjacentes.size(); i++) {
                int v = listaAdjacentes.get(i);
                if (!visited[v]) {
                    visited[v] = true;
                    q.add(v);
                    parent[v] = u;
                } else if (parent[u] != v)
                    return true;
            }
        }
        return false;
    }

    public double[][] getMatrizAdjacencia() {
        return matrizAdjacencia;
    }

    public void printMatrizAdjacencia() {
        System.out.println("GRAFO");
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                System.out.format("%.0f\t", matrizAdjacencia[i][j]);
            }
            System.out.println("\n");
        }
    }
}
