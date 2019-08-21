package br.com.calculadorainvestimentos;

public class Investimento {
    private double aliquotaAplicacao;
    private double aliquotaAplicacaoMes;
    private double aliquotaReaplicacao;
    private double aliquotaReaplicacaoMes;
    private double aliquotaIR;
    private double aliquotaInflacao;

    private int qtdeAportes;
    private int qtdeSaques;
    private double valorAporte;
    private double valorSaque;

    public double getAliquotaAplicacao() {
        return aliquotaAplicacao;
    }

    public void setAliquotaAplicacao(final double aliquotaAplicacao) {
        this.aliquotaAplicacao = aliquotaAplicacao;
    }

    public double getAliquotaReaplicacao() {
        return aliquotaReaplicacao;
    }

    public void setAliquotaReaplicacao(final double aliquotaReaplicacao) {
        this.aliquotaReaplicacao = aliquotaReaplicacao;
    }

    public double getAliquotaIR() {
        return aliquotaIR;
    }

    public void setAliquotaIR(final double aliquotaIR) {
        this.aliquotaIR = aliquotaIR;
    }

    public double getAliquotaInflacao() {
        return aliquotaInflacao;
    }

    public void setAliquotaInflacao(final double aliquotaInflacao) {
        this.aliquotaInflacao = aliquotaInflacao;
    }

    public int getQtdeAportes() {
        return qtdeAportes;
    }

    public void setQtdeAportes(final int qtdeAportes) {
        this.qtdeAportes = qtdeAportes;
    }

    public int getQtdeSaques() {
        return qtdeSaques;
    }

    public void setQtdeSaques(final int qtdeSaques) {
        this.qtdeSaques = qtdeSaques;
    }

    public double getValorAporte() {
        return valorAporte;
    }

    public void setValorAporte(final double valorAporte) {
        this.valorAporte = valorAporte;
    }

    public double getValorSaque() {
        return valorSaque;
    }

    public void setValorSaque(final double valorSaque) {
        this.valorSaque = valorSaque;
    }

    public double getAliquotaAplicacaoMes() {
        return aliquotaAplicacaoMes;
    }

    public void setAliquotaAplicacaoMes(final double aliquotaAplicacaoMes) {
        this.aliquotaAplicacaoMes = aliquotaAplicacaoMes;
    }

    public double getAliquotaReaplicacaoMes() {
        return aliquotaReaplicacaoMes;
    }

    public void setAliquotaReaplicacaoMes(final double aliquotaReaplicacaoMes) {
        this.aliquotaReaplicacaoMes = aliquotaReaplicacaoMes;
    }

}
