package rpg.montanha_de_fogo.personagem;

import javafx.scene.control.TextArea;
import rpg.montanha_de_fogo.inventario.Inventario;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class Personagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String nome;
    private int habilidade;
    private int habilidadeMaxima;
    private int energia;
    private int energiaMaxima;
    private int sorte;
    private int sorteMaxima;
    private int provisoes;
    private Inventario inventario;
    private String armaEquipada = null;
    private String armaduraEquipada = null;
    private static final Random rand = new Random();

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
    public int getHabilidadeMaxima() {
        return habilidadeMaxima;
    }

    public void setHabilidadeMaxima(int habilidadeMaxima) {
        this.habilidadeMaxima = habilidadeMaxima;
    }

    public int getSorteMaxima() {
        return sorteMaxima;
    }

    public void setSorteMaxima(int sorteMaxima) {
        this.sorteMaxima = sorteMaxima;
    }
    public int getEnergiaMaxima() {
        return energiaMaxima;
    }

    public void setEnergiaMaxima(int energiaMaxima) {
        this.energiaMaxima = energiaMaxima;
    }

    public Personagem(String nome) {
        this.nome = nome;
        this.habilidade = this.habilidadeMaxima = rand.nextInt(1, 6) + 6;
        this.energia = this.energiaMaxima = rand.nextInt(1,6) + rand.nextInt(1, 6) + 12;
        this.sorte = this.sorteMaxima = rand.nextInt(1,6) + 6;
        this.provisoes = 10;
        this.inventario = new Inventario();
    }

    public int getSorte() {
        return sorte;
    }

    public void setSorte(int sorte) {
        this.sorte = sorte;
    }

    public int getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(int habilidade) {
        this.habilidade = habilidade;
    }

    public int getEnergia() {
        return energia;
    }
    public void setEnergia(int energia) {
        this.energia = energia;
    }

    public String getArmaEquipada() {
        return armaEquipada;
    }

    public void setArmaEquipada(String armaEquipada) {
        this.armaEquipada = armaEquipada;
    }
    public String getArmaduraEquipada() {
        return armaduraEquipada;
    }

    public void setArmaduraEquipada(String armaduraEquipada) {
        this.armaduraEquipada = armaduraEquipada;
    }


    public String getNome() {
        return nome;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void exibirEstatisticas(TextArea output) {
        output.appendText("Nome: " + nome + "\n");
        output.appendText("Habilidade: " + habilidade + "/" + habilidadeMaxima + "\n");
        output.appendText("Energia: " + energia + "/" + energiaMaxima + "\n");
        output.appendText("Sorte: " + sorte + "/" + sorteMaxima + "\n");
        listarEquipamentos(output);
        inventario.listarMochila(output);
        inventario.listarPocoes(output);
    }

    public void listarEquipamentos(TextArea outputArea) {
        System.out.println("Equipamentos Equipados:");
        System.out.println("Arma: " + (armaEquipada != null ? armaEquipada : "Nenhuma"));
        System.out.println("Armadura: " + (armaduraEquipada != null ? armaduraEquipada : "Nenhuma"));
    }

    public void comer() {
        if (provisoes > 0) {
            provisoes--;
            restaurarHabilidade();
            restaurarEnergia();
            aumentarSorte();
            System.out.println(nome + " comeu uma provisão e restaurou todos os status.");
        } else {
            System.out.println(nome + " não tem provisões disponíveis.");
        }
    }

    public void ganharProvisao() {
        provisoes++;
        System.out.println(nome + " ganhou uma provisão! Total de provisões: " + provisoes);
    }

    public void equipar(String nomeItem) {
        if (!inventario.temNaMochila(nomeItem)) {
            System.out.println("Esse item não está na mochila!");
            return;
        }

        String tipo = inventario.obterTipoItem(nomeItem);
        if (tipo == null) {
            System.out.println("Tipo de equipamento desconhecido.");
            return;
        }

        int poderItem = inventario.obterPoderItem(nomeItem);

        if (tipo.equals("arma")) {
            if (armaEquipada != null) {
                System.out.println("Você já tem uma arma equipada! Deseja substituir " + armaEquipada + "? (s/n)");
                if (confirmarAcao()) return;
                guardarNaMochila(armaEquipada);
            }
            armaEquipada = nomeItem;
            habilidade += poderItem;
        } else if (tipo.equals("armadura")) {
            if (armaduraEquipada != null) {
                System.out.println("Você já tem uma armadura equipada! Deseja substituir " + armaduraEquipada + "? (s/n)");
                if (confirmarAcao()) return;
                guardarNaMochila(armaduraEquipada);
            }
            armaduraEquipada = nomeItem;
            energia += poderItem;
        } else {
            System.out.println("Tipo de equipamento inválido.");
            return;
        }

        inventario.removerDaMochila(nomeItem);
        System.out.println(nomeItem + " equipado com sucesso!");
    }

    public void guardarNaMochila(String nomeItem) {
        if (nomeItem.equals(armaEquipada)) {
            habilidade -= inventario.obterPoderItem(nomeItem);
            armaEquipada = null;
        } else if (nomeItem.equals(armaduraEquipada)) {
            energia -= inventario.obterPoderItem(nomeItem);
            armaduraEquipada = null;
        } else {
            System.out.println("Esse item não está equipado!");
            return;
        }

        inventario.adicionarNaMochila(nomeItem, "equipamento", 0, "mochila");
        System.out.println(nomeItem + " guardado na mochila e seu efeito foi removido.");
    }

    public void guardarItemDiretoNaMochila(String nomeItem, String tipo, int poder) {
        if (inventario.temNaMochila(nomeItem)) {
            System.out.println("O item " + nomeItem + " já está na mochila!");
            return;
        }

        inventario.adicionarNaMochila(nomeItem, tipo, poder, "mochila");
        System.out.println(nomeItem + " foi guardado diretamente na mochila.");
    }


    private boolean confirmarAcao() {
        Scanner scanner = new Scanner(System.in);
        String resposta = scanner.nextLine().trim().toLowerCase();
        return !resposta.equals("s");
    }

    public void restaurarHabilidade() {
        habilidade = habilidadeMaxima;
    }

    public void restaurarEnergia() {
        energia = energiaMaxima;
    }

    public void aumentarSorte() {
        sorte = sorteMaxima + 1;
    }

    public boolean estaVivo() {
        return energia > 0;
    }

    public boolean testarSorte() {
        int resultado = (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
        boolean sucesso = resultado <= sorte;
        reduzirSorte();
        return sucesso;
    }

    public int[] atacar(boolean usarSorte) {
        int rolagemAtaque = rand.nextInt(6) + 1 + rand.nextInt(6) + 1 + habilidade;
        int danoBase = 2;

        if (usarSorte && testarSorte()) {
            danoBase += 2; // Dano aumentado ao ter sorte no ataque
            System.out.println(nome + " usou sorte no ataque e causou mais dano!");
        } else if (usarSorte) {
            danoBase -= 1; // Dano reduzido ao falhar no teste de sorte
            System.out.println(nome + " falhou no teste de sorte e causou menos dano.");
        }

        return new int[]{rolagemAtaque, danoBase};
    }

    public void reduzirSorte() {
        sorte = Math.max(0, sorte - 1);
    }

    public void receberDano(int danoRecebido) {
        energia = Math.max(0, energia - danoRecebido);
        System.out.println(nome + " recebeu " + danoRecebido + " de dano. Energia restante: " + energia);
    }

    public int getDano() {
        return 2;
    }

    public void defender(int danoMonstro, boolean usarSorteDefesa) {
        if (usarSorteDefesa){
            if (testarSorte()){
                receberDano(danoMonstro - 1);
            }else {
                receberDano(danoMonstro + 1);
            }
        }else {
            receberDano(danoMonstro);
        }
    }
}
