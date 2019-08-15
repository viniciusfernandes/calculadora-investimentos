import static java.lang.Math.pow;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Calculadora {
	public static void main(final String[] args) {

		final int totalMeses = 12 * 20;
		final double valorMensal = 5000d;
		final double valorInvestido = totalMeses * valorMensal;
		final double indiceAnual = 9.0 / 100d;
		final double indiceMensal = calcularIndiceMensal(indiceAnual);
		final double inflacaoAnual = 4.5 / 100d;
		final double inflacaoMensal = calcularIndiceMensal(inflacaoAnual);
		final double indiceReal = calcularIndiceReal(indiceMensal, inflacaoMensal);

		final double indiceAnualAposentadoria = 6.0 / 100d;
		final double indiceMensalAposentadoria = calcularIndiceMensal(indiceAnualAposentadoria);
		final double indiceRealAposentadoria = calcularIndiceReal(indiceMensalAposentadoria, inflacaoMensal);
		final int quantidadeSaquesFuturos = 12 * 25;
		final double valorSaqueFuturo = 7800.0;

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

		System.out.println(header);

		System.out.println("Dep. Mensal    : " + NumberFormat.getCurrencyInstance().format(valorMensal));
		System.out.println("Total Meses    : " + totalMeses);
		System.out.println("Indice Anual   : " + formatPercentualIndex(indiceAnual));
		System.out.println("Inflacao Anual : " + formatPercentualIndex(inflacaoAnual));
		System.out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

		System.out.println(margem);

		System.out.println(footer);
		System.out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
		System.out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
		System.out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
		System.out.println("Inflacao Acumul: " + formatPercentualIndex(inflacaoAcumulada));
		System.out.println("Inflacao Mensal: " + formatPercentualIndex(inflacaoAcumuladaMensal));

		System.out.println("Ganho Final    : " + formatPercentualIndex(indiceGanhoFinal));
		System.out.println("Ganho Real     : " + formatPercentualIndex(indiceGanhoReal));

		System.out.println(subfooter);
		System.out.println("Val. Saque     : " + NumberFormat.getCurrencyInstance().format(valorSaqueFuturo));
		System.out.println("Total Meses    : " + quantidadeSaquesFuturos);
		System.out.println("Indice Anual   : " + formatPercentualIndex(indiceAnualAposentadoria));

		System.out.println(
				"Val. Restante  : " + NumberFormat.getCurrencyInstance().format(calcularValorRestanteSaque(valorFinal,
						valorSaqueFuturo, indiceRealAposentadoria, inflacaoMensal, quantidadeSaquesFuturos)));

		System.out.println(margem);

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

	public static double calcularValorRestanteSaque(double montante, double valorSaque, double idxReal,
			double idxInflacao, int totalSaques) {
		if (totalSaques <= 0) {
			return montante;
		}

		double idxRendimentoAcumulado = pow((1 + idxReal), (double) totalSaques);
		double idxSaqueAcumulado = 0;
		for (int j = 0; j < totalSaques; j++) {
			idxSaqueAcumulado += pow((1 + idxReal), j) * pow((1 + idxInflacao), (double) (totalSaques - 1 - j));
		}

		return montante * idxRendimentoAcumulado - valorSaque * idxSaqueAcumulado;
	}

	public static double calcularIndiceMensal(double indice) {
		return 1 - Math.pow((1 - indice), 1d / 12d);
	}

	public static double calcularIndiceReal(double indiceRendimento, double indiceInflacao) {
		return (1 + indiceRendimento) / (1 + indiceInflacao) - 1;
	}
}
