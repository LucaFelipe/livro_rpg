import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import rpg.montanha_de_fogo.personagem.Personagem;

public class PersonagemTest {
    private Personagem personagem;

    @BeforeEach
    void setup(){
        personagem = new Personagem("Bem");
    }

    @Test
    void testReduzirSorte(){
        personagem.setSorte(2);
        personagem.reduzirSorte();
        assertEquals(1, personagem.getSorte());
    }
}