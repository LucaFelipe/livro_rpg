package rpg.montanha_de_fogo.menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rpg.montanha_de_fogo.batalha.Batalha;
import rpg.montanha_de_fogo.personagem.Personagem;
import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.inventario.Pocao;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Personagem personagem;

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
        System.out.println("7. Guardar item diretamente na mochila");
        System.out.println("8. Ganhar provisão");
        System.out.println("9. Comer provisão");
        System.out.println("10. Usar poção");
        System.out.println("11. Iniciar batalha");
        System.out.println("12. Salvar o personagem");
        System.out.println("13. Carregar o personagem");
        System.out.println("14. Diminuir dose da poção");
        System.out.println("15. Sair");
        System.out.println("");
    }

    private static String lerEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static boolean processarOpcao(String escolha) {
        switch (escolha) {
            case "1" -> criarPersonagem();
            case "2" -> exibirEstatisticas();
            case "3" -> listarEquipamentos();
            case "4" -> listarItens();
            case "5" -> equiparItem();
            case "6" -> guardarItem();
            case "7" -> guardarNaMochilaDiretamente();
            case "8" -> ganharProvisao();
            case "9" -> comerProvisao();
            case "10" -> usarPocao();
            case "11" -> iniciarBatalha();
            case "12" -> salvarPersonagem();
            case "13" -> carregarPersonagem();
            case "14" -> diminuirDosePocao();
            case "15" -> {
                System.out.println("Saindo do jogo...");
                return false;
            }
            default -> System.out.println("Opção inválida.");
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
            System.out.println("3. Poção de Fortuna");

            String escolha = lerEntrada("Escolha (1, 2 ou 3): ").trim();
            Pocao pocaoEscolhida = criarPocaoEscolhida(escolha);

            if (pocaoEscolhida != null) {
                personagem.getInventario().adicionarPocao(pocaoEscolhida);
                System.out.println("Poção de " + pocaoEscolhida.getTipo() + " foi adicionada ao inventário.");
            }
        }
    }

    static Pocao criarPocaoEscolhida(String escolha) {
        return switch (escolha) {
            case "1" -> new Pocao("habilidade", 2);
            case "2" -> new Pocao("energia", 2);
            case "3" -> new Pocao("fortuna", 2);
            default -> {
                System.out.println("Opção inválida. Nenhuma poção foi adicionada.");
                yield null;
            }
        };
    }

    public static void usarPocao() {
        if (verificarPersonagem()) {
            String tipo = lerEntrada("Digite o tipo da poção (habilidade, energia, fortuna): ").trim().toLowerCase();
            if (!tipo.equals("habilidade") && !tipo.equals("energia") && !tipo.equals("fortuna")) {
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
                ArrayList<Monstro> monstros = new ArrayList<>();
                monstros.add(monstro);
                Batalha batalha = new Batalha(personagem, monstros);
                batalha.iniciar();
            } catch (Exception e) {
                System.out.println("Erro ao iniciar batalha: " + e.getMessage());
            }
        }
    }

    private static void guardarNaMochilaDiretamente() {
        if (verificarPersonagem()) {
            String nomeItem = lerEntrada("Digite o nome do item a ser guardado na mochila: ");
            String tipo = lerEntrada("Digite o tipo do item a ser guardado na mochila: ");
            int valor = lerEntradaInteiro("Digite o poder do item a ser guardado na mochila: ");
            String local = lerEntrada("Digite o local onde o item deve ser equipado no personagem: ");
            personagem.getInventario().adicionarNaMochila(nomeItem, tipo, valor, local);
            System.out.println("Item " + nomeItem + " foi guardado diretamente na mochila.");
        }
    }

    public static void diminuirDosePocao() {
        if (verificarPersonagem()) {
            String tipo = lerEntrada("Digite o tipo da poção (habilidade, energia, fortuna): ").trim().toLowerCase();
            Pocao pocao = personagem.getInventario().getPocao(tipo);
            if (pocao != null) {
                if (pocao.diminuirDose()) {
                    System.out.println("A poção de " + tipo + " acabou!");
                } else {
                    System.out.println("Dose diminuída! Restam " + pocao.getDoses() + " dose(s).");
                }
            } else {
                System.out.println("Você não possui uma poção de " + tipo + ".");
            }
        }
    }

    public static void exibirEstatisticas() {
        if (verificarPersonagem()) personagem.exibirEstatisticas();
    }

    private static void listarEquipamentos() {
        if (verificarPersonagem()) personagem.listarEquipamentos();
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
        if (verificarPersonagem()) personagem.ganharProvisao();
    }

    private static void comerProvisao() {
        if (verificarPersonagem()) personagem.comer();
    }

    public static void salvarPersonagem() {
        String nomeArquivo = lerEntrada("Digite o nome do arquivo para salvar (ou deixe em branco para 'personagem.json'): ").trim();
        if (nomeArquivo.isEmpty()) {
            nomeArquivo = "personagem.json";
        }
        try (Writer writer = new FileWriter(nomeArquivo)) {
            gson.toJson(personagem, writer);
            System.out.println("Personagem salvo em " + nomeArquivo + " com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar personagem: " + e.getMessage());
        }
    }

    public static void carregarPersonagem() {
        String nomeArquivo = lerEntrada("Digite o nome do arquivo para carregar: ").trim();
        try (Reader reader = new FileReader(nomeArquivo)) {
            personagem = gson.fromJson(reader, Personagem.class);
            System.out.println("Personagem carregado com sucesso!");
        } catch (IOException e) {
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

    static int lerEntradaInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerEntrada(mensagem));
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }
}