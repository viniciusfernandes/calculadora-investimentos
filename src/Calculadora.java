import static java.lang.Math.pow;
import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Calculadora {
    public static void main(final String[] args) {

        final int anosInvestimento = 20;
        final int totalMeses = 12 * anosInvestimento;
        final double valorMensal = 5000d;
        final double valorInvestido = totalMeses * valorMensal;
        final double indiceAnual = 9.0 / 100d;
        final double indiceMensal = calcularIndiceMensal(indiceAnual);
        final double inflacaoAnual = 4.5 / 100d;
        final double inflacaoMensal = calcularIndiceMensal(inflacaoAnual);
        final double indiceReal = calcularIndiceReal(indiceMensal, inflacaoMensal);

        final int anosSaque = 25;
        final int quantidadeSaquesFuturos = 12 * anosSaque;
        final double valorSaqueFuturo = 7800.0;
        final double indiceAnualAposentadoria = 6.0 / 100d;
        final double indiceMensalAposentadoria = calcularIndiceMensal(indiceAnualAposentadoria);
        final double indiceRealAposentadoria = calcularIndiceReal(indiceMensalAposentadoria, inflacaoMensal);

        final double valorFinal = valorMensal * calcularIndiceAcumulado(totalMeses, indiceMensal);
        final double valorReal = valorMensal * calcularIndiceAcumulado(totalMeses, indiceReal);
        final double valorInflacaoIncidente = valorMensal * calcularIndiceAcumulado(totalMeses, -inflacaoMensal);
        final double inflacaoAcumulada = (valorFinal - valorReal) / valorReal;

        final double inflacaoAcumuladaMensal = 1 - Math.pow((1 - inflacaoAcumulada), 1d / totalMeses);

        final double valorBase = valorMensal * calcularIndiceAcumulado(totalMeses, -inflacaoMensal);
        final double indiceGanhoFinal = (valorFinal - valorBase) / valorBase;
        final double indiceGanhoReal = (valorReal - valorBase) / valorBase;

        final String margem = "---------------------------------------";
        final String header = margem + "\nValores Iniciais\n" + margem;
        final String footer = "\n\nInvestimentos\n" + margem;
        final String subfooter = "\n\nSaques\n" + margem;

        out.println(header);

        out.println("Dep. Mensal    : " + NumberFormat.getCurrencyInstance().format(valorMensal));
        out.println("Anos Invest.   : " + anosInvestimento);
        out.println("Indice Anual   : " + formatPercentualIndex(indiceAnual));
        out.println("Inflacao Anual : " + formatPercentualIndex(inflacaoAnual));
        out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

        out.println(margem);

        out.println(footer);
        out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
        out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
        out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
        out.println("Inflacao Acumul: " + formatPercentualIndex(inflacaoAcumulada));
        out.println("Inflacao Mensal: " + formatPercentualIndex(inflacaoAcumuladaMensal));

        out.println("Ganho Final    : " + formatPercentualIndex(indiceGanhoFinal));
        out.println("Ganho Real     : " + formatPercentualIndex(indiceGanhoReal));

        out.println(subfooter);
        out.println("Val. Saque     : " + NumberFormat.getCurrencyInstance().format(valorSaqueFuturo));
        out.println("Anos Saque     : " + anosSaque);
        out.println("Indice Anual   : " + formatPercentualIndex(indiceAnualAposentadoria));

        out.println("Val. Restante  : " + NumberFormat.getCurrencyInstance()
                        .format(calcularValorRestanteSaque(valorFinal, valorSaqueFuturo, indiceRealAposentadoria, inflacaoMensal,
                                        quantidadeSaquesFuturos)));

        out.println(margem);

    }

    public static double calcularIndiceAcumulado(final int frequencia, final double fator) {
        double indiceAcumulado = -1;
        double indice = (1 + fator);

        if (frequencia <= 0) {
            indiceAcumulado = 1;
        } else if (frequencia == 1) {
            indiceAcumulado = indice + 1;
        } else {
            indiceAcumulado = indice + 1;
            for (int i = 2; i <= frequencia; i++) {
                indice *= (1 + fator);
                indiceAcumulado += indice;
            }
        }
        return indiceAcumulado;
    }

    private static String formatPercentualIndex(final double index) {
        return (new DecimalFormat("#.###").format(index * 100) + "%").replace(".", ",");
    }

    public static double calcularValorRestanteSaque(final double montante, final double valorSaque, final double idxReal,
                    final double idxInflacao, final int totalSaques) {
        if (totalSaques <= 0) {
            return montante;
        }

        final double idxRendimentoAcumulado = pow((1 + idxReal), totalSaques);
        double idxSaqueAcumulado = 0;
        for (int j = 0; j < totalSaques; j++) {
            idxSaqueAcumulado += pow((1 + idxReal), j) * pow((1 + idxInflacao), totalSaques - 1 - j);
        }

        return montante * idxRendimentoAcumulado - valorSaque * idxSaqueAcumulado;
    }

    public static double calcularIndiceMensal(final double indice) {
        return 1 - Math.pow((1 - indice), 1d / 12d);
    }

    public static double calcularIndiceReal(final double indiceRendimento, final double indiceInflacao) {
        return (1 + indiceRendimento) / (1 + indiceInflacao) - 1;
    }
}
