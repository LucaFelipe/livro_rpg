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
        while (personagem.estaVivo() && haMonstrosVivos()) {
            System.out.println("\nMonstros disponíveis para combate:");
            for (int i = 0; i < monstros.size(); i++) {
                Monstro m = monstros.get(i);
                if (m.estaVivo()) {
                    System.out.println((i + 1) + " - " + m.getNome() + " (Vida: " + m.getEnergia() + ")");
                }
            }

            System.out.println("Digite o número do monstro que deseja atacar ou 'fugir':");
            String acao = scanner.nextLine();

            if (acao.equalsIgnoreCase("fugir")) {
                fugir();
                if (!personagem.estaVivo()) {
                    System.out.println(personagem.getNome() + " foi derrotado ao tentar fugir!");
                }
                return;
            }

            try {
                int indice = Integer.parseInt(acao) - 1;
                if (indice >= 0 && indice < monstros.size() && monstros.get(indice).estaVivo()) {
                    Monstro monstroEscolhido = monstros.get(indice);
                    exibirEstatisticas();
                    realizarAtaque(monstroEscolhido);
                    verificarDerrota(monstroEscolhido);
                } else {
                    System.out.println("Número inválido ou monstro já foi derrotado.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite o número do monstro ou 'fugir'.");
            }
        }

        if (personagem.estaVivo()) {
            System.out.println("Parabéns! Todos os monstros foram derrotados.");
        }
    }

    protected boolean haMonstrosVivos() {
        for (Monstro monstro : monstros) {
            if (monstro.estaVivo()) {
                return true;
            }
        }
        return false;
    }

    private void exibirEstatisticas() {
        personagem.exibirEstatisticas();
        for (Monstro monstro : monstros) {
            monstro.exibirEstatisticas();
        }
    }

    private void fugir() {
        System.out.print("Deseja usar a sorte para fugir? (sim/nao): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        boolean usarSorteFuga = resposta.equals("sim");

        int monstrosVivos = (int) monstros.stream().filter(Monstro::estaVivo).count();

        if (usarSorteFuga) {
            boolean fugaBemSucedida = true;
            for (int i = 0; i < monstrosVivos; i++) {
                if (!personagem.testarSorte()) {
                    fugaBemSucedida = false;
                    break;
                }
            }

            if (fugaBemSucedida) {
                personagem.receberDano(1);
                System.out.println(personagem.getNome() + " fugiu com sucesso de todos os monstros, sofrendo apenas 1 de dano!");
            } else {
                personagem.receberDano(3);
                System.out.println(personagem.getNome() + " não conseguiu escapar de todos os monstros e sofreu 3 de dano!");
            }

        } else {
            personagem.receberDano(2);
            System.out.println(personagem.getNome() + " fugiu sem usar a sorte e sofreu 2 de dano!");
        }
    }

    private void realizarAtaque(Monstro monstroEscolhido) {
        System.out.print("Deseja usar a sorte no ataque? (sim/nao): ");
        boolean usarSorteAtaque = scanner.nextLine().trim().equalsIgnoreCase("sim");
        int[] ataquePersonagem = personagem.atacar(usarSorteAtaque);
        int[] ataqueMonstro = monstroEscolhido.atacar();

        // Exibir forças
        System.out.println(personagem.getNome() + " (Força do ataque: " + ataquePersonagem[0] + ")");
        System.out.println(monstroEscolhido.getNome() + " (Força do ataque: " + ataqueMonstro[0] + ")");

        if (ataquePersonagem[0] > ataqueMonstro[0]) {
            int danoPersonagem = personagem.getDano();
            monstroEscolhido.receberDano(danoPersonagem);
            System.out.println(personagem.getNome() + " ataca e causa " + danoPersonagem + " de dano ao " + monstroEscolhido.getNome() + ". Energia restante: " + monstroEscolhido.getEnergia());
        } else if (ataqueMonstro[0] > ataquePersonagem[0]) {
            System.out.print("Deseja usar a sorte para se defender? (sim/nao): ");
            boolean usarSorteDefesa = scanner.nextLine().trim().equalsIgnoreCase("sim");
            int danoMonstro = monstroEscolhido.getDano();
            personagem.defender(danoMonstro, usarSorteDefesa);
        } else {
            System.out.println("O ataque de " + monstroEscolhido.getNome() + " e " + personagem.getNome() + " foram iguais. Ninguém sofre dano nesta rodada.");
        }
    }

    protected void verificarDerrota(Monstro monstro) {
        if (!personagem.estaVivo()) {
            System.out.println(personagem.getNome() + " foi derrotado!");
        } else if (!monstro.estaVivo()) {
            System.out.println(monstro.getNome() + " foi derrotado!");
        }
    }
}