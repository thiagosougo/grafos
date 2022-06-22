import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Grafo {

    private double[][] matrizAdjacencia;
    private int quant; // numero de vertices

    // para dijkstra
    HashSet<Integer> visitados;
    double dist[];
    LinkedList<Integer> fila = new LinkedList<>();

    // auxiliar
    LinkedList<LinkedList<Integer>> listaAdjacentes = new LinkedList<>();

    Grafo(ArrayList<Cidade> cidades) {

        quant = cidades.size();
        dist = new double[quant];

        matrizAdjacencia = new double[quant][quant];

        for (int i = 0; i < quant; i++) {
            for (int j = i + 1; j < quant; j++) {
                matrizAdjacencia[i][j] = cidades.get(i).distancia(cidades.get(j));
                matrizAdjacencia[j][i] = matrizAdjacencia[i][j];
            }
        }

        // deixa somente as 3 menores distancias
        for (int i = 0; i < quant; i++) {
            for (int j = 4; j < quant; j++) {

                double maior = -1; // maior valor na linha i
                int posMaior = 0; // posicao do maior valor na linha i
                for (int k = 0; k < j; k++) {
                    if (matrizAdjacencia[i][j] < matrizAdjacencia[i][k]) {
                        if (maior < matrizAdjacencia[i][k]) {
                            maior = matrizAdjacencia[i][k];
                            posMaior = k;
                        }
                    }
                }
                if (maior == -1) {
                    matrizAdjacencia[i][j] = -1;
                } else {
                    matrizAdjacencia[i][posMaior] = -1;
                }
            }
        }

        // deixar o grafo simetrico depois da remoção de arestas
        // (se a cidade X tem aresta para a cidade Y, Y tem para X)
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                if (matrizAdjacencia[i][j] != -1) {
                    matrizAdjacencia[j][i] = matrizAdjacencia[i][j];
                }
            }
        }

        criarListaAdjacentes();
    }

    /**
     * Cria lista de vertices adjacentes para cada vertice do grafo
     */
    private void criarListaAdjacentes() {

        for (int i = 0; i < quant; i++) {
            listaAdjacentes.add(new LinkedList<>());
            for (int j = 0; j < quant; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    listaAdjacentes.get(i).add(j);
                }
            }
        }
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
                if (matrizAdjacencia[v][i] > 0) {
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
     * 
     * @param raiz
     * @param intermed
     * @return
     */
    public boolean encontrarCiclo(int raiz, int intermed) {
        int pais[] = new int[quant];
        Arrays.fill(pais, -1);
        visitados = new HashSet<>();

        return encontrarCiclo(raiz, raiz, intermed, pais);
    }

    /**
     * Chamada recursiva de encontrarCiclo()
     * 
     * @param v
     * @param raiz
     * @param intermed
     * @param pais
     * @param visitados
     * @return
     */
    private boolean encontrarCiclo(int v, int raiz, int intermed, int pais[]) {

        visitados.add(v);

        Iterator<Integer> i = listaAdjacentes.get(v).listIterator();
        while (i.hasNext()) {
            int n = i.next();

            if (n == raiz && pais[v] != n) {

                if (intermedEstaNoCiclo(intermed, v, pais)) {
                    printCiclo(pais, v);
                    return true;
                }

            } else if (!visitados.contains(n)) {
                pais[n] = v;
                boolean ciclo = encontrarCiclo(n, raiz, intermed, pais);
                if (ciclo)
                    return ciclo;
            }

        }
        return false;
    }

    private void printCiclo(int pais[], int fimCiclo) {
        System.out.println("Ciclo encontrado");
        StringBuilder sb = new StringBuilder();
        int k = fimCiclo;
        int raiz = k;
        while (k != -1) {
            sb.append(k + "\t");
            raiz = k;
            k = pais[k];
        }
        System.out.println(raiz + "\t" + sb.toString());
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

    /**
     * Caminho mínimo a partir do vértice raiz utilizando o algoritmo de Dijkstra
     * 
     * @param origem
     * @param destino
     * @return distancia mínima entre os 2 vertices fornecidos como parâmetro.
     *         Retorna -1 se não existir um caminho entre os vértices
     */
    public double caminhoMinimo(int origem, int destino) {

        visitados = new HashSet<>();

        // considerando -1 como distancia infinita
        for (int i = 0; i < quant; i++)
            dist[i] = -1;

        fila.add(origem);
        dist[origem] = 0;

        while (visitados.size() != quant) {

            if (fila.isEmpty()) {
                break;
            }
            int u = fila.remove();
            visitados.add(u);

            verificarVerticesAdjacentes(u);
        }

        return dist[destino];
    }

    /**
     * Auxiliar de Dijkstra
     * 
     * @param u Vértice atual
     */
    private void verificarVerticesAdjacentes(int u) {

        double novaDistancia = -1;

        for (int i = 0; i < listaAdjacentes.get(u).size(); i++) {
            int v = listaAdjacentes.get(u).get(i);

            if (!visitados.contains(v)) {
                novaDistancia = dist[u] + matrizAdjacencia[u][v];

                // se a nova distancia for menor, ela é atualizada
                if (novaDistancia < dist[v] || dist[v] == -1) {
                    dist[v] = novaDistancia;
                }

                if (!fila.contains(v)) {
                    fila.add(v);
                }
            }
        }
    }
}
