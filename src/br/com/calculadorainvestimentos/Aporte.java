package br.com.calculadorainvestimentos;

public class Aporte {
    private double valor;
    private double indiceRendiento;
    private int tempoAplicacao;

    public double getValor() {
        return valor;
    }

    public void setValor(final double valor) {
        this.valor = valor;
    }

    public double getIndiceRendiento() {
        return indiceRendiento;
    }

    public void setIndiceRendiento(final double indiceRendiento) {
        this.indiceRendiento = indiceRendiento;
    }

    public int getTempoAplicacao() {
        return tempoAplicacao;
    }

    public void setTempoAplicacao(final int tempoAplicacao) {
        this.tempoAplicacao = tempoAplicacao;
    }

}
