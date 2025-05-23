package rpg.montanha_de_fogo.inventario;

import javafx.scene.control.TextArea;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Item> mochila = new HashMap<>();
    protected final Map<String, Pocao> pocoes = new HashMap<>();

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

    public void adicionarPocao(Pocao pocao) {
        pocoes.put(pocao.getTipo(), pocao);
        System.out.println("Poção de " + pocao.getTipo() + " foi adicionada ao inventário.");
    }

    public void listarMochila(TextArea output) {
        output.appendText("Mochila:\n");
        for (Item i : mochila.values()) {
            output.appendText(i.toString() + "\n");
        }
    }


    public void listarPocoes(TextArea output) {
        output.appendText("Poções:\n");
        for (Pocao p : pocoes.values()) {
            output.appendText(p.toString() + "\n");
        }
    }

    public Pocao getPocao(String tipo) {
        return pocoes.get(tipo);
    }

    public boolean temPocao(String tipo) {
        return pocoes.containsKey(tipo);
    }
}