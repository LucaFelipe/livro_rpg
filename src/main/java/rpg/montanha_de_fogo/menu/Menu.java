package rpg.montanha_de_fogo.menu;

import rpg.montanha_de_fogo.batalha.Batalha;
import rpg.montanha_de_fogo.personagem.Personagem;
import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.inventario.Pocao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Scanner scanner = new Scanner(System.in);
    private static Personagem personagem;

    public static void menu() {
        while (true) {
            try {
                exibirMenu();
                String escolha = lerEntrada("Escolha uma opção: ").trim();
                if (!processarOpcao(escolha)) break;
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Criar um novo personagem");
        System.out.println("2. Ver estatísticas do personagem");
        System.out.println("3. Listar equipamentos");
        System.out.println("4. Listar itens na mochila");
        System.out.println("5. Equipar equipamento");
        System.out.println("6. Guardar equipamento na mochila");
        System.out.println("7. Ganhar provisão");
        System.out.println("8. Comer provisão");
        System.out.println("9. Usar poção");
        System.out.println("10. Iniciar batalha");
        System.out.println("11. Salvar o personagem");
        System.out.println("12. Carregar o personagem");
        System.out.println("13. Sair");
    }

    private static String lerEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static boolean processarOpcao(String escolha) {
        switch (escolha) {
            case "1": criarPersonagem(); break;
            case "2": exibirEstatisticas(); break;
            case "3": listarEquipamentos(); break;
            case "4": listarItens(); break;
            case "5": equiparItem(); break;
            case "6": guardarItem(); break;
            case "7": ganharProvisao(); break;
            case "8": comerProvisao(); break;
            case "9": usarPocao(); break;
            case "10": iniciarBatalha(); break;
            case "11": salvarPersonagem(); break;
            case "12": carregarPersonagem(); break;
            case "13":
                System.out.println("Saindo do jogo...");
                return false;
            default:
                System.out.println("Opção inválida.");
        }
        return true;
    }

    private static void criarPersonagem() {
        try {
            String nome = lerEntrada("Digite o nome do seu personagem: ");
            personagem = new Personagem(nome);
            adicionarPocaoInicial();
            System.out.println("Personagem criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar personagem: " + e.getMessage());
        }
    }

    private static void adicionarPocaoInicial() {
        if (verificarPersonagem()) {
            System.out.println("Escolha uma poção inicial para o seu personagem:");
            System.out.println("1. Poção de Habilidade");
            System.out.println("2. Poção de Energia");
            System.out.println("3. Poção de Sorte");

            String escolha = lerEntrada("Escolha (1, 2 ou 3): ").trim();
            Pocao pocaoEscolhida = criarPocaoEscolhida(escolha);

            if (pocaoEscolhida != null) {
                personagem.getInventario().adicionarPocao(pocaoEscolhida.toString());
                System.out.println("Poção de " + pocaoEscolhida + " foi adicionada ao inventário.");
            }
        }
    }

    private static Pocao criarPocaoEscolhida(String escolha) {
        return switch (escolha) {
            case "1" -> new Pocao("habilidade");
            case "2" -> new Pocao("energia");
            case "3" -> new Pocao("sorte");
            default -> {
                System.out.println("Opção inválida. Nenhuma poção foi adicionada.");
                yield null;
            }
        };
    }

    private static void exibirEstatisticas() {
        if (verificarPersonagem()) {
            personagem.exibirEstatisticas();
        }
    }

    private static void listarEquipamentos() {
        if (verificarPersonagem()) {
            personagem.listarEquipamentos();
        }
    }

    private static void listarItens() {
        if (verificarPersonagem()) {
            personagem.getInventario().listarMochila();
            personagem.getInventario().listarPocoes();
        }
    }

    private static void equiparItem() {
        if (verificarPersonagem()) {
            String nomeItem = lerEntrada("Digite o nome do item para equipar: ");
            personagem.equipar(nomeItem);
        }
    }

    private static void guardarItem() {
        if (verificarPersonagem()) {
            String nome = lerEntrada("Digite o nome do equipamento: ");
            personagem.guardarNaMochila(nome);
        }
    }

    private static void ganharProvisao() {
        if (verificarPersonagem()) {
            personagem.ganharProvisao();
        }
    }

    private static void comerProvisao() {
        if (verificarPersonagem()) {
            personagem.comer();
        }
    }

    private static void usarPocao() {
        if (verificarPersonagem()) {
            String tipo = lerEntrada("Digite o tipo da poção (habilidade, vigor, fortuna): ").trim().toLowerCase();
            if (!tipo.equals("habilidade") && !tipo.equals("vigor") && !tipo.equals("fortuna")) {
                System.out.println("Tipo de poção inválido!");
                return;
            }
            Pocao pocao = personagem.getInventario().getPocao(tipo);
            if (pocao != null) {
                pocao.usarPocao(personagem);
            } else {
                System.out.println("Você não possui uma poção de " + tipo + ".");
            }
        }
    }

    private static void iniciarBatalha() {
        if (verificarPersonagem()) {
            try {
                String nome = lerEntrada("Digite o nome do monstro: ");
                int habilidade = lerEntradaInteiro("Digite a habilidade do monstro: ");
                int energia = lerEntradaInteiro("Digite a energia do monstro: ");

                Monstro monstro = new Monstro(nome, habilidade, energia);
                List<Monstro> monstros = new ArrayList<>();
                monstros.add(monstro);
                Batalha batalha = new Batalha(personagem, monstros);
                batalha.iniciar();
            } catch (Exception e) {
                System.out.println("Erro ao iniciar batalha: " + e.getMessage());
            }
        }
    }

    private static void salvarPersonagem() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("personagem.dat"))) {
            out.writeObject(personagem);
            System.out.println("Personagem salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar personagem: " + e.getMessage());
        }
    }

    private static void carregarPersonagem() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("personagem.dat"))) {
            personagem = (Personagem) in.readObject();
            System.out.println("Personagem carregado com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar personagem: " + e.getMessage());
        }
    }

    private static boolean verificarPersonagem() {
        if (personagem == null) {
            System.out.println("Crie um personagem primeiro!");
            return false;
        }
        return true;
    }

    private static int lerEntradaInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerEntrada(mensagem));
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }
}
