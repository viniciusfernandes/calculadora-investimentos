package br.com.calculadorainvestimentos;

import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Main {
    private static final String MARGEM = "---------------------------------------";
    private static final DecimalFormat DF = new DecimalFormat("#.###");

    public static void main(final String[] args) {
        final Investimento investimento = new Investimento();
        investimento.setAliquotaAplicacao(9.0);
        investimento.setAliquotaInflacao(4.5);
        investimento.setAliquotaIR(10.0);
        investimento.setAliquotaReaplicacao(6.0);
        investimento.setQtdeAportes(12 * 20);
        investimento.setQtdeSaques(12 * 25);
        investimento.setValorAporte(5000.0);
        investimento.setValorSaque(4000.0);

        final FluxoInvestimento fluxo = new CalculadoraInvestimento().calcular(investimento);

        print(investimento);
        print(fluxo.getProjecaoInvestimento());
        print(fluxo.getProjecaoSaque());
    }

    private static void print(final ProjecaoInvestimento projecaoInvest) {
        out.println("\n\nInvestimentos\n" + MARGEM);
        out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorInvestido()));
        out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorFinal()));
        out.println("Rend. Mensal   : " + formatarAliquota(projecaoInvest.getAliquotaAplicacaoMes()));
        out.println("Tempo Invest.  : " + formatarTempo(projecaoInvest.getQtdeAportes()));

        out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorReal()));
        out.println("Val. c/ Infl.  : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorInvestidoComInflacao()));
        out.println("Inflacao Mensal: " + formatarAliquota(projecaoInvest.getAliquotaInflacaoMes()));

        out.println("Infl. Acumul   : " + formatarAliquota(projecaoInvest.getAliquotaInflacaoAcumulada()));
        out.println("Infl. Acum. Mes: " + formatarAliquota(projecaoInvest.getAliquotaInflacaoAcumuladaMes()));

        out.println("Rend. Final    : " + formatarAliquota(projecaoInvest.getAliquotaGanhoFinal()));
        out.println("Rend. Real     : " + formatarAliquota(projecaoInvest.getAliquotaGanhoReal()));
    }

    private static void print(final ProjecaoSaque saque) {
        /*
         * out.println("\n\nSaques\n" + MARGEM); out.println("Primeiro Saque : " +
         * NumberFormat.getCurrencyInstance().format(valPrimeiroSaque));
         * out.println("Último Saque   : " +
         * NumberFormat.getCurrencyInstance().format(valUltimoSaque));
         * out.println("Reinvest. Mens.: " + formatarAliquota(indiceReaplicacao));
         * 
         * out.println("Val. Restante  : " +
         * NumberFormat.getCurrencyInstance().format(aporteRestante.getValor()) + " apos " +
         * aporteRestante.getQtdeSaques() + " saque(s)");
         * 
         * out.println("Qtde. Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ? "SEM LIMITES" :
         * qtdeMaxSaques)); out.println("Tempo Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ?
         * "SEM LIMITES" : formatarTempo(qtdeMaxSaques)));
         * 
         * out.println(MARGEM);
         */
    }

    private static void print(final Investimento invest) {
        out.println(MARGEM + "\nValores Iniciais\n" + MARGEM);
        out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(invest.getValorAporte()));
        out.println("Saque Mensal   : " + NumberFormat.getCurrencyInstance().format(invest.getValorSaque()));
        out.println("Qtde Aportes   : " + invest.getQtdeAportes());
        out.println("Qtde Saques    : " + invest.getQtdeSaques());
        out.println("Rend. Anual    : " + formatarAliquota(invest.getAliquotaAplicacao()));
        out.println(MARGEM);
    }

    private static String formatarAliquota(final double aliquota) {
        return (DF.format(aliquota) + "%").replace(".", ",");
    }

    private static String formatarTempo(final int qtdeMeses) {
        return qtdeMeses / 12 + " anos e " + qtdeMeses % 12 + " meses";
    }
}
