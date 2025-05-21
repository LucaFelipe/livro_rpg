package rpg.montanha_de_fogo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import rpg.montanha_de_fogo.batalha.Batalha;
import rpg.montanha_de_fogo.monstro.Monstro;
import rpg.montanha_de_fogo.personagem.Personagem;
import rpg.montanha_de_fogo.inventario.Pocao;
import java.io.*;
import java.util.List;

public class MenuGUI extends Application {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Personagem personagem;
    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Montanha de Fogo - Menu Principal");

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10;");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        String[] opcoes = {
                "1. Criar novo personagem",
                "2. Ver estatísticas",
                "3. Listar equipamentos",
                "4. Listar mochila",
                "5. Equipar item",
                "6. Guardar equipamento",
                "7. Guardar item diretamente",
                "8. Ganhar provisão",
                "9. Comer provisão",
                "10. Usar poção",
                "11. Diminuir dose da poção",
                "12. Iniciar batalha",
                "13. Salvar personagem",
                "14. Carregar personagem",
                "15. Excluir personagem"
        };

        for (int i = 0; i < opcoes.length; i++) {
            Button btn = new Button(opcoes[i]);
            int finalI = i + 1;
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> processarOpcao(finalI, primaryStage));
            vbox.getChildren().add(btn);
        }

        vbox.getChildren().add(outputArea);

        Scene scene = new Scene(vbox, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processarOpcao(int opcao, Stage stage) {
        switch (opcao) {
            case 1 -> criarPersonagem();
            case 2 -> exibirEstatisticas();
            case 3 -> listarEquipamentos();
            case 4 -> listarItens();
            case 5 -> equiparItem();
            case 6 -> guardarItem();
            case 7 -> guardarNaMochilaDiretamente();
            case 8 -> ganharProvisao();
            case 9 -> comerProvisao();
            case 10 -> usarPocao();
            case 11 -> diminuirDosePocao();
            case 12 -> iniciarBatalha();
            case 13 -> salvarPersonagem();
            case 14 -> carregarPersonagem(stage);
            case 15 -> excluirPersonagem();
        }
    }

    private void criarPersonagem() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Digite o nome do seu personagem:");
        dialog.showAndWait().ifPresent(nome -> {
            personagem = new Personagem(nome);
            adicionarPocaoInicial();
            append("Personagem criado: " + nome);
        });
    }

    private void adicionarPocaoInicial() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("habilidade", "habilidade", "energia", "fortuna");
        dialog.setHeaderText("Escolha uma poção inicial:");
        dialog.showAndWait().ifPresent(tipo -> {
            Pocao pocao = new Pocao(tipo, 2);
            personagem.getInventario().adicionarPocao(pocao);
            append("Poção de " + tipo + " adicionada ao inventário.");
        });
    }

    private void usarPocao() {
        if (verificarPersonagem()) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("habilidade", "habilidade", "energia", "fortuna");
            dialog.setHeaderText("Escolha o tipo da poção:");
            dialog.showAndWait().ifPresent(tipo -> {
                Pocao pocao = personagem.getInventario().getPocao(tipo);
                if (pocao != null) {
                    pocao.usarPocao(personagem);
                    append("Poção usada: " + tipo);
                } else {
                    append("Você não possui a poção de " + tipo);
                }
            });
        }
    }

    private void abrirTelaDeBatalha(Monstro monstro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/batalha.fxml"));
            Parent root = loader.load();

            // Passa personagem e monstro para o controlador da batalha
            Batalha controller = loader.getController();
            controller.inicializar(personagem, List.of(monstro));

            Stage stage = new Stage();
            stage.setTitle("Batalha contra " + monstro.getNome());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            append("Erro ao abrir a batalha: " + e.getMessage());
        }
    }

    private void iniciarBatalha() {
        if (verificarPersonagem()) {
            TextInputDialog nomeDialog = new TextInputDialog("Goblin");
            nomeDialog.setHeaderText("Nome do monstro:");
            TextInputDialog habilidadeDialog = new TextInputDialog("6");
            habilidadeDialog.setHeaderText("Habilidade do monstro:");
            TextInputDialog energiaDialog = new TextInputDialog("10");
            energiaDialog.setHeaderText("Energia do monstro:");

            nomeDialog.showAndWait().ifPresent(nome ->
                    habilidadeDialog.showAndWait().ifPresent(hab ->
                            energiaDialog.showAndWait().ifPresent(ene -> {
                                try {
                                    int h = Integer.parseInt(hab);
                                    int e = Integer.parseInt(ene);
                                    Monstro m = new Monstro(nome, h, e);
                                    abrirTelaDeBatalha(m); // <- AGORA USANDO A TELA DE BATALHA
                                    append("Batalha contra " + nome + " iniciada!");
                                } catch (Exception ex) {
                                    append("Erro ao iniciar batalha: " + ex.getMessage());
                                }
                            })));
        }
    }

    private void guardarNaMochilaDiretamente() {
        if (verificarPersonagem()) {
            TextInputDialog nomeDialog = new TextInputDialog("Espada Longa");
            nomeDialog.setHeaderText("Nome do item:");
            TextInputDialog tipoDialog = new TextInputDialog("arma");
            tipoDialog.setHeaderText("Tipo do item:");
            TextInputDialog valorDialog = new TextInputDialog("5");
            valorDialog.setHeaderText("Valor do item:");
            TextInputDialog localDialog = new TextInputDialog("mão");
            localDialog.setHeaderText("Local de uso:");

            nomeDialog.showAndWait().ifPresent(nome ->
                    tipoDialog.showAndWait().ifPresent(tipo ->
                            valorDialog.showAndWait().ifPresent(valor ->
                                    localDialog.showAndWait().ifPresent(local -> {
                                        try {
                                            personagem.getInventario().adicionarNaMochila(nome, tipo, Integer.parseInt(valor), local);
                                            append("Item " + nome + " adicionado à mochila.");
                                        } catch (NumberFormatException e) {
                                            append("Valor inválido.");
                                        }
                                    }))));
        }
    }

    private void diminuirDosePocao() {
        if (verificarPersonagem()) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("habilidade", "habilidade", "energia", "fortuna");
            dialog.setHeaderText("Escolha o tipo da poção:");
            dialog.showAndWait().ifPresent(tipo -> {
                Pocao pocao = personagem.getInventario().getPocao(tipo);
                if (pocao != null) {
                    if (pocao.diminuirDose()) {
                        append("A poção de " + tipo + " acabou!");
                    } else {
                        append("Dose diminuída. Restam " + pocao.getDoses() + " dose(s).\n");
                    }
                } else {
                    append("Você não possui a poção de " + tipo);
                }
            });
        }
    }

    private void exibirEstatisticas() {
        if (verificarPersonagem()) personagem.exibirEstatisticas(outputArea);
    }

    private void listarEquipamentos() {
        if (verificarPersonagem()) personagem.listarEquipamentos(outputArea);
    }

    private void listarItens() {
        if (verificarPersonagem()) {
            personagem.getInventario().listarMochila(outputArea);
            personagem.getInventario().listarPocoes(outputArea);
        }
    }

    private void equiparItem() {
        if (verificarPersonagem()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Digite o nome do item a equipar:");
            dialog.showAndWait().ifPresent(nome -> personagem.equipar(nome));
        }
    }

    private void guardarItem() {
        if (verificarPersonagem()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Digite o nome do equipamento a guardar:");
            dialog.showAndWait().ifPresent(nome -> personagem.guardarNaMochila(nome));
        }
    }

    private void ganharProvisao() {
        if (verificarPersonagem()) personagem.ganharProvisao();
    }

    private void comerProvisao() {
        if (verificarPersonagem()) personagem.comer();
    }

    private void salvarPersonagem() {
        if (verificarPersonagem()) {
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("personagem.json");
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(personagem, writer);
                    append("Personagem salvo em: " + file.getAbsolutePath());
                } catch (IOException e) {
                    append("Erro ao salvar personagem: " + e.getMessage());
                }
            }
        }
    }

    private void carregarPersonagem(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecione um arquivo de personagem");
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            try (Reader reader = new FileReader(file)) {
                personagem = gson.fromJson(reader, Personagem.class);
                append("Personagem carregado: " + personagem.getNome());
            } catch (IOException e) {
                append("Erro ao carregar personagem: " + e.getMessage());
            }
        }
    }

    private void excluirPersonagem() {
        if (personagem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Personagem");
            alert.setHeaderText("Tem certeza que deseja excluir o personagem atual?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    personagem = null;
                    append("Personagem excluído.");
                }
            });
        } else {
            append("Nenhum personagem para excluir.");
        }
    }

    private boolean verificarPersonagem() {
        if (personagem == null) {
            append("Crie um personagem primeiro!");
            return false;
        }
        return true;
    }

    private void append(String texto) {
        outputArea.appendText(texto + "\n");
    }
}