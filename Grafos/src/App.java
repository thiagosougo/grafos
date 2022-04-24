import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class App {

    // #region CONSTANTE
    private static final String NOME_ARQUIVO = "./br.csv";
    // #endregion

    public static void main(String[] args) throws IOException {

        ArquivoLeitura f = new ArquivoLeitura(NOME_ARQUIVO);

        int quant = f.contarLinhas() - 1;
        Cidade cidades[] = new Cidade[quant];
        f.fecharArq();

        f = new ArquivoLeitura(NOME_ARQUIVO);
        f.lerLinha();

        for (int i = 0; i < quant; i++) {
            String linha = f.lerLinha();

            String[] dadosCidade = linha.split(",");
            cidades[i] = new Cidade(dadosCidade[0],
                    Double.parseDouble(dadosCidade[1]),
                    Double.parseDouble(dadosCidade[2]));
        }

        f.fecharArq();

        FileWriter arq = new FileWriter("distancias.txt");
        PrintWriter gravarArq = new PrintWriter(arq);

        for (int i = 0; i < (cidades.length); i++) {
            gravarArq.printf("------------------------------------Id cidade: " + i + " | Nome da cidade: "
                    + cidades[i].getNome());
            for (int j = 0; j < (cidades.length); j++) {
                gravarArq.printf("\nDistancia entre as cidades " + cidades[i].getNome() + " e " + cidades[j].getNome()
                        + " é " + cidades[i].distancia(cidades[j]));
            }
            gravarArq.printf("\n\n");
        }
        arq.close();


    }
}
