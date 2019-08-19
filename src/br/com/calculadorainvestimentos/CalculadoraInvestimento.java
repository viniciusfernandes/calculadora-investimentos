package br.com.calculadorainvestimentos;

import static java.lang.Math.pow;
import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraInvestimento {

    private final List<Double> aportes = new ArrayList<>();
    private final List<Double> saques = new ArrayList<>();
    private final List<Double> restantes = new ArrayList<>();

    private final double indiceAplicacao;
    private final double indiceReaplicacao;
    private final double indiceIR;
    private final double indiceInflacao;
    private final int qtdeAportes;
    private final int qtdeSaques;
    private final double valorAporte;
    private final double valorSaque;

    public CalculadoraInvestimento(
                    final double aliquotaAplicacao, final double aliquotaReaplicacao, final double aliquotaIR,
                    final double aliquotaInflacao, final int qtdeAportes, final int qtdeSaques, final double valorAporte,
                    final double valorSaque) {
        indiceAplicacao = aliquotaAplicacao / 100;
        indiceReaplicacao = aliquotaReaplicacao / 100;
        indiceIR = aliquotaIR / 100;
        indiceInflacao = aliquotaInflacao / 100;
        this.qtdeAportes = qtdeAportes;
        this.qtdeSaques = qtdeSaques;
        this.valorAporte = valorAporte;
        this.valorSaque = valorSaque;
    }

    public void calcular() {

        final double valorInvestido = qtdeAportes * valorAporte;

        final double idxApliMes = calcularIndiceMensal(indiceAplicacao);
        final double idxInflMes = calcularIndiceMensal(indiceInflacao);
        final double idxReal = calcularIndiceReal(idxApliMes, idxInflMes);

        final double idxReapliMes = calcularIndiceMensal(indiceReaplicacao);

        final double valorFinal = valorAporte * calcularIndiceAcumulado(qtdeAportes, idxApliMes);
        final double valorReal = valorAporte * calcularIndiceAcumulado(qtdeAportes, idxReal);
        final double valorInflacaoIncidente = valorAporte * calcularIndiceAcumulado(qtdeAportes, -idxInflMes);
        final double idxInflAcumul = (valorFinal - valorReal) / valorReal;

        final double idxInflAcumulMes = 1d - Math.pow(1 - idxInflAcumul, 1d / qtdeAportes);

        final double valorBase = valorAporte * calcularIndiceAcumulado(qtdeAportes, -idxInflMes);
        final double indiceGanhoFinal = (valorFinal - valorBase) / valorBase;
        final double indiceGanhoReal = (valorReal - valorBase) / valorBase;

        final double valPrimeiroSaque = valorSaque * pow(1 + idxInflMes, qtdeAportes);
        final double valUltimoSaque = valorSaque * pow(1 + idxInflMes, qtdeAportes + qtdeSaques - 1);

        inicializarAportes(idxApliMes);
        inicializarSaques(idxInflMes);

        final String margem = "---------------------------------------";
        final String header = margem + "\nValores Iniciais\n" + margem;
        final String footer = "\n\nInvestimentos\n" + margem;
        final String subfooter = "\n\nSaques\n" + margem;

        final double[] valoresRestantes = calcularQtdeMaximaSaques(valorFinal, valorSaque, idxApliMes, idxReapliMes, indiceIR, idxInflMes);

        final double valorRestante = valoresRestantes[0];
        final int qtdeMaxSaques = (int) valoresRestantes[1];

        out.println(header);

        out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(valorAporte));
        out.println("Anos Invest.   : " + qtdeAportes / 12);
        out.println("Rend. Anual    : " + formatPercentualIndex(indiceAplicacao));
        out.println("Rend. Mensal   : " + formatPercentualIndex(idxApliMes));

        out.println("Inflacao Anual : " + formatPercentualIndex(indiceInflacao));
        out.println("Inflacao Mensal: " + formatPercentualIndex(idxInflMes));

        out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

        out.println(margem);

        out.println(footer);
        out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
        out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
        out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
        out.println("Inflacao Acumul: " + formatPercentualIndex(idxInflAcumul));
        out.println("Inflacao Mensal: " + formatPercentualIndex(idxInflAcumulMes));

        out.println("Rend. Final    : " + formatPercentualIndex(indiceGanhoFinal));
        out.println("Rend. Real     : " + formatPercentualIndex(indiceGanhoReal));

        out.println(subfooter);
        out.println("Saque          : " + NumberFormat.getCurrencyInstance().format(valorSaque));
        out.println("Primeiro Saque : " + NumberFormat.getCurrencyInstance().format(valPrimeiroSaque));
        out.println("Último Saque  : " + NumberFormat.getCurrencyInstance().format(valUltimoSaque));
        out.println("Reinvest. Mens.: " + formatPercentualIndex(indiceReaplicacao));

        out.println("Anos Saque     : " + qtdeSaques / 12);
        out.println("Anos. Max.     : " + qtdeMaxSaques / 12);
        out.println("Qtde. Saq.     : " + qtdeSaques);
        out.println("Qtde. Max. Saq.: " + qtdeMaxSaques);
        out.println("Val. Restante  : " + NumberFormat.getCurrencyInstance().format(valorRestante));

        out.println(margem);
    }

    public double calcularIndiceAcumulado(final int frequencia, final double fator) {
        double indiceAcumulado = 0;

        for (int i = 0; i < frequencia; i++) {
            indiceAcumulado += pow(1 + fator, i);
        }

        return indiceAcumulado;
    }

    public double calcularIndiceMensal(final double indice) {
        return Math.pow(1 + indice, 1d / 12d) - 1;
    }

    public double calcularIndiceReal(final double indiceRendimento, final double indiceInflacao) {
        return (1 + indiceRendimento) / (1 + indiceInflacao) - 1;
    }

    public double[] calcularQtdeMaximaSaques(final double valorInicial, final double valorSaque, final double idxRendInicial,
                    final double idxRendAtual, final double idxIR, final double idxInflacao) {
        int saque = 0;
        int totSaques = 0;
        double valFinal;
        double valRestante = -1;

        while (true) {
            valFinal = calcularValorRestante(valorInicial, valorSaque, ++saque, idxRendInicial, idxRendAtual, idxIR, idxInflacao);
            if (valFinal <= 0) {
                return new double[] {valRestante, totSaques};
            } else {
                valRestante = valFinal;
                totSaques = saque;
            }
        }
    }

    private double calcularValorRestante(final double montanteInicial, final double valorSaque, final int qtdeSaques,
                    final double idxRendInicial, final double idxRendAtual, final double idxIR, final double idxInflacao) {
        return calcularValorRestante(montanteInicial, valorSaque, idxRendInicial, idxRendAtual, idxIR, idxInflacao, 0, 1);
    }

    private void calcularReinvestimento() {
        double aporte = -1;
        for (int i = 0; i < aportes.size(); i++) {
            aporte = aportes.get(i);
            aporte *= (1 + indiceReaplicacao);
            aportes.add(i, aporte);
        }
    }

    private double calcularValorRestante(double valorRestante, double valorSaque, final double idxRendInicial, final double idxRendAtual,
                    final double idxIR, final double idxInflacao, double valorIR, final int idxSaque) {

        if (idxSaque > qtdeSaques) {
            return valorRestante;
        }

        valorSaque *= pow(1 + idxInflacao, qtdeAportes + idxSaque);

        valorIR = valorSaque * (pow(1 + idxRendInicial, qtdeSaques - idxSaque) - 1d);
        valorRestante = valorRestante - (valorSaque + valorIR);

        // return calcularValorRestante(valorRestante, valorSaque, qtdeSaques, idxRendInicial,
        // idxRendAtual, idxIR, idxInflacao, valorIR,
        // ++idxSaque);
        return 8d;
    }

    private String formatPercentualIndex(final double index) {
        return (new DecimalFormat("#.###").format(index * 100) + "%").replace(".", ",");
    }

    private void inicializarAportes(final double idxApliMes) {
        for (int i = qtdeAportes - 1; i >= 1; i--) {
            aportes.add(valorAporte * pow(1 + idxApliMes, i));
        }
    }

    private void inicializarSaques(final double idxInflacaoMes) {
        final int idxPrimeiroSaque = qtdeAportes;
        final int idxUltimoSaque = idxPrimeiroSaque + qtdeSaques - 1;
        for (int i = idxPrimeiroSaque; i <= idxUltimoSaque; i++) {
            saques.add(valorSaque * pow((1 + idxInflacaoMes), i));
        }
    }
    
    
}
