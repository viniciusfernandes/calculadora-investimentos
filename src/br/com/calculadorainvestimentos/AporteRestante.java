package br.com.calculadorainvestimentos;

public class AporteRestante {
    private final double valor;
    private final int qtdeSaques;

    public AporteRestante(final double valor, final int qtdeSaques) {
        this.valor = valor;
        this.qtdeSaques = qtdeSaques;
    }

    public double getValor() {
        return valor;
    }

    public int getQtdeSaques() {
        return qtdeSaques;
    }
}
