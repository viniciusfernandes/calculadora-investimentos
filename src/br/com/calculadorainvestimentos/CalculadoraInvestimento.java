package br.com.calculadorainvestimentos;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraInvestimento {

    private final List<Aporte> aportes = new ArrayList<>();
    private double indiceAplicacao;
    private double indiceInflacao;

    private double indiceIR;
    private double indiceReaplicacao;
    private int qtdeAportes;
    private int qtdeSaques;
    private final List<Double> saques = new ArrayList<>();
    private double valorAporte;
    private double valorSaque;
    private static final int TEMPO_MAXIMO = 1000;

    private double indiceAplicacaoMes;
    private double indiceInflacaoMes;
    private double idxReal;

    private double valorFinal;
    private double valorInvestido;
    private double valorReal;
    private double valorInvestidoComInflacao;

    private double indiceGanhoFinal;
    private double indiceGanhoReal;

    private int qtdeMaxSaques;
    private double valorRestante;

    private void inicializarIndices(final Investimento investimento) {
        indiceAplicacao = investimento.getAliquotaAplicacao() / 100;
        indiceReaplicacao = investimento.getAliquotaReaplicacao() / 100;
        indiceIR = investimento.getAliquotaIR() / 100;
        indiceInflacao = investimento.getAliquotaInflacao() / 100;
        qtdeAportes = investimento.getQtdeAportes();
        qtdeSaques = investimento.getQtdeSaques();
        valorAporte = investimento.getValorAporte();
        valorSaque = investimento.getValorSaque();

    }

    private void calcularIndicesMensaisEValores() {
        indiceAplicacaoMes = calcularIndiceMensal(indiceAplicacao);
        indiceInflacaoMes = calcularIndiceMensal(indiceInflacao);
        idxReal = calcularIndiceReal(indiceAplicacaoMes, indiceInflacaoMes);

        valorFinal = calcularValorFinal();
        valorInvestido = calcularValorInvestido();
        valorReal = valorAporte * calcularIndiceAcumulado(qtdeAportes, idxReal);
        valorInvestidoComInflacao = valorAporte * calcularIndiceAcumulado(qtdeAportes, -indiceInflacaoMes);

        indiceGanhoFinal = (valorFinal - valorInvestido) / valorInvestido;
        indiceGanhoReal = (valorReal - valorInvestido) / valorInvestido;
    }

    public FluxoInvestimento calcular(final Investimento investimento) {
        inicializarIndices(investimento);
        calcularIndicesMensaisEValores();
        calcularQtdeMaxSaquesEValorRestante();

        final double idxInflAcumul = (valorFinal - valorReal) / valorReal;

        final double idxInflAcumulMes = 1d - Math.pow(1 - idxInflAcumul, 1d / qtdeAportes);

        final double valPrimeiroSaque = valorSaque * pow(1 + indiceInflacaoMes, qtdeAportes);
        final double valUltimoSaque = valorSaque * pow(1 + indiceInflacaoMes, qtdeAportes + qtdeSaques - 1);

        inicializarAportes(indiceAplicacaoMes);
        inicializarSaques(indiceInflacaoMes);

        return null;
    }

    private double calcularIndiceAcumulado(final int frequencia, final double fator) {
        double indiceAcumulado = 0;

        for (int i = 0; i < frequencia; i++) {
            indiceAcumulado += pow(1 + fator, i);
        }

        return indiceAcumulado;
    }

    private double calcularIndiceMensal(final double indice) {
        return calcularIndiceEquivalente(indice, 12);
    }

    private double calcularIndiceEquivalente(final double indice, final int periodo) {
        return Math.pow(1 + indice, 1d / periodo) - 1;
    }

    private double calcularIndiceReal(final double indiceRendimento, final double indiceInflacao) {
        return (1 + indiceRendimento) / (1 + indiceInflacao) - 1;
    }

    private double calcularValorInvestido() {
        double val = 0;
        for (int i = 1; i <= qtdeAportes; i++) {
            val += valorAporte;

        }
        return val;
    }

    private void calcularQtdeMaxSaquesEValorRestante() {
        // final double indiceInflacaoMes = calcularIndiceMensal(indiceInflacao);
        final double indiceReaplicacaoMes = calcularIndiceMensal(indiceReaplicacao);
        final int idxSaqueInicial = qtdeAportes;

        final double valorSaqueFuturo = valorSaque * pow((1 + indiceInflacaoMes), idxSaqueInicial);
        final double indiceLucro = calcularIndiceLucroMedio();
        calcularQtdeMaxSaques(valorFinal, valorSaqueFuturo, indiceInflacaoMes, indiceReaplicacaoMes, indiceLucro, 0);
    }

    private void calcularQtdeMaxSaques(double valorFinal, double valorSaque, final double indiceInflacao, final double indiceReaplicacao,
                    final double indiceLucro, int numSaque) {
        valorRestante = valorFinal;
        qtdeMaxSaques = ++numSaque;
        // Subtranido o valor do IR incidente no saque.
        valorFinal -= (valorSaque + valorSaque * indiceLucro * indiceIR);

        if (valorFinal < 0) {
            qtdeMaxSaques = --numSaque;
            return;
        } else if (valorFinal == 0 || numSaque >= TEMPO_MAXIMO) {
            return;
        }

        valorSaque *= (1 + indiceInflacao);
        valorFinal *= (1 + indiceReaplicacao);

        calcularQtdeMaxSaques(valorFinal, valorSaque, indiceInflacao, indiceReaplicacao, indiceLucro, numSaque);
    }

    private void inicializarAportes(final double idxApliMes) {
        aportes.clear();

        Aporte aporte = null;
        double valor = 0;
        for (int posicao = qtdeAportes - 1; posicao >= 0; posicao--) {
            valor = valorAporte * pow(1 + idxApliMes, posicao);
            aporte = new Aporte();
            aporte.setValorInicial(valorAporte);
            aporte.setPosicao(posicao);
            aporte.setValor(valor);
            aporte.setValorRestante(valor);
            aportes.add(aporte);
        }
    }

    private void inicializarSaques(final double idxInflacaoMes) {
        saques.clear();
        final int idxPrimeiroSaque = qtdeAportes;
        final int idxUltimoSaque = idxPrimeiroSaque + qtdeSaques - 1;
        for (int i = idxPrimeiroSaque; i <= idxUltimoSaque; i++) {
            saques.add(valorSaque * pow(1 + idxInflacaoMes, i));
        }
    }

    private String formatarAnos(final int qtdeMeses) {
        return qtdeMeses / 12 + " anos e " + qtdeMeses % 12 + " meses";
    }

    private double calcularIndiceLucroMedio() {
        final double valorInvestido = calcularValorInvestido();
        final double valorFinal = calcularValorFinal();
        final double indiceLucro = (valorFinal / valorInvestido - 1);
        return calcularIndiceEquivalente(indiceLucro, qtdeAportes);
    }

    private double calcularValorFinal() {
        final double idxAplicMes = calcularIndiceMensal(indiceAplicacao);
        return valorAporte * calcularIndiceAcumulado(qtdeAportes, idxAplicMes);
    }
}
