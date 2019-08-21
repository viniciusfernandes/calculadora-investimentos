package br.com.calculadorainvestimentos;

import static java.lang.Math.pow;
import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraInvestimento {

    private final List<Aporte> aportes = new ArrayList<>();
    private final double indiceAplicacao;
    private final double indiceInflacao;

    private final double indiceIR;
    private final double indiceReaplicacao;
    private final int qtdeAportes;
    private final int qtdeSaques;
    private final List<Double> saques = new ArrayList<>();
    private final double valorAporte;
    private final double valorSaque;
    private final static int TEMPO_MAXIMO = 1000;

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

        final double idxApliMes = calcularIndiceMensal(indiceAplicacao);
        final double idxInflMes = calcularIndiceMensal(indiceInflacao);
        final double idxReal = calcularIndiceReal(idxApliMes, idxInflMes);

        final double valorFinal = calcularValorFinal();
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

        final double valorInvestido = calcularValorInvestido();

        final String margem = "---------------------------------------";
        final String header = margem + "\nValores Iniciais\n" + margem;
        final String footer = "\n\nInvestimentos\n" + margem;
        final String subfooter = "\n\nSaques\n" + margem;

        final AporteRestante aporteRestante = calcularAporteRestante();
        final int qtdeMaxSaques = calcularQtdeMaxSaques();

        out.println(header);

        out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(valorAporte));
        out.println("Saque Mensal   : " + NumberFormat.getCurrencyInstance().format(valorSaque));

        out.println("Qtde Aportes   : " + qtdeAportes);
        out.println("Qtde Saques    : " + qtdeSaques);

        out.println("Rend. Anual    : " + formatPercentualIndex(indiceAplicacao));
        out.println("Rend. Mensal   : " + formatPercentualIndex(idxApliMes));

        out.println("Inflacao Anual : " + formatPercentualIndex(indiceInflacao));
        out.println("Inflacao Mensal: " + formatPercentualIndex(idxInflMes));

        out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

        out.println(margem);

        out.println(footer);
        out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
        out.println("Tempo Invest.  : " + formatarAnos(qtdeAportes));

        out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
        out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
        out.println("Inflacao Acumul: " + formatPercentualIndex(idxInflAcumul));
        out.println("Inflacao Mensal: " + formatPercentualIndex(idxInflAcumulMes));

        out.println("Rend. Final    : " + formatPercentualIndex(indiceGanhoFinal));
        out.println("Rend. Real     : " + formatPercentualIndex(indiceGanhoReal));

        out.println(subfooter);
        out.println("Primeiro Saque : " + NumberFormat.getCurrencyInstance().format(valPrimeiroSaque));
        out.println("Último Saque   : " + NumberFormat.getCurrencyInstance().format(valUltimoSaque));
        out.println("Reinvest. Mens.: " + formatPercentualIndex(indiceReaplicacao));

        out.println("Val. Restante  : " + NumberFormat.getCurrencyInstance().format(aporteRestante.getValor()) + " apos "
            + aporteRestante.getQtdeSaques() + " saque(s)");

        out.println("Qtde. Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ? "SEM LIMITES" : qtdeMaxSaques));
        out.println("Tempo Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ? "SEM LIMITES" : formatarAnos(qtdeMaxSaques)));

        out.println(margem);
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

    private AporteRestante calcularAporteRestante() {
        final double indiceAplicacaoMes = calcularIndiceMensal(indiceAplicacao);
        final double indiceInflacaoMes = calcularIndiceMensal(indiceInflacao);
        final double indiceReaplicacaoMes = calcularIndiceMensal(indiceReaplicacao);
        final int idxSaqueInicial = qtdeAportes;

        final double valorFinal = valorAporte * calcularIndiceAcumulado(qtdeAportes, indiceAplicacaoMes);
        final double valorSaqueFuturo = valorSaque * pow((1 + indiceInflacaoMes), idxSaqueInicial);
        return calcularAporteRestante(valorFinal, valorSaqueFuturo, indiceInflacaoMes, indiceReaplicacaoMes, qtdeSaques, 0);
    }

    private AporteRestante calcularAporteRestante(double valorFinal, double valorSaque, final double indiceInflacao,
                    final double indiceReaplicacao, final int numMaxSaque, int numSaque) {
        valorFinal -= valorSaque;
        numSaque++;
        if (valorFinal < 0) {
            return new AporteRestante(valorFinal + valorSaque, numSaque - 1);
        } else if (valorFinal == 0 || numSaque >= numMaxSaque) {
            return new AporteRestante(valorFinal, numSaque);
        }

        valorSaque *= (1 + indiceInflacao);
        valorFinal *= (1 + indiceReaplicacao);
        return calcularAporteRestante(valorFinal, valorSaque, indiceInflacao, indiceReaplicacao, numMaxSaque, numSaque);
    }

    private double calcularValorInvestido() {
        double val = 0;
        for (final Aporte aporte : aportes) {
            val += aporte.getValorInicial();
        }
        return val;
    }

    private int calcularQtdeMaxSaques() {
        final double indiceAplicacaoMes = calcularIndiceMensal(indiceAplicacao);
        final double indiceInflacaoMes = calcularIndiceMensal(indiceInflacao);
        final double indiceReaplicacaoMes = calcularIndiceMensal(indiceReaplicacao);
        final int idxSaqueInicial = qtdeAportes;

        final double valorFinal = valorAporte * calcularIndiceAcumulado(qtdeAportes, indiceAplicacaoMes);
        final double valorSaqueFuturo = valorSaque * pow((1 + indiceInflacaoMes), idxSaqueInicial);
        final double indiceLucro = calcularIndiceLucroMedio();
        return calcularQtdeMaxSaques(valorFinal, valorSaqueFuturo, indiceInflacaoMes, indiceReaplicacaoMes, indiceLucro, 0);
    }

    private int calcularQtdeMaxSaques(double valorFinal, double valorSaque, final double indiceInflacao, final double indiceReaplicacao,
                    final double indiceLucro, int numSaque) {
        // Subtranido o valor do IR incidente no saque.
        valorFinal -= (valorSaque + valorSaque * indiceLucro * indiceIR);
        numSaque++;
        if (valorFinal < 0) {
            return --numSaque;
        } else if (valorFinal == 0 || numSaque >= TEMPO_MAXIMO) {
            return numSaque;
        }

        valorSaque *= (1 + indiceInflacao);
        valorFinal *= (1 + indiceReaplicacao);

        return calcularQtdeMaxSaques(valorFinal, valorSaque, indiceInflacao, indiceReaplicacao, indiceLucro, numSaque);
    }

    private String formatPercentualIndex(final double index) {
        return (new DecimalFormat("#.###").format(index * 100) + "%").replace(".", ",");
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
