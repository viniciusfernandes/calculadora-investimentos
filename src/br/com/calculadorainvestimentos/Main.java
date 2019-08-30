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
	private static final String ESPACO = "\n+\n+\n+\n+\n+\n+\n+\n+\n+\n+\n";
	private static final DecimalFormat DF = new DecimalFormat("#.###");
	private static final String pathDir = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private static final File dir = new File(pathDir).getParentFile();
	private static final File dadosInvestimento = new File(dir.getAbsolutePath() + File.separator + "investimento.txt");
	private static boolean hasFile = false;
	private static final Scanner scanner = new Scanner(System.in);
	private static boolean isPrimeiraExecucao = true;
	private static Properties props = null;

	public static void main(final String[] args) {
		gerarArquivoInvestimento();

		System.out.println(MARGEM + MARGEM);
		System.out.println("Preencha o arquivo: investimento.txt");
		System.out.println(MARGEM + MARGEM);

		String comando = "";
		boolean leituraOk = true;
		while (true) {
			if (leituraOk) {
				printInstrucaoInicial();
			}

			comando = scanner.nextLine();
			if (!"s".equalsIgnoreCase(comando)) {
				scanner.close();
				System.out.println("\n" + MARGEM);
				System.out.println("FIM");
				System.out.println(MARGEM);
				return;
			}
			try {
				gerarFluxoInvestimento();
				leituraOk = true;
			} catch (Exception e) {
				System.out.println(ESPACO);
				System.out.println(
						"HOUVE UMA FALHA NA LEITURA DO ARQUIVO DE INVESTIMENTO.");
				leituraOk = false;
				dadosInvestimento.delete();
				gerarArquivoInvestimento();

				System.out.println("O arquivo foi gerado no mesmo diretorio. Preencha os valores novamente.");
				printInstrucaoInicial();
			}

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
			if (hasFile = arq.getName().equals("investimento.txt")) {
				break;
			}
		}
		if (!hasFile) {
			try {
				StringBuilder descricao = new StringBuilder();
				descricao.append(
						"# Eh a aliquota anual da aplicacao que se espera fazer no inicio dos investimentos.\r\n");
				descricao.append("aliquotaAplicacao:10.0\r\n\n");

				descricao.append(
						"# Eh a aliquota anual da aplicacao que se espera fazer ao inicio dos saques, por exemplo,\r\n# ao termino de 20 anos de aplicacao com aliquotaAplicacao=10.0%, pretende-se direcionar \r\n# toda a quantia acumulada para um investimento mais conversador que renda 5%.\r\n");
				descricao.append("aliquotaReaplicacao:6.0\r\n\n");

				descricao.append("# Eh a aliquota media de Imposto de Renda sobre os saques efetuados.\r\n");
				descricao.append("aliquotaIR:12.0\r\n\n");

				descricao.append("# Eh a aliquota media da inflacao anual.\r\n");
				descricao.append("aliquotaInflacao:4.5\r\n\n");

				descricao.append(
						"# Eh o numero de aportes que se deseja efetuar ate o inicio dos saques (inicio da aposentadoria).\r\n");
				descricao.append("qtdeAportes:240\r\n\n");

				descricao.append("# Eh o valor do montante que se possui no momento do primeiro aporte.\r\n");
				descricao.append("valorInicial:100000\r\n\n");

				descricao.append("# Eh o valor do aporte mensal que se deseja fazer.\r\n");
				descricao.append("valorAporte:3000.0\r\n\n");

				descricao.append("# Eh o valor do saque mensal que se deseja fazer durante toda a aposentadoria.\r\n");
				descricao.append("valorSaque:4000.0\r\n");
				BufferedWriter writer = new BufferedWriter(new FileWriter(dadosInvestimento));
				writer.write(descricao.toString());
				writer.close();
			} catch (IOException e) {
				System.out.println(MARGEM);
				System.out.println(
						"Falha na leitura/geracao do arquivo de investimentos. Delete o arquivo do diretorio para que seja gerado automaticamente.");
			}
		}
	}

	private static Investimento gerarInvetimento() throws Exception {
		props = new Properties();
		FileInputStream file;
		file = new FileInputStream(dadosInvestimento);
		props.load(file);
		file.close();

		final Investimento investimento = new Investimento();
		investimento.setAliquotaAplicacao(parse("aliquotaAplicacao"));
		investimento.setAliquotaInflacao(parse("aliquotaReaplicacao"));
		investimento.setAliquotaIR(parse("aliquotaIR"));
		investimento.setAliquotaReaplicacao(parse("aliquotaInflacao"));
		investimento.setQtdeAportes(parse("qtdeAportes").intValue());
		investimento.setValorAporte(parse("valorAporte"));
		investimento.setValorInicial(parse("valorInicial"));
		investimento.setValorSaque(parse("valorSaque"));

		return investimento;

	}

	private static void gerarFluxoInvestimento() throws Exception {
		final Investimento investimento = gerarInvetimento();
		final FluxoInvestimento fluxo = new CalculadoraInvestimento().calcular(investimento);

		if (!isPrimeiraExecucao) {
			System.out.println(ESPACO);
		}

		isPrimeiraExecucao = false;

		print(investimento);
		print(fluxo.getProjecaoInvestimento());
		print(fluxo.getProjecaoSaque());

	}

	private static Double parse(String property) {
		return Double.parseDouble(props.getProperty(property));
	}

	public static void printInstrucaoInicial() {
		System.out.print("\nDigite \"s\" para gerar o fluxo de investimentos ou qualquer letra para sair: ");
	}
}
