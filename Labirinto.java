import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Labirinto {
    private int m, n;
    private char[][] labirinto;
    private boolean[][] visitado;

    // Construtor que inicializa o labirinto e os dados necessários-gau
    public Labirinto(int m, int n, char[][] labirinto) {
        this.m = m;
        this.n = n;
        this.labirinto = labirinto;
        this.visitado = new boolean[m][n];
    }

    // Função para realizar a busca de regiões e contar os seres em cada região-bruno
    public void contarRegioes() {
        Map<String, Integer> frequencias = new HashMap<>();
        int numRegioes = 0;
        int contadorDeCiclos = 0; 

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!visitado[i][j]) {
                    Map<String, Integer> contador = new HashMap<>();
                    dfs(i, j, contador);
                    numRegioes++;

                    for (Map.Entry<String, Integer> entry : contador.entrySet()) {
                        frequencias.put(entry.getKey(), frequencias.getOrDefault(entry.getKey(), 0) + entry.getValue());
                    }
                }
                contadorDeCiclos++;
                if (contadorDeCiclos > m * n) {
                    System.out.println("Loop infinito detectado. Abortando...");
                    return;
                }
            }
        }

        System.out.println("Número de regiões isoladas: " + numRegioes);
        String serMaisFrequente = frequencias.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        System.out.println("Ser mais frequente em todas as regiões: " + serMaisFrequente);
    }

    // Função DFS que explora uma região e conta os seres-gau
    private void dfs(int i, int j, Map<String, Integer> contador) {
        if (i < 0 || i >= m || j < 0 || j >= n || visitado[i][j] || labirinto[i][j] == '#') {
            return;
        }

        visitado[i][j] = true;
        String tipoSer = String.valueOf(labirinto[i][j]);

        if (Character.isUpperCase(labirinto[i][j])) {
            contador.put(tipoSer, contador.getOrDefault(tipoSer, 0) + 1);
        }

        if (!temParede(i, j, 'U')) dfs(i - 1, j, contador); // Cima
        if (!temParede(i, j, 'R')) dfs(i, j + 1, contador); // Direita
        if (!temParede(i, j, 'D')) dfs(i + 1, j, contador); // Baixo
        if (!temParede(i, j, 'L')) dfs(i, j - 1, contador); // Esquerda
    }

    // Função que verifica se existe parede em uma direção específica com base no código hexadecimal-bruno
    private boolean temParede(int i, int j, char direcao) {
        try {
            int codigo = Character.digit(labirinto[i][j], 16);
            switch (direcao) {
                case 'U': return (codigo & 8) != 0;
                case 'R': return (codigo & 4) != 0;
                case 'D': return (codigo & 2) != 0;
                case 'L': return (codigo & 1) != 0;
                default: return false;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar parede na posição: (" + i + ", " + j + ")");
            return true;
        }
    }

    public static void main(String[] args) {
        String caminhoArquivo = "caso120_2.txt"; // Atualize para o arquivo desejado-(ajuda)

        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            String[] dimensoes = linhas.get(0).split(" ");
            int m = Integer.parseInt(dimensoes[0]);
            int n = Integer.parseInt(dimensoes[1]);

            char[][] labirinto = new char[m][n];
            for (int i = 0; i < m; i++) {
                labirinto[i] = linhas.get(i + 1).replace(" ", "").toCharArray();
            }

            Labirinto lab = new Labirinto(m, n, labirinto);
            lab.contarRegioes();

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
