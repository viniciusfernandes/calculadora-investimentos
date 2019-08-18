import static java.lang.Math.pow;
import static java.lang.System.out;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Calculadora {
	public static void main(final String[] args) {
		new Calculadora().calcular();
	}

	private final double indiceAnual              = 10.0 / 100d;
	private final double indiceAnualAposentadoria = 6.0 / 100d;
	private final double indiceIR                 = 10.0 / 100d;
	private final double inflacaoAnual            = 4.5 / 100d;
	private final int    qtdeAportes              = 4;
	private final int    qtdeSaques               = 4;
	private final double valorAporte              = 1000d;

	private final double valorSaque               = 1000.0;

	public void calcular() {

		final double valorInvestido = qtdeAportes * valorAporte;
		final double idxRendimentoMensal = calcularIndiceMensal(indiceAnual);
		final double idxInflacaoMensal = calcularIndiceMensal(inflacaoAnual);
		final double indiceReal = calcularIndiceReal(idxRendimentoMensal, idxInflacaoMensal);

		final double idxReinvestimento = calcularIndiceMensal(indiceAnualAposentadoria);
		calcularIndiceReal(idxReinvestimento, idxInflacaoMensal);

		final double valorFinal = valorAporte * calcularIndiceAcumulado(qtdeAportes, idxRendimentoMensal);
		final double valorReal = valorAporte * calcularIndiceAcumulado(qtdeAportes, indiceReal);
		final double valorInflacaoIncidente = valorAporte * calcularIndiceAcumulado(qtdeAportes, -idxInflacaoMensal);
		final double inflacaoAcumulada = (valorFinal - valorReal) / valorReal;

		final double inflacaoAcumuladaMensal = 1d - Math.pow(1 - inflacaoAcumulada, 1d / qtdeAportes);

		final double valorBase = valorAporte * calcularIndiceAcumulado(qtdeAportes, -idxInflacaoMensal);
		final double indiceGanhoFinal = (valorFinal - valorBase) / valorBase;
		final double indiceGanhoReal = (valorReal - valorBase) / valorBase;

		final String margem = "---------------------------------------";
		final String header = margem + "\nValores Iniciais\n" + margem;
		final String footer = "\n\nInvestimentos\n" + margem;
		final String subfooter = "\n\nSaques\n" + margem;

		final double[] valoresRestantes = calcularQtdeMaximaSaques(valorFinal, valorSaque, idxRendimentoMensal,
				idxReinvestimento, indiceIR, idxInflacaoMensal);

		final double valorRestante = valoresRestantes[0];
		final int qtdeMaxSaques = (int) valoresRestantes[1];

		out.println(header);

		out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(valorAporte));
		out.println("Anos Invest.   : " + qtdeAportes / 12);
		out.println("Rend. Anual    : " + formatPercentualIndex(indiceAnual));
		out.println("Rend. Mensal   : " + formatPercentualIndex(idxRendimentoMensal));

		out.println("Inflacao Anual : " + formatPercentualIndex(inflacaoAnual));
		out.println("Inflacao Mensal: " + formatPercentualIndex(idxInflacaoMensal));

		out.println("Val. Investido : " + NumberFormat.getCurrencyInstance().format(valorInvestido));

		out.println(margem);

		out.println(footer);
		out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(valorFinal));
		out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(valorReal));
		out.println("Inflacao Incid : " + NumberFormat.getCurrencyInstance().format(valorInflacaoIncidente));
		out.println("Inflacao Acumul: " + formatPercentualIndex(inflacaoAcumulada));
		out.println("Inflacao Mensal: " + formatPercentualIndex(inflacaoAcumuladaMensal));

		out.println("Rend. Final    : " + formatPercentualIndex(indiceGanhoFinal));
		out.println("Rend. Real     : " + formatPercentualIndex(indiceGanhoReal));

		out.println(subfooter);
		out.println("Val. Saque     : " + NumberFormat.getCurrencyInstance().format(valorSaque));
		out.println("Reinvest. Mens.: " + formatPercentualIndex(indiceAnualAposentadoria));

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

	public double[] calcularQtdeMaximaSaques(final double valorInicial, final double valorSaque,
			final double idxRendInicial, final double idxRendAtual, final double idxIR, final double idxInflacao) {
		int saque = 0;
		int totSaques = 0;
		double valFinal;
		double valRestante = -1;

		while (true) {
			valFinal = calcularValorRestante(valorInicial, valorSaque, ++saque, idxRendInicial, idxRendAtual, idxIR,
					idxInflacao);
			if (valFinal <= 0) {
				return new double[] { valRestante, totSaques };
			} else {
				valRestante = valFinal;
				totSaques = saque;
			}
		}
	}

	private double calcularValorRestante(final double montanteInicial, final double valorSaque, final int qtdeSaques,
			final double idxRendInicial, final double idxRendAtual, final double idxIR, final double idxInflacao) {
		return calcularValorRestante(montanteInicial, valorSaque, qtdeSaques, idxRendInicial, idxRendAtual, idxIR,
				idxInflacao, 0, 1);
	}

	private double calcularValorRestante(double montanteInicial, double valorSaque, final int qtdeSaques,
			final double idxRendInicial, final double idxRendAtual, final double idxIR, final double idxInflacao,
			double valorIR, int count) {

		if (count > qtdeSaques) {
			return montanteInicial;
		}

		if (count > 0) {
			valorSaque *= 1 + idxInflacao;
		}
		valorIR = valorSaque * (pow(1 + idxRendInicial, qtdeSaques - count) - 1d);
		montanteInicial = montanteInicial - (valorSaque + valorIR);

		return calcularValorRestante(montanteInicial, valorSaque, qtdeSaques, idxRendInicial, idxRendAtual, idxIR,
				idxInflacao, valorIR, ++count);
	}

	private String formatPercentualIndex(final double index) {
		return (new DecimalFormat("#.###").format(index * 100) + "%").replace(".", ",");
	}
}
