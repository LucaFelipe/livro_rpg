package inventarioTestes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rpg.montanha_de_fogo.inventario.Inventario;
import rpg.montanha_de_fogo.inventario.Pocao;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
    }

    @Test
    void testAdicionarNaMochila() {
        inventario.adicionarNaMochila("Espada", "arma", 10, "habilidade");
        assertTrue(inventario.temNaMochila("Espada"));
        assertEquals("arma", inventario.obterTipoItem("Espada"));
        assertEquals(10, inventario.obterPoderItem("Espada"));
    }

    @Test
    void testRemoverDaMochila() {
        inventario.adicionarNaMochila("Escudo", "armadura", 5, "vigor");
        assertTrue(inventario.temNaMochila("Escudo"));
        inventario.removerDaMochila("Escudo");
        assertFalse(inventario.temNaMochila("Escudo"));
    }

    @Test
    void testObterTipoItemInexistente() {
        assertEquals("", inventario.obterTipoItem("Inexistente"));
    }

    @Test
    void testObterPoderItemInexistente() {
        assertEquals(0, inventario.obterPoderItem("Inexistente"));
    }

    @Test
    void testAdicionarPocaoPorTipoNaoAdicionaNova() {
        assertFalse(inventario.temPocao("habilidade"));
        inventario.adicionarPocao(new Pocao("vigor", 1) );
        assertFalse(inventario.temPocao("habilidade"));
    }

    @Test
    void testAdicionarPocaoNovaCorretamente() {
        Pocao pocao = new Pocao("fortuna", 2);
        inventario.adicionarPocao(pocao);
        assertTrue(inventario.temPocao("fortuna"));
        assertEquals(2, inventario.getPocao("fortuna").getDoses());
    }

    @Test
    void testTemPocao() {
        Pocao pocao = new Pocao("habilidade", 2);
        inventario.adicionarPocao(pocao);
        assertTrue(inventario.temPocao("habilidade"));
        assertFalse(inventario.temPocao("sorte"));
    }
}
