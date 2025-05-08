package batalhaTestes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rpg.montanha_de_fogo.batalha.Batalha;
import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.personagem.Personagem;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatalhaTest {

    private Personagem personagem;
    private Monstro monstro1;
    private Monstro monstro2;
    private ArrayList<Monstro> monstros;

    @BeforeEach
    void setUp() {
        personagem = mock(Personagem.class);
        monstro1 = mock(Monstro.class);
        monstro2 = mock(Monstro.class);

        // Evita que getNome() retorne null nos testes
        when(personagem.getNome()).thenReturn("Herói");
        when(monstro1.getNome()).thenReturn("Goblin");
        when(monstro2.getNome()).thenReturn("Orc");

        monstros = new ArrayList<>();
        monstros.add(monstro1);
        monstros.add(monstro2);
    }

    @Test
    void testHaMonstrosVivosComUmVivo() {
        when(monstro1.estaVivo()).thenReturn(false);
        when(monstro2.estaVivo()).thenReturn(true);

        assertTrue(new BatalhaTestable(personagem, monstros).haMonstrosVivos());
    }

    @Test
    void testHaMonstrosVivosComNenhumVivo() {
        when(monstro1.estaVivo()).thenReturn(false);
        when(monstro2.estaVivo()).thenReturn(false);

        assertFalse(new BatalhaTestable(personagem, monstros).haMonstrosVivos());
    }

    @Test
    void testVerificarDerrotaMonstroDerrotado() {
        when(personagem.estaVivo()).thenReturn(true);
        when(monstro1.estaVivo()).thenReturn(false);
        when(monstro1.getNome()).thenReturn("Goblin");

        ArrayList<Monstro> lista = new ArrayList<>();
        lista.add(monstro1);

        BatalhaTestable batalha = new BatalhaTestable(personagem, lista);
        batalha.verificarDerrota(monstro1);

        verify(monstro1, atLeastOnce()).estaVivo();  // AGORA vai passar
    }

    @Test
    void testVerificarDerrotaPersonagemDerrotado() {
        when(personagem.estaVivo()).thenReturn(false);
        when(personagem.getNome()).thenReturn("Herói");

        BatalhaTestable batalha = new BatalhaTestable(personagem, monstros);
        batalha.verificarDerrota(monstro1);

        verify(personagem, atLeastOnce()).estaVivo();
    }

    private static class BatalhaTestable extends Batalha {
        public BatalhaTestable(Personagem personagem, ArrayList<Monstro> monstros) {
            super(personagem, monstros);
        }

        public boolean haMonstrosVivos() {
            return super.haMonstrosVivos();
        }

        public void verificarDerrota(Monstro monstro) {
            super.verificarDerrota(monstro);
        }
    }
}