package Mock;

import rpg.montanha_de_fogo.inventario.Inventario;
import rpg.montanha_de_fogo.inventario.Item;
import rpg.montanha_de_fogo.inventario.Pocao;

import java.util.HashMap;
import java.util.Map;

public class InventarioMock extends Inventario {
    private final Map<String, Item> itensEquipaveis;
    private final Map<String, Pocao> pocoes;

    public InventarioMock() {
        itensEquipaveis = new HashMap<>();
        pocoes = new HashMap<>();
    }

    @Override
    public void adicionarNaMochila(String nome, String tipo, int poder, String local) {
        itensEquipaveis.put(nome, new Item(nome, tipo, poder, local));
    }

    @Override
    public void removerDaMochila(String nome) {
        itensEquipaveis.remove(nome);
    }

    @Override
    public boolean temNaMochila(String nome) {
        return itensEquipaveis.containsKey(nome);
    }

    @Override
    public String obterTipoItem(String nome) {
        return itensEquipaveis.getOrDefault(nome, new Item("", "", 0, "")).tipo();
    }

    @Override
    public int obterPoderItem(String nome) {
        return itensEquipaveis.getOrDefault(nome, new Item("", "", 0, "")).poder();
    }

    @Override
    public void adicionarPocao(String tipo) {
        // Simula o aumento da dose de poções no inventário
        pocoes.computeIfAbsent(tipo, Pocao::new).aumentarDose();
    }

    @Override
    public Pocao getPocao(String tipo) {
        return pocoes.get(tipo);
    }

    @Override
    public boolean temPocao(String tipo) {
        return pocoes.containsKey(tipo);
    }

    // Métodos adicionais para testes
    public void adicionarItemSimulado(String nomeItem, String tipo, int poder) {
        itensEquipaveis.put(nomeItem, new Item(nomeItem, tipo, poder, ""));
    }

    public void listarMochila() {
        if (itensEquipaveis.isEmpty()) {
            System.out.println("A mochila está vazia.");
        } else {
            itensEquipaveis.forEach((nome, item) -> System.out.println(nome + " (Poder: " + item.poder() + ")"));
        }
    }

    public void listarPocoes() {
        if (pocoes.isEmpty()) {
            System.out.println("Não há poções no inventário.");
        } else {
            pocoes.forEach((tipo, pocao) -> System.out.println("Poção de " + tipo + " (Doses: " + pocao.getDoses() + ")"));
        }
    }
}
