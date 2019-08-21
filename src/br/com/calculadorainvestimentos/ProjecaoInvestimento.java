package br.com.calculadorainvestimentos;

public class ProjecaoInvestimento {
    private int qtdeAportes;
    private double valorFinal;
    private double valorReal;
    private double valorInvestidoComInflacao;
    private double valorInvestido;
    private double aliquotaInflacaoMes;
    private double aliquotaInflacaoAcumulada;
    private double aliquotaInflacaoAcumuladaMes;
    private double aliquotaGanhoFinal;
    private double aliquotaGanhoReal;
    private double aliquotaAplicacaoMes;

    public int getQtdeAportes() {
        return qtdeAportes;
    }

    public void setQtdeAportes(final int qtdeAportes) {
        this.qtdeAportes = qtdeAportes;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(final double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public double getValorReal() {
        return valorReal;
    }

    public void setValorReal(final double valorReal) {
        this.valorReal = valorReal;
    }

    public double getValorInvestidoComInflacao() {
        return valorInvestidoComInflacao;
    }

    public void setValorInvestidoComInflacao(final double valorInvestidoComInflacao) {
        this.valorInvestidoComInflacao = valorInvestidoComInflacao;
    }

    public double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(final double valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public double getAliquotaInflacaoAcumulada() {
        return aliquotaInflacaoAcumulada;
    }

    public void setAliquotaInflacaoAcumulada(final double aliquotaInflacaoAcumulada) {
        this.aliquotaInflacaoAcumulada = aliquotaInflacaoAcumulada;
    }

    public double getAliquotaInflacaoAcumuladaMes() {
        return aliquotaInflacaoAcumuladaMes;
    }

    public void setAliquotaInflacaoAcumuladaMes(final double aliquotaInflacaoAcumuladaMes) {
        this.aliquotaInflacaoAcumuladaMes = aliquotaInflacaoAcumuladaMes;
    }

    public double getAliquotaGanhoFinal() {
        return aliquotaGanhoFinal;
    }

    public void setAliquotaGanhoFinal(final double aliquotaGanhoFinal) {
        this.aliquotaGanhoFinal = aliquotaGanhoFinal;
    }

    public double getAliquotaGanhoReal() {
        return aliquotaGanhoReal;
    }

    public void setAliquotaGanhoReal(final double aliquotaGanhoReal) {
        this.aliquotaGanhoReal = aliquotaGanhoReal;
    }

    public double getAliquotaInflacaoMes() {
        return aliquotaInflacaoMes;
    }

    public void setAliquotaInflacaoMes(final double aliquotaInflacaoMes) {
        this.aliquotaInflacaoMes = aliquotaInflacaoMes;
    }

    public double getAliquotaAplicacaoMes() {
        return aliquotaAplicacaoMes;
    }

    public void setAliquotaAplicacaoMes(final double aliquotaAplicacaoMes) {
        this.aliquotaAplicacaoMes = aliquotaAplicacaoMes;
    }

}
