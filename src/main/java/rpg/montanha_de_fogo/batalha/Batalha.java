package rpg.montanha_de_fogo.batalha;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.personagem.Personagem;

import java.util.List;

public class Batalha {

    @FXML
    private TextArea logArea;

    @FXML
    private ComboBox<String> monstroCombo;

    @FXML
    private Button atacarButton, fugirButton;

    @FXML
    private CheckBox usarSorteAtaqueCheck, usarSorteDefesaCheck, usarSorteFugaCheck;

    private Personagem personagem;
    private List<Monstro> monstros;

    public void inicializar(Personagem personagem, List<Monstro> monstros) {
        this.personagem = personagem;
        this.monstros = monstros;
        atualizarListaMonstros();
        exibirEstatisticas();
    }

    @FXML
    private void onAtacar() {
        int index = monstroCombo.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= monstros.size()) {
            log("Selecione um monstro válido.");
            return;
        }

        Monstro monstro = monstros.get(index);
        boolean usarSorte = usarSorteAtaqueCheck.isSelected();

        int[] ataquePersonagem = personagem.atacar(usarSorte);
        int[] ataqueMonstro = monstro.atacar();

        log(personagem.getNome() + " atacou com força " + ataquePersonagem[0]);
        log(monstro.getNome() + " atacou com força " + ataqueMonstro[0]);

        if (ataquePersonagem[0] > ataqueMonstro[0]) {
            monstro.receberDano(personagem.getDano());
            log("Você causou " + personagem.getDano() + " de dano ao " + monstro.getNome());
        } else if (ataqueMonstro[0] > ataquePersonagem[0]) {
            boolean usarSorteDefesa = usarSorteDefesaCheck.isSelected();
            personagem.defender(monstro.getDano(), usarSorteDefesa);
            log(monstro.getNome() + " atacou você com " + monstro.getDano() + " de dano.");
        } else {
            log("Empate! Ninguém sofreu dano.");
        }

        verificarEstado(monstro);
        atualizarListaMonstros();
    }

    @FXML
    private void onFugir() {
        int monstrosVivos = (int) monstros.stream().filter(Monstro::estaVivo).count();
        boolean usarSorte = usarSorteFugaCheck.isSelected();

        if (usarSorte) {
            boolean sucesso = true;
            for (int i = 0; i < monstrosVivos; i++) {
                if (!personagem.testarSorte()) {
                    sucesso = false;
                    break;
                }
            }
            if (sucesso) {
                personagem.receberDano(1);
                log("Você fugiu com sucesso, sofreu apenas 1 de dano.");
            } else {
                personagem.receberDano(3);
                log("Você falhou na fuga e sofreu 3 de dano!");
            }
        } else {
            personagem.receberDano(2);
            log("Você fugiu sem sorte e sofreu 2 de dano.");
        }

        if (!personagem.estaVivo()) {
            log("Você foi derrotado ao tentar fugir.");
        }
    }

    private void verificarEstado(Monstro monstro) {
        if (!personagem.estaVivo()) {
            log("Você foi derrotado!");
            desativarControles();
        } else if (!monstro.estaVivo()) {
            log(monstro.getNome() + " foi derrotado!");
        }

        if (monstros.stream().noneMatch(Monstro::estaVivo)) {
            log("Parabéns! Todos os monstros foram derrotados.");
            desativarControles();
        }
    }

    private void exibirEstatisticas() {
        log(personagem.getNome() + " - Energia: " + personagem.getEnergia());
        monstros.forEach(monstro -> {
            log(monstro.getNome() + " - Energia: " + monstro.getEnergia());
        });
    }

    private void atualizarListaMonstros() {
        monstroCombo.getItems().clear();
        for (Monstro m : monstros) {
            if (m.estaVivo()) {
                monstroCombo.getItems().add(m.getNome() + " (Energia: " + m.getEnergia() + ")");
            }
        }
    }

    private void desativarControles() {
        atacarButton.setDisable(true);
        fugirButton.setDisable(true);
        monstroCombo.setDisable(true);
    }

    private void log(String mensagem) {
        logArea.appendText(mensagem + "\n");
    }
}