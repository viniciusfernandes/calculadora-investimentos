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

	public CalculadoraInvestimento(final double aliquotaAplicacao, final double aliquotaReaplicacao,
			final double aliquotaIR, final double aliquotaInflacao, final int qtdeAportes, final int qtdeSaques,
			final double valorAporte, final double valorSaque) {
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

		final double valorInvestido = calcularValorInvestido();

		final String margem = "---------------------------------------";
		final String header = margem + "\nValores Iniciais\n" + margem;
		final String footer = "\n\nInvestimentos\n" + margem;
		final String subfooter = "\n\nSaques\n" + margem;

		final double valorRestante = calcularValorRestante();
		final int qtdeMaxSaques = calcularQtdeMaxSaques(idxApliMes, idxInflMes);

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
		out.println("Último Saque   : " + NumberFormat.getCurrencyInstance().format(valUltimoSaque));
		out.println("Reinvest. Mens.: " + formatPercentualIndex(indiceReaplicacao));

		out.println("Anos Saque     : " + qtdeSaques / 12);
		out.println("Anos. Max.     : " + 33333 / 12);
		out.println("Qtde. Saq.     : " + qtdeSaques);

		out.println("Qtde. Max. Saq.: " + qtdeMaxSaques);
		out.println("Tempo Saq      : " + qtdeMaxSaques / 12 + " anos e " + qtdeMaxSaques % 12 + " meses");

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

	private void efetuarReinvestimento() {
		Aporte aporte = null;
		for (int i = 0; i < aportes.size(); i++) {
			aporte = aportes.get(i);
			aporte.setValorRestante(aporte.getValorRestante() * (1 + indiceReaplicacao));
		}
	}

	private double calcularValorRestante() {
		return calcularValorRestante(qtdeSaques);
	}

	private double calcularValorRestante(int qtdeSaques) {
		for (int idxSaque = 0; idxSaque < qtdeSaques; idxSaque++) {
			efetuarSaque(qtdeSaques, idxSaque);
			efetuarReinvestimento();
		}
		double valRestate = 0;
		for (Aporte aporte : aportes) {
			valRestate += aporte.getValorRestante();
		}
		return valRestate;
	}

	private double calcularValorInvestido() {
		double val = 0;
		for (Aporte aporte : aportes) {
			val += aporte.getValorInicial();
		}
		return val;
	}

	private int calcularQtdeMaxSaques(double indiceAplicacaoMes, double indiceInflacaoMes) {
		int qtdeMaxSaques = 1;
		double valRestante = -1;
		while (true) {
			inicializarAportes(indiceAplicacaoMes);
			inicializarSaques(qtdeMaxSaques, indiceInflacaoMes);

			valRestante = calcularValorRestante(qtdeMaxSaques);
			if (valRestante <= 0) {
				return qtdeMaxSaques;
			}
			incrementarSaques(1, indiceInflacaoMes);
			qtdeMaxSaques++;
		}
	}

	private void efetuarSaque(int qtdeSaques, int idxSaque) {
		double valSaque = 0;
		double valAporte = 0;
		int idxAporte = 0;
		int idxUltimoSaque = qtdeSaques - 1;
		int idxUltimoAporte = qtdeAportes - 1;
		Aporte aporte = null;

		if (idxSaque > idxUltimoSaque) {
			return;
		}
		valSaque = saques.get(idxSaque);
		while (true) {
			if (idxAporte > idxUltimoAporte) {
				break;
			}
			aporte = aportes.get(idxAporte);
			valAporte += aporte.getValorRestante();
			if (valSaque > valAporte) {
				aporte.setValorRestante(0d);
				idxAporte++;
			} else {
				aporte.setValorRestante(valAporte - valSaque);
				return;
			}

		}
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

	private void incrementarSaques(int qtdeSaques, final double idxInflacaoMes) {
		int qtdeSaquesAnterior = saques.size();
		qtdeSaques = qtdeSaques + qtdeSaquesAnterior - 1;
		for (int i = qtdeSaquesAnterior; i <= qtdeSaques; i++) {
			saques.add(valorSaque * pow(1 + idxInflacaoMes, i));
		}
	}

	private void inicializarSaques(final double idxInflacaoMes) {
		inicializarSaques(qtdeSaques, idxInflacaoMes);
	}

	private void inicializarSaques(int qtdeSaques, final double idxInflacaoMes) {
		saques.clear();
		final int idxPrimeiroSaque = qtdeAportes;
		final int idxUltimoSaque = idxPrimeiroSaque + qtdeSaques - 1;
		for (int i = idxPrimeiroSaque; i <= idxUltimoSaque; i++) {
			saques.add(valorSaque * pow(1 + idxInflacaoMes, i));
		}
	}

}
