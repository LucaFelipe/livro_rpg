package rpg.montanha_de_fogo.inventario;

import rpg.montanha_de_fogo.personagem.Personagem;

import java.io.Serial;
import java.io.Serializable;

public class Pocao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String tipo;
    private int dose;

    public Pocao(String tipo, int dose) {
        this.tipo = tipo;
        this.dose = dose;
    }

    @Override
    public String toString() {
        return "Poção de " + tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
    }

    public void aplicarEfeito(Personagem personagem) {
        System.out.println("1 - habilidade\n2 - energia\n3 - sorte\n\nDigite o tipo de efeito que deseja aplicar:");
        switch (tipo) {
            case "1":
                personagem.restaurarHabilidade();
                break;
            case "2":
                personagem.restaurarEnergia();
                break;
            case "3":
                personagem.aumentarSorte();
                break;
            default:
                System.out.println("Tipo de poção desconhecido!");
                return;
        }
        System.out.println("Poção de " + tipo + " usada com sucesso!");
    }

    public void usarPocao(Personagem personagem) {
        if (dose > 0) {
            aplicarEfeito(personagem);
            diminuirDose();
            System.out.println("Restam " + dose + " dose(s) de poção de " + tipo + ".");
            return;
        }
        System.out.println("Não há doses restantes da poção de " + tipo + ".");
    }

    public int getDoses() {
        return dose;
    }

    public void aumentarDose() {
        this.dose++;
    }

    public boolean diminuirDose() {
        if (dose > 0) {
            this.dose--;
        }
        return dose == 0;
    }

    public String getTipo() {
        return tipo;
    }
}
