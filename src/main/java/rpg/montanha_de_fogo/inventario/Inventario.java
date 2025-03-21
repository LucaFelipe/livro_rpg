package rpg.montanha_de_fogo.inventario;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Item> mochila = new HashMap<>();
    private final Map<String, Pocao> pocoes = new HashMap<>();

    public void adicionarNaMochila(String nome, String tipo, int poder, String local) {
        mochila.put(nome, new Item(nome, tipo, poder, local));
        System.out.println(nome + " foi adicionado à mochila.");
    }

    public void removerDaMochila(String nome) {
        if (mochila.remove(nome) != null) {
            System.out.println(nome + " foi removido da mochila.");
        } else {
            System.out.println("O item " + nome + " não está na mochila.");
        }
    }

    public boolean temNaMochila(String nome) {
        return mochila.containsKey(nome);
    }

    public String obterTipoItem(String nome) {
        return mochila.getOrDefault(nome, new Item("", "", 0, "")).tipo();
    }

    public int obterPoderItem(String nome) {
        return mochila.getOrDefault(nome, new Item("", "", 0, "")).poder();
    }

    public void adicionarPocao(String tipo) {
        pocoes.computeIfAbsent(tipo, Pocao::new).aumentarDose();
        System.out.println("Poção de " + tipo + " adicionada ao inventário.");
    }

    public void listarMochila() {
        System.out.println("\nItens na mochila:");
        if (mochila.isEmpty()) {
            System.out.println("A mochila está vazia.");
        } else {
            mochila.values().forEach(item ->
                    System.out.println(item.nome() + " (Poder: " + item.poder() + ")")
            );
        }
    }

    public void listarPocoes() {
        System.out.println("\nPoções no inventário:");
        if (pocoes.isEmpty()) {
            System.out.println("Não há poções no inventário.");
        } else {
            pocoes.forEach((tipo, pocao) ->
                    System.out.println("Poção de " + tipo + " (Doses: " + pocao.getDoses() + ")")
            );
        }
    }

    public Pocao getPocao(String tipo) {
        return pocoes.get(tipo);
    }

    public boolean temPocao(String tipo) {
        return pocoes.containsKey(tipo);
    }
}