import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class App {

    // #region CONSTANTE
    private static final String NOME_ARQUIVO = "./../br.csv";
    // #endregion

    public static void main(String[] args) throws IOException {
        ArquivoLeitura f = new ArquivoLeitura(NOME_ARQUIVO);

        int quant = f.contarLinhas() - 1;
        f.fecharArq();

        ArrayList<Cidade> cidades = lerCidades(quant);

        Grafo grafo = new Grafo(cidades);
        grafo.buscaLargura(0);

        // FileWriter arq = new FileWriter("distancias.txt");
        // PrintWriter gravarArq = new PrintWriter(arq);

        // for (int i = 0; i < (cidades.length); i++) {
        // gravarArq.printf("------------------------------------Id cidade: " + i + " |
        // Nome da cidade: "
        // + cidades[i].getNome());
        // for (int j = 0; j < (cidades.length); j++) {
        // gravarArq.printf("\nDistancia entre as cidades " + cidades[i].getNome() + " e
        // " + cidades[j].getNome()
        // + " é " + cidades[i].distancia(cidades[j]));
        // }
        // gravarArq.printf("\n\n");
        // }
        // arq.close();

    }

    private static ArrayList<Cidade> lerCidades(int quant) {
        ArquivoLeitura f = new ArquivoLeitura(NOME_ARQUIVO);
        ArrayList<Cidade> cidades = new ArrayList<Cidade>();

        f = new ArquivoLeitura(NOME_ARQUIVO);
        f.lerLinha();

        for (int i = 0; i < quant; i++) {
            String linha = f.lerLinha();

            String[] dadosCidade = linha.split(",");
            cidades.add(new Cidade(dadosCidade[0],
                    Double.parseDouble(dadosCidade[1]),
                    Double.parseDouble(dadosCidade[2])));
        }

        f.fecharArq();
        return cidades;
    }

    private static double[][] criarGrafo(ArrayList<Cidade> cidades) {

        int quant = cidades.size();

        double grafo[][] = new double[quant][quant];

        for (int i = 0; i < quant; i++) {
            for (int j = i + 1; j < quant; j++) {
                grafo[i][j] = cidades.get(i).distancia(cidades.get(j));
                grafo[j][i] = grafo[i][j];
            }
        }
        System.out.println("GRAFO");
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                System.out.format("%.0f\t", grafo[i][j]);
            }
            System.out.println("\n");
        }

        // deixa somente as 3 menores distancias
        for (int i = 0; i < quant; i++) {
            for (int j = 4; j < quant; j++) {

                double maior = 0; // maior valor na linha i
                int posMaior = 0; // posicao do maior valor na linha i
                for (int k = 0; k < j; k++) {
                    if (grafo[i][j] < grafo[i][k]) {
                        if (maior < grafo[i][k]) {
                            maior = grafo[i][k];
                            posMaior = k;
                        }
                    }
                }
                if (maior == 0) {
                    grafo[i][j] = 0;
                } else {
                    grafo[i][posMaior] = 0;
                }
            }
        }

        // deixar o grafo simetrico depois da remoção de arestas
        // (se a cidade X tem aresta para a cidade Y, Y tem para X)
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                if (grafo[i][j] != 0) {
                    grafo[j][i] = grafo[i][j];
                }
            }
        }

        System.out.println("GRAFO REDUZIDO");
        for (int i = 0; i < quant; i++) {
            for (int j = 0; j < quant; j++) {
                System.out.format("%.0f\t", grafo[i][j]);
            }
            System.out.println("\n");
        }

        return grafo;
    }
}
