import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Labirinto {
    private int m, n;
    private char[][] labirinto;
    private boolean[][] visitado;

    // Mapeamento de letras para raças
private static final Map<Character, String> MAPA_RACAS = new HashMap<>();

static {
    MAPA_RACAS.put('A', "Anão");
    MAPA_RACAS.put('B', "Elfo");
    MAPA_RACAS.put('C', "Troll");
    MAPA_RACAS.put('D', "Dragão");
    MAPA_RACAS.put('E', "Orc");
    MAPA_RACAS.put('F', "Goblin");
    MAPA_RACAS.put('G', "Gnomo");
    MAPA_RACAS.put('H', "Humano");
    // Adicione outras letras e raças conforme necessário
}

    public Labirinto(int m, int n, char[][] labirinto) {
        this.m = m;
        this.n = n;
        this.labirinto = labirinto;
        this.visitado = new boolean[m][n];
    }

    public void contarRegioes() {
        Map<String, Integer> frequencias = new HashMap<>();
        int numRegioes = 0;

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
            }
        }

        System.out.println("Número de regiões isoladas: " + numRegioes);
        String serMaisFrequente = frequencias.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        System.out.println("Raça mais frequente em todas as regiões: " + serMaisFrequente);
    }

    private void dfs(int i, int j, Map<String, Integer> contador) {
        if (i < 0 || i >= m || j < 0 || j >= n || visitado[i][j] || labirinto[i][j] == '#') {
            return;
        }

        visitado[i][j] = true;
        char tipoSer = labirinto[i][j];

        if (Character.isUpperCase(tipoSer)) {
            String raca = MAPA_RACAS.getOrDefault(tipoSer, "Desconhecido");
            contador.put(raca, contador.getOrDefault(raca, 0) + 1);
        }

        if (!temParede(i, j, 'U')) dfs(i - 1, j, contador); // Cima
        if (!temParede(i, j, 'R')) dfs(i, j + 1, contador); // Direita
        if (!temParede(i, j, 'D')) dfs(i + 1, j, contador); // Baixo
        if (!temParede(i, j, 'L')) dfs(i, j - 1, contador); // Esquerda
    }

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
        String caminhoArquivo = "caso40_2.txt"; // Atualize para o arquivo desejado

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
