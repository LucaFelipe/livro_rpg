package rpg.montanha_de_fogo.monstro;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Monstro implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private final String nome;
    private final int habilidade;
    private int energia;
    private static final int DANO_FIXO = 2;

    public String getNome() {
        return nome;
    }

    public Monstro(String nome, int habilidade, int energia) {
        this.nome = nome;
        this.habilidade = habilidade;
        this.energia = energia;
    }

    // Método para o ataque do monstro
    public int[] atacar() {
        Random rand = new Random();
        int dados = (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
        int ataque = habilidade + dados;
        System.out.println(nome + " ataca com força " + ataque + "!");
        return new int[]{ataque, DANO_FIXO};
    }

    // Método de defesa, reduz a energia do monstro conforme o dano
    public void defender(int dano) {
        energia -= dano;
        System.out.println(nome + " recebeu " + dano + " de dano! Energia restante: " + energia);
    }

    // Verifica se o monstro ainda está vivo
    public boolean estaVivo() {
        return energia <= 0;
    }

    // Exibe as estatísticas do monstro
    public void exibirEstatisticas() {
        System.out.println("\n=======================");
        System.out.println("Nome do Monstro: " + nome);
        System.out.println("Habilidade: " + habilidade);
        System.out.println("Energia: " + energia);
        System.out.println("=======================\n");
    }

    public int getDano() {
        return DANO_FIXO;
    }

    public void receberDano(int danoPersonagem) {
        energia -= danoPersonagem;
    }

    public int getEnergia() {
        return energia;
    }
}
