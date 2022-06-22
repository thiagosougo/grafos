import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

public class App {

    // #region CONSTANTE
    private static final String NOME_ARQUIVO = "./../br.csv";
    // #endregion

    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        ArquivoLeitura f = new ArquivoLeitura(NOME_ARQUIVO);

        int quant = f.contarLinhas() - 1;
        f.fecharArq();

        ArrayList<Cidade> cidades = lerCidades(quant);
        Grafo grafo = new Grafo(cidades);

        int opcao;
        do {

            opcao = menu(teclado);

            switch (opcao) {
                case 1:
                    grafo.printMatrizAdjacencia();
                    break;
                case 2:
                    int raiz;
                    System.out.println("Digite a raiz: ");
                    try {
                        raiz = teclado.nextInt();
                        teclado.nextLine();
                        grafo.buscaLargura(raiz);
                    } catch (InputMismatchException ex) {
                        teclado.nextLine();
                        System.out.println("Somente opções numéricas.");
                    }
                    break;
                case 3:
                    int raizCiclo, intermedCiclo;

                    try {
                        System.out.println("Digite a raiz: ");
                        raizCiclo = teclado.nextInt();
                        teclado.nextLine();
                        System.out.println("Digite o intermediario: ");
                        intermedCiclo = teclado.nextInt();
                        teclado.nextLine();
                        if (!grafo.encontrarCiclo(raizCiclo, intermedCiclo)) {
                            System.out.println("Ciclo nao encontrado");
                        }
                    } catch (InputMismatchException ex) {
                        teclado.nextLine();
                        System.out.println("Somente opções numéricas.");
                    }
                    break;
                case 4:
                    int raizCMin, destino;

                    try {
                        System.out.println("Digite a raiz: ");
                        raizCMin = teclado.nextInt();
                        teclado.nextLine();
                        System.out.println("Digite o destino: ");
                        destino = teclado.nextInt();
                        teclado.nextLine();
                        double caminhoMinimo = grafo.caminhoMinimo(raizCMin, destino);
                        if (caminhoMinimo == -1) {
                            System.out.println("Caminho nao encontrado");
                        } else {
                            System.out.format("Caminho minimo: %.0f\n", caminhoMinimo);
                        }

                    } catch (InputMismatchException ex) {
                        teclado.nextLine();
                        System.out.println("Somente opções numéricas.");
                    }
                    break;
                default:
                    break;

            }
            pausa(teclado);
        } while (opcao != 0);
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

    public static int menu(Scanner teclado) {
        System.out.println("======== Menu =======");
        System.out.println("1 - Imprimir matriz de adjacência (considerando -1 como distancia infinita)");
        System.out.println("2 - Busca em largura");
        System.out.println("3 - Encontrar ciclo");
        System.out.println("4 - Encontrar caminho minimo");
        System.out.println("0 - Sair");
        int opcao = 0;
        try {
            opcao = teclado.nextInt();
            teclado.nextLine();
        } catch (InputMismatchException ex) {
            teclado.nextLine();
            System.out.println("Somente opções numéricas.");
            opcao = -1;
        }
        return opcao;
    }

    private static void pausa(Scanner teclado) {
        System.out.println("Enter para continuar.");
        teclado.nextLine();
    }
}
