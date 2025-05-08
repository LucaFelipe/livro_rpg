package personagemTestes;

import rpg.montanha_de_fogo.personagem.Personagem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import rpg.montanha_de_fogo.inventario.Inventario;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MockPersonagemTest {

    private Personagem personagem;

    @Mock
    private Inventario inventarioMock;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
        personagem = new Personagem("Herói");
        personagem.setInventario(inventarioMock);
    }

    @Test
    void testEquiparArmaComMock() {
        // Configura o mock para simular o comportamento do Inventario
        Mockito.when(inventarioMock.temNaMochila("Espada")).thenReturn(true);
        Mockito.when(inventarioMock.obterTipoItem("Espada")).thenReturn("arma");
        Mockito.when(inventarioMock.obterPoderItem("Espada")).thenReturn(3);

        personagem.equipar("Espada");

        assertEquals("Espada", personagem.getArmaEquipada());
        assertEquals(personagem.getHabilidadeMaxima() + 3, personagem.getHabilidade());
        Mockito.verify(inventarioMock, Mockito.times(1)).removerDaMochila("Espada");
    }

    @Test
    void testEquiparArmaduraComMock() {
        // Configura o mock para simular o comportamento do Inventario
        Mockito.when(inventarioMock.temNaMochila("Armadura de Couro")).thenReturn(true);
        Mockito.when(inventarioMock.obterTipoItem("Armadura de Couro")).thenReturn("armadura");
        Mockito.when(inventarioMock.obterPoderItem("Armadura de Couro")).thenReturn(2);

        personagem.equipar("Armadura de Couro");

        assertEquals("Armadura de Couro", personagem.getArmaduraEquipada());
        assertEquals(personagem.getEnergiaMaxima() + 2, personagem.getEnergia());
        Mockito.verify(inventarioMock, Mockito.times(1)).removerDaMochila("Armadura de Couro");
    }

    @Test
    void testGuardarNaMochilaComMock() {
        // Configura o mock para simular o comportamento do Inventario
        Mockito.when(inventarioMock.temNaMochila("Espada")).thenReturn(true);
        Mockito.when(inventarioMock.obterTipoItem("Espada")).thenReturn("arma");
        Mockito.when(inventarioMock.obterPoderItem("Espada")).thenReturn(3);

        // Equipa a arma primeiro
        personagem.equipar("Espada");

        // Guarda a arma na mochila
        personagem.guardarNaMochila("Espada");

        Assertions.assertNull(personagem.getArmaEquipada());
        assertEquals(personagem.getHabilidadeMaxima(), personagem.getHabilidade());
    }

    @Test
    void testComerSemProvisoesComMock() {
        // Configura o mock para simular que não há provisões
        Mockito.when(inventarioMock.temNaMochila("Provisão")).thenReturn(false);

        personagem.comer();

        assertEquals(personagem.getEnergiaMaxima(), personagem.getEnergia()); // Energia não deve ser restaurada
        assertEquals(personagem.getHabilidadeMaxima(), personagem.getHabilidade()); // Habilidade não deve ser restaurada
    }

    @Test
    void testAtacarComSorteComMock() {
        // Configura o mock para simular o teste de sorte
        Mockito.when(inventarioMock.temNaMochila(ArgumentMatchers.anyString())).thenReturn(false);

        int[] resultado = personagem.atacar(true);

        Assertions.assertTrue(resultado[0] >= personagem.getHabilidade() + 2); // Rolagem de ataque
    }
}