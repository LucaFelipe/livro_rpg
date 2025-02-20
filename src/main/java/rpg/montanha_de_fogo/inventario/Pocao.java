package rpg.montanha_de_fogo.inventario;

import rpg.montanha_de_fogo.personagem.Personagem;

import java.io.Serial;
import java.io.Serializable;

public class Pocao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String tipo;
    private int dose;

    public Pocao(String tipo) {
        this.tipo = tipo;
        this.dose = 2;
    }

    @Override
    public String toString() {
        return "Poção de " + tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
    }

    public void aplicarEfeito(Personagem personagem) {
        switch (tipo) {
            case "habilidade":
                personagem.restaurarHabilidade();
                break;
            case "vigor":
                personagem.restaurarEnergia();
                break;
            case "fortuna":
                personagem.aumentarSorte();
                break;
            default:
                System.out.println("Tipo de poção desconhecido!");
                return;
        }
        System.out.println("Poção de " + tipo + " usada com sucesso!");
    }

    public boolean usarPocao(Personagem personagem) {
        if (dose > 0) {
            aplicarEfeito(personagem);
            dose--;
            System.out.println("Restam " + dose + " dose(s) de poção de " + tipo + ".");
            return true;
        }
        System.out.println("Não há doses restantes da poção de " + tipo + ".");
        return false;
    }

    public int getDoses() {
        return dose;
    }

    public void aumentarDose() {
        this.dose++;
    }

    public void diminuirDose() {
        if (dose > 0) {
            this.dose--;  // Garantir que a dose não ficará negativa
        }
    }
}
