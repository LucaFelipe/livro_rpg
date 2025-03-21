import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rpg.montanha_de_fogo.menu.Menu;
import rpg.montanha_de_fogo.personagem.Personagem;
import static org.junit.Assert.*;

public class PersonagemTest {

    private Personagem personagem;

    @BeforeEach
    public void setup() {
        // Inicializa o personagem
        personagem = new Personagem("Hero");

        // Cria o Menu com o personagem
        Menu.personagem = personagem;
    }

    @Test
    public void testePersonagemAtaque(){
        boolean sorte = true;
        personagem.atacar(sorte);

        assertTrue();
    }


}
