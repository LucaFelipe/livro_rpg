package rpg.montanha_de_fogo.batalha;

import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.personagem.Personagem;

import java.util.List;
import java.util.Scanner;

public class Batalha {
    private final Personagem personagem;
    private final List<Monstro> monstros;
    private final Scanner scanner;

    public Batalha(Personagem personagem, List<Monstro> monstros) {
        this.personagem = personagem;
        this.monstros = monstros;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (personagem.estaVivo() && !monstros.isEmpty()) {
            exibirEstatisticas();

            if (!escolherAcao()) {
                System.out.println(personagem.getNome() + " fugiu da batalha!");
                break;
            }

            realizarAtaque();
            verificarDerrota();
        }
    }

    private void verificarDerrota() {
        if (!personagem.estaVivo()) {
            System.out.println(personagem.getNome() + " foi derrotado!");
        } else if (monstros.isEmpty()) {
            System.out.println("Todos os monstros foram derrotados!");
        }
    }

    private void exibirEstatisticas() {
        personagem.exibirEstatisticas();
        for (Monstro monstro : monstros) {
            monstro.exibirEstatisticas();
        }
    }

    private boolean escolherAcao() {
        System.out.println("Escolha uma ação (atacar/fugir): ");
        String acao = scanner.nextLine().trim().toLowerCase();
        return acao.equals("atacar");
    }

    private void realizarAtaque() {
        if (monstros.isEmpty()) return;

        System.out.println("Escolha um monstro para atacar (1-" + monstros.size() + "):");
        int indice;
        try {
            indice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        if (indice < 0 || indice >= monstros.size() || !monstros.get(indice).estaVivo()) {
            System.out.println("Escolha inválida.");
            return;
        }

        Monstro alvo = monstros.get(indice);

        System.out.println("Deseja usar a sorte no ataque? (sim/nao): ");
        boolean usarSorteAtaque = scanner.nextLine().trim().equalsIgnoreCase("sim");

        int ataquePersonagem = personagem.atacar(false);
        int[] ataqueMonstro = alvo.atacar();

        if (ataquePersonagem > ataqueMonstro[0]) {
            int dano = 2;
            if (usarSorteAtaque) {
                if (personagem.testarSorte()) {
                    personagem.reduzirSorte();
                    dano += 2;
                    System.out.println("A sorte ajudou! Dano aumentado para " + dano);
                } else {
                    dano -= 1;
                    System.out.println("A sorte falhou! Dano reduzido para " + dano);
                }
            }
            alvo.defender(dano);
            if (!alvo.estaVivo()) {
                monstros.remove(indice);
            }
        } else {
            System.out.println("O monstro bloqueou o ataque!");
            System.out.println("Deseja usar a sorte para reduzir o dano? (sim/nao): ");
            boolean usarSorteDefesa = scanner.nextLine().trim().equalsIgnoreCase("sim");
            int danoRecebido = 2;
            if (usarSorteDefesa) {
                if (personagem.testarSorte()) {
                    personagem.reduzirSorte();
                    danoRecebido -= 1;
                    System.out.println("A sorte ajudou! Dano reduzido para " + danoRecebido);
                } else {
                    danoRecebido += 2;
                    System.out.println("A sorte falhou! Dano aumentado para " + danoRecebido);
                }
            }
            personagem.receberDano(danoRecebido);
        }
    }
}
