package br.com.calculadorainvestimentos;

import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Main {
    private static final String MARGEM = "---------------------------------------";

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

    private static void print(final ProjecaoInvestimento invest) {}

    private static void print(final ProjecaoSaque saque) {}

    private static void print(final Investimento invest) {

        final String header = MARGEM + "\nValores Iniciais\n" + MARGEM;
        final String footer = "\n\nInvestimentos\n" + MARGEM;
        final String subfooter = "\n\nSaques\n" + MARGEM;

        final AporteRestante aporteRestante = calcularAporteRestante();
        final int qtdeMaxSaques = calcularQtdeMaxSaques();

        out.println(header);

        out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(invest.getValorAporte()));
        out.println("Saque Mensal   : " + NumberFormat.getCurrencyInstance().format(invest.getValorSaque()));

        out.println("Qtde Aportes   : " + invest.getQtdeAportes());
        out.println("Qtde Saques    : " + invest.getQtdeSaques());

        out.println("Rend. Anual    : " + invest.getAliquotaAplicacao() + "%");
        out.println("Rend. Mensal   : " + formatarPercentual(idxApliMes));

        out.println("Inflacao Anual : " + invest.getAliquotaInflacao() + "%");
        out.println("Inflacao Mensal: " + formatarPercentual(idxInflMes));

        out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

        out.println(MARGEM);

        out.println(footer);
        out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
        out.println("Tempo Invest.  : " + formatarAnos(qtdeAportes));

        out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
        out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
        out.println("Inflacao Acumul: " + formatarPercentual(idxInflAcumul));
        out.println("Inflacao Mensal: " + formatarPercentual(idxInflAcumulMes));

        out.println("Rend. Final    : " + formatarPercentual(indiceGanhoFinal));
        out.println("Rend. Real     : " + formatarPercentual(indiceGanhoReal));

        out.println(subfooter);
        out.println("Primeiro Saque : " + NumberFormat.getCurrencyInstance().format(valPrimeiroSaque));
        out.println("Último Saque   : " + NumberFormat.getCurrencyInstance().format(valUltimoSaque));
        out.println("Reinvest. Mens.: " + formatarPercentual(indiceReaplicacao));

        out.println("Val. Restante  : " + NumberFormat.getCurrencyInstance().format(aporteRestante.getValor()) + " apos "
            + aporteRestante.getQtdeSaques() + " saque(s)");

        out.println("Qtde. Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ? "SEM LIMITES" : qtdeMaxSaques));
        out.println("Tempo Max. Saq.: " + (qtdeMaxSaques >= TEMPO_MAXIMO ? "SEM LIMITES" : formatarAnos(qtdeMaxSaques)));

        out.println(MARGEM);
    }

    private String formatarPercentual(final double indice) {
        return (new DecimalFormat("#.###").format(indice * 100) + "%").replace(".", ",");
    }
}
