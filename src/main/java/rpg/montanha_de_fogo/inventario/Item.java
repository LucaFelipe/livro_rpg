package rpg.montanha_de_fogo.inventario;

import java.io.Serial;
import java.io.Serializable;

public record Item(String nome, String tipo, int poder, String local) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Item [nome=" + nome + ", tipo=" + tipo + ", poder=" + poder + ", local=" + local + "]";
    }
}
