import java.io.*;
import java.util.*;

public class Labirinto {
    static int[][] direcoes = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    static String[] seres = {"Anão", "Bruxa", "Cavaleiro", "Duende", "Elfo", "Feijão"};
    static char[] seresHex = {'A', 'B', 'C', 'D', 'E', 'F'};
    
    public static void main(String[] args) throws IOException {
        // gau faz o começo lendo o arquivo e separo os casos de teste. 
        BufferedReader br = new BufferedReader(new FileReader("caso80_2.txt"));
        String linha = br.readLine();
        
        while (linha != null) {
            // Primeiro pego as dimensões do labirinto pra criar a matriz.
            String[] dimensoes = linha.split(" ");
            int m = Integer.parseInt(dimensoes[0]);
            int n = Integer.parseInt(dimensoes[1]);
            
            // Aqui eu monto a matriz lendo linha por linha.
            String[][] labirinto = new String[m][n];
            for (int i = 0; i < m; i++) {
                linha = br.readLine();
                labirinto[i] = linha.split(" ");
            }

            // Agora eu chamo o processamento pra contar regiões e seres.
            processarLabirinto(labirinto, m, n);
            linha = br.readLine(); // E vou pro próximo caso de teste.
        }
        br.close();
    }
    
    public static void processarLabirinto(String[][] labirinto, int m, int n) {
        // bruno verifica as regiões isoladas no labirinto.
        boolean[][] visitado = new boolean[m][n];
        int regioes = 0;
        Map<Integer, Map<Character, Integer>> contagemPorRegiao = new HashMap<>();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!visitado[i][j]) {
                    regioes++;
                    Map<Character, Integer> contagem = new HashMap<>();
                    explorarRegiao(labirinto, visitado, m, n, i, j, contagem);
                    contagemPorRegiao.put(regioes, contagem);
                }
            }
        }
        
        // Agora eu mostro o número de regiões e o ser predominante de cada uma.
        System.out.println("Número de regiões isoladas: " + regioes);
        for (Map.Entry<Integer, Map<Character, Integer>> regiao : contagemPorRegiao.entrySet()) {
            int regiaoId = regiao.getKey();
            Map<Character, Integer> contagem = regiao.getValue();
            char serPredominante = ' ';
            int maxFreq = 0;
            for (char ser : contagem.keySet()) {
                if (contagem.get(ser) > maxFreq) {
                    maxFreq = contagem.get(ser);
                    serPredominante = ser;
                }
            }
            // Aqui eu converto o ser predominante pra algo legível.
            System.out.println("Região " + regiaoId + ": Ser predominante - " + seres[serPredominante - 'A']);
        }
    }
    
    public static void explorarRegiao(String[][] labirinto, boolean[][] visitado, int m, int n, int x, int y, Map<Character, Integer> contagem) {
        Queue<int[]> fila = new LinkedList<>();
        fila.add(new int[]{x, y});
        visitado[x][y] = true;

        while (!fila.isEmpty()) {
            int[] atual = fila.poll();
            int cx = atual[0], cy = atual[1];
            
            char valor = labirinto[cx][cy].toUpperCase().charAt(0);
            if (valor >= 'A' && valor <= 'F') {
                contagem.put(valor, contagem.getOrDefault(valor, 0) + 1);
            }
            
            for (int d = 0; d < 4; d++) {
                int nx = cx + direcoes[d][0];
                int ny = cy + direcoes[d][1];

                if (nx >= 0 && ny >= 0 && nx < m && ny < n && !visitado[nx][ny] && !temParede(labirinto[cx][cy], d)) {
                    fila.add(new int[]{nx, ny});
                    visitado[nx][ny] = true;
                }
            }
        }
    }
        // gau detecta as paredes tenho um aniversario hoje
    public static boolean temParede(String celula, int direcao) {

        try {
            int valor = Integer.parseInt(celula, 16);
            return ((valor >> direcao) & 1) == 1;
        } catch (NumberFormatException e) {
            System.err.println("Erro: valor inválido no labirinto - " + celula);
            return true; 
        }
    }
}
