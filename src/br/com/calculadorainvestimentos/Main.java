package br.com.calculadorainvestimentos;

import static java.lang.System.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Scanner;

public class Main {
	private static final String MARGEM = "---------------------------------------";
	private static final DecimalFormat DF = new DecimalFormat("#.###");
	private static final String pathDir = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private static final File dir = new File(pathDir).getParentFile();
	private static final File dadosInvestimento = new File(
			dir.getAbsolutePath() + File.separator + "investimento.properties");
	private static boolean hasFile = false;
	private static final Scanner scanner = new Scanner(System.in);
	private static boolean isPrimeiraExecucao = true;

	public static void main(final String[] args) {
		gerarArquivoInvestimento();

		System.out.println(MARGEM + MARGEM);
		System.out.println("Preencha o arquivo: investimento.properties");
		System.out.println(MARGEM + MARGEM);

		String comando = "";
		while (true) {

			System.out.print("\nDigite \"s\" para gerar o fluxo de investimentos ou qualquer letra para sair: ");
			comando = scanner.nextLine();
			if (!"s".equalsIgnoreCase(comando)) {
				scanner.close();
				System.out.println("\n" + MARGEM);
				System.out.println("FIM");
				System.out.println(MARGEM);
				return;
			}
			gerarFluxoInvestimento();

		}
	}

	private static void print(final ProjecaoInvestimento projecaoInvest) {
		out.println("\n\nInvestimentos\n" + MARGEM);
		out.println("Tempo Invest.  : " + formatarTempo(projecaoInvest.getQtdeAportes()));

		out.println(
				"Val. Investido : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorInvestido()));
		out.println("Val. Depreci.  : "
				+ NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorInvestidoComInflacao()));
		out.println("Val. Final     : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorFinal()));
		out.println("Val. Real      : " + NumberFormat.getCurrencyInstance().format(projecaoInvest.getValorReal()));

		out.println("Rend. Mensal   : " + formatarAliquota(projecaoInvest.getAliquotaAplicacaoMes()));
		out.println("Rend. Real     : " + formatarAliquota(projecaoInvest.getAliquotaReal()));

		out.println("Infl. Mes      : " + formatarAliquota(projecaoInvest.getAliquotaInflacaoMes()));
		out.println("Infl. Acum.    : " + formatarAliquota(projecaoInvest.getAliquotaInflacaoAcumulada()));
		out.println("Infl. Acum. Mes: " + formatarAliquota(projecaoInvest.getAliquotaInflacaoAcumuladaMes()));

		out.println("Rend. Final    : " + formatarAliquota(projecaoInvest.getAliquotaGanhoFinal()));
		out.println("Rend. Real     : " + formatarAliquota(projecaoInvest.getAliquotaGanhoReal()));
	}

	private static void print(final ProjecaoSaque projSaque) {

		out.println("\n\nSaques\n" + MARGEM);
		out.println("Primeiro Saque : " + NumberFormat.getCurrencyInstance().format(projSaque.getValorPrimeiroSaque()));
		out.println("Último Saque   : " + NumberFormat.getCurrencyInstance().format(projSaque.getValorUltimoSaque()));
		out.println("Reaplic. Mes   : " + formatarAliquota(projSaque.getAliquotaReaplicacaoMes()));
		out.println("Val. Restante  : " + NumberFormat.getCurrencyInstance().format(projSaque.getValorRestante()));

		out.println("Qtde. Max. Saq.: "
				+ (projSaque.getQtdeMaxSaques() >= CalculadoraInvestimento.QTDE_MAX_SAQUES ? "SEM LIMITES"
						: projSaque.getQtdeMaxSaques()));
		out.println("Tempo Max. Saq.: "
				+ (projSaque.getQtdeMaxSaques() >= CalculadoraInvestimento.QTDE_MAX_SAQUES ? "SEM LIMITES"
						: formatarTempo(projSaque.getQtdeMaxSaques())));
		out.println(MARGEM);
	}

	private static void print(final Investimento invest) {
		out.println(MARGEM + "\nValores Iniciais\n" + MARGEM);
		out.println("Aporte Mensal  : " + NumberFormat.getCurrencyInstance().format(invest.getValorAporte()));
		out.println("Saque Mensal   : " + NumberFormat.getCurrencyInstance().format(invest.getValorSaque()));
		out.println("Qtde Aportes   : " + invest.getQtdeAportes());
		out.println("Rend. Anual    : " + formatarAliquota(invest.getAliquotaAplicacao()));
		out.println("Reinvestimento : " + formatarAliquota(invest.getAliquotaReaplicacao()));
		out.println(MARGEM);
	}

	private static String formatarAliquota(final double aliquota) {
		return (DF.format(aliquota) + "%").replace(".", ",");
	}

	private static String formatarTempo(final int qtdeMeses) {
		return qtdeMeses / 12 + " anos e " + qtdeMeses % 12 + " meses";
	}

	private static void gerarArquivoInvestimento() {
		for (File arq : dir.listFiles()) {
			if (hasFile = arq.getName().equals("investimento.properties")) {
				break;
			}
		}
		if (!hasFile) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(dadosInvestimento));
				writer.write("aliquotaAplicacao:10.0\r\n" + "aliquotaReaplicacao:6.0\r\n" + "aliquotaIR:12.0\r\n"
						+ "aliquotaInflacao:4.5\r\n" + "qtdeAportes:240\r\n" + "valorAporte:5000.0\r\n"
						+ "valorSaque:4000.0");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Investimento gerarInvetimento() {
		Properties props = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream(dadosInvestimento);
			props.load(file);
			file.close();

			final Investimento investimento = new Investimento();
			investimento.setAliquotaAplicacao(Double.parseDouble(props.getProperty("aliquotaAplicacao")));
			investimento.setAliquotaInflacao(Double.parseDouble(props.getProperty("aliquotaReaplicacao")));
			investimento.setAliquotaIR(Double.parseDouble(props.getProperty("aliquotaIR")));
			investimento.setAliquotaReaplicacao(Double.parseDouble(props.getProperty("aliquotaInflacao")));
			investimento.setQtdeAportes(Integer.parseInt(props.getProperty("qtdeAportes")));
			investimento.setValorAporte(Double.parseDouble(props.getProperty("valorAporte")));
			investimento.setValorSaque(Double.parseDouble(props.getProperty("valorSaque")));

			return investimento;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void gerarFluxoInvestimento() {
		final Investimento investimento = gerarInvetimento();
		final FluxoInvestimento fluxo = new CalculadoraInvestimento().calcular(investimento);

		if (!isPrimeiraExecucao) {
			for (int i = 0; i < 10; i++) {
				System.out.println("+");
			}
		}

		isPrimeiraExecucao = false;

		print(investimento);
		print(fluxo.getProjecaoInvestimento());
		print(fluxo.getProjecaoSaque());

	}

}
