package br.com.calculadorainvestimentos;

public class FluxoInvestimentos {
    private final double indiceAplicacao;
    private final double indiceReaplicacao;
    private final double indiceIR;
    private final double indiceInflacao;
    private final int qtdeAportes;
    private final int qtdeSaques;
    private final double valorAporte;

    public FluxoInvestimentos(
                    final double aliquotaAplicacao, final double aliquotaReaplicacao, final double aliquotaIR,
                    final double aliquotaInflacao, final int qtdeAportes, final int qtdeSaques, final double valorAporte) {
        indiceAplicacao = aliquotaAplicacao / 100;
        indiceReaplicacao = aliquotaReaplicacao / 100;
        indiceIR = aliquotaIR / 100;
        indiceInflacao = aliquotaInflacao / 100;
        this.qtdeAportes = qtdeAportes;
        this.qtdeSaques = qtdeSaques;
        this.valorAporte = valorAporte;
    }

    public double getIndiceAplicacao() {
        return indiceAplicacao;
    }

    public double getIndiceReaplicacao() {
        return indiceReaplicacao;
    }

    public double getIndiceIR() {
        return indiceIR;
    }

    public double getIndiceInflacao() {
        return indiceInflacao;
    }

    public int getQtdeAportes() {
        return qtdeAportes;
    }

    public int getQtdeSaques() {
        return qtdeSaques;
    }

    public double getValorAporte() {
        return valorAporte;
    }

}
