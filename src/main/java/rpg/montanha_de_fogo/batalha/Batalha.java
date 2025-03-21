package rpg.montanha_de_fogo.batalha;

import rpg.montanha_de_fogo.personagem.Personagem;
import rpg.montanha_de_fogo.monstro.Monstro;

import java.util.ArrayList;
import java.util.Scanner;

public class Batalha {
    private final Personagem personagem;
    private final ArrayList<Monstro> monstros;
    private final Scanner scanner = new Scanner(System.in);

    public Batalha(Personagem personagem, ArrayList<Monstro> monstros) {
        this.personagem = personagem;
        this.monstros = monstros;
    }

    public void iniciar() {
        while (personagem.estaVivo() && monstros.get(0).estaVivo()) {
            exibirEstatisticas();

            String acao = escolherAcao();
            if (acao.equals("fugir")) {
                fugir();
                if (!personagem.estaVivo()) {
                    System.out.println(personagem.getNome() + " foi derrotado ao tentar fugir!");
                }
                break;
            }

            realizarAtaque();
            if (!personagem.estaVivo() || !monstros.get(0).estaVivo()) {
                break;
            }
        }
    }

    private void exibirEstatisticas() {
        personagem.exibirEstatisticas();
        monstros.get(0).exibirEstatisticas();
    }

    private String escolherAcao() {
        return obterEntrada(new String[]{"atacar", "fugir"});
    }

    private String obterEntrada(String[] opcoes) {
        while (true) {
            System.out.print("Escolha uma ação (atacar/fugir): ");
            String entrada = scanner.nextLine().trim().toLowerCase();
            for (String opcao : opcoes) {
                if (entrada.equals(opcao)) {
                    return entrada;
                }
            }
            System.out.println("Ação inválida. Por favor, escolha entre " + String.join(", ", opcoes) + ".");
        }
    }

    private void fugir() {
        System.out.print("Deseja usar a sorte para fugir? (sim/nao): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        boolean usarSorteFuga = resposta.equals("sim");

        if (usarSorteFuga) {
            if (personagem.testarSorte()){
                System.out.println(personagem.getNome() + " fugiu com 1 de dano!");
                personagem.receberDano(1);
            } else {
                personagem.receberDano(3);
                System.out.println(personagem.getNome() + " fugiu com 3 de dano!");
            }
        } else {
            System.out.println(personagem.getNome() + " fugiu com 2 de dano!");
            personagem.receberDano(2);
        }
    }

    private void realizarAtaque() {
        System.out.print("Deseja usar a sorte no ataque? (sim/nao): ");
        boolean usarSorteAtaque = scanner.nextLine().trim().equalsIgnoreCase("sim");
        int[] ataquePersonagem = personagem.atacar(usarSorteAtaque);

        for (Monstro monstro : monstros) {
            int[] ataqueMonstro = monstro.atacar();

            if (ataquePersonagem[0] > ataqueMonstro[0]) {
                int danoPersonagem = personagem.getDano();
                monstro.receberDano(danoPersonagem);
                System.out.println(personagem.getNome() + " ataca e causa " + danoPersonagem + " de dano ao " + monstro.getNome() + ". Energia restante: " + monstro.getEnergia());
            } else if (ataqueMonstro[0] > ataquePersonagem[0]) {
                System.out.print("Deseja usar a sorte para se defender? (sim/nao): ");
                boolean usarSorteDefesa = scanner.nextLine().trim().equalsIgnoreCase("sim");
                int danoMonstro = monstro.getDano();
                personagem.defender(danoMonstro, usarSorteDefesa);
            } else {
                System.out.println("O ataque de " + monstro.getNome() + " e " + personagem.getNome() + " foram iguais. Ninguém sofre dano nesta rodada.");
            }
        }

        verificarDerrota();
    }

    private void verificarDerrota() {
        if (!personagem.estaVivo()) {
            System.out.println(personagem.getNome() + " foi derrotado!");
        } else if (!monstros.get(0).estaVivo()) {
            System.out.println("O monstro foi derrotado!");
        }
    }
}
