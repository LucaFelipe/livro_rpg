package rpg.montanha_de_fogo.inventario;

import rpg.montanha_de_fogo.personagem.Personagem;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<String, Item> mochila;
    private final Map<String, Pocao> pocoes;

    public Inventario() {
        mochila = new HashMap<>();
        pocoes = new HashMap<>();
    }

    public void adicionarNaMochila(String nome, String tipo, int poder, String local) {
        Item item = new Item(nome, tipo, poder, local);
        mochila.put(nome, item);
        System.out.println(nome + " foi adicionado à mochila.");
    }

    public void removerDaMochila(String nome) {
        if (mochila.containsKey(nome)) {
            mochila.remove(nome);
            System.out.println(nome + " foi removido da mochila.");
        } else {
            System.out.println("O item " + nome + " não está na mochila.");
        }
    }

    public boolean temNaMochila(String nome) {
        return mochila.containsKey(nome);
    }

    public String obterTipoItem(String nome) {
        if (mochila.containsKey(nome)) {
            return mochila.get(nome).tipo();
        } else {
            return null;
        }
    }

    public void adicionarPocao(String tipo) {
        if (pocoes.containsKey(tipo)) {
            pocoes.get(tipo).aumentarDose();
        } else {
            pocoes.put(tipo, new Pocao(tipo));
        }
        System.out.println("Poção de " + tipo + " adicionada ao inventário.");
    }

    public void usarPocao(String tipo, Personagem personagem) {
        if (pocoes.containsKey(tipo)) {
            Pocao pocao = pocoes.get(tipo);
            pocao.usarPocao(personagem);

            // Verificar se a poção ainda tem doses
            if (pocao.getDoses() == 0) {
                pocoes.remove(tipo);
                System.out.println("Poção de " + tipo + " foi completamente usada e removida do inventário.");
            }
        } else {
            System.out.println("Você não possui uma poção de " + tipo + "!");
        }
    }


    public void listarMochila() {
        System.out.println("\nItens na mochila:");
        if (mochila.isEmpty()) {
            System.out.println("A mochila está vazia.");
        } else {
            for (Item item : mochila.values()) {
                System.out.println(item.nome() + " (Poder: " + item.poder() + ")");
            }
        }
    }

    public void listarPocoes() {
        System.out.println("\nPoções no inventário:");
        if (pocoes.isEmpty()) {
            System.out.println("Não há poções no inventário.");
        } else {
            for (Map.Entry<String, Pocao> entry : pocoes.entrySet()) {
                System.out.println("Poção de " + entry.getKey() + " (Doses: " + entry.getValue().getDoses() + ")");
            }
        }
    }

    public Pocao getPocao(String tipo) {
        return pocoes.get(tipo);  // Retorna a poção do tipo especificado, ou null se não existir
    }

}
