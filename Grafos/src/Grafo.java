import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    private double[][] matrizAdjacencia;
    private int quant; // numero de vertices

    Grafo(ArrayList<Cidade> cidades) {

        quant = cidades.size();

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
        System.out.println("Busca em largura a partir de " + raiz);

        boolean foiVisitado[] = new boolean[quant];
        Arrays.fill(foiVisitado, false);

        LinkedList<Integer> fila = new LinkedList<Integer>();

        foiVisitado[raiz] = true;
        fila.add(raiz);

        while (fila.size() != 0) {
            // remove o vertice explorado da fila
            int v = fila.poll();
            System.out.print(v + "\t");

            // lista de vertices adjacentes ao vertice atual
            LinkedList<Integer> listaAdjacentes = new LinkedList<Integer>();
            for (int i = 0; i < quant; i++) {
                if (matrizAdjacencia[v][i] != 0) {
                    listaAdjacentes.add(i);
                }
            }

            // percorre a lista de vertices adjacentes e encontra os nao visitados
            Iterator<Integer> i = listaAdjacentes.listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!foiVisitado[n]) {
                    foiVisitado[n] = true;
                    fila.add(n);
                }
            }
        }
        System.out.println();
    }

    /**
     * Verifica se existe um ciclo que contém os vértices raiz e intermed
     * @param raiz
     * @param intermed
     * @return
     */
    boolean encontrarCiclo(int raiz, int intermed) {
        int pais[] = new int[quant];
        Arrays.fill(pais, -1);
        boolean visited[] = new boolean[quant];

        return encontrarCiclo(raiz, raiz, intermed, pais, visited);
    }

    /**
     * Chamada recursiva de encontrarCiclo()
     * @param v
     * @param raiz
     * @param intermed
     * @param pais
     * @param visited
     * @return
     */
    boolean encontrarCiclo(int v, int raiz, int intermed, int pais[], boolean visited[]) {

        visited[v] = true;

        // lista de vertices adjacentes ao vertice atual
        LinkedList<Integer> listaAdjacentes = new LinkedList<Integer>();
        for (int i = 0; i < quant; i++) {
            if (matrizAdjacencia[v][i] != 0) {
                listaAdjacentes.add(i);
            }
        }

        Iterator<Integer> i = listaAdjacentes.listIterator();
        while (i.hasNext()) {
            int n = i.next();

            if (n == raiz && pais[v] != n) {

                if (intermedEstaNoCiclo(intermed, v, pais)) {
                    printCiclo(pais, v);
                    return true;
                }

            } else if (!visited[n]) {
                pais[n] = v;
                boolean ciclo = encontrarCiclo(n, raiz, intermed, pais, visited);
                if (ciclo)
                    return ciclo;
            }

        }
        return false;
    }

    private void printCiclo(int pais[], int fimCiclo) {
        System.out.println("Ciclo encontrado");
        int k = fimCiclo;
        while (k != -1) {
            System.out.print(k + "\t");
            k = pais[k];
        }
        System.out.println();

    }

    private boolean intermedEstaNoCiclo(int intermed, int fimCiclo, int pais[]) {
        int k = fimCiclo;
        while (k != -1) {
            if (k == intermed) {
                return true;
            }
            k = pais[k];
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
