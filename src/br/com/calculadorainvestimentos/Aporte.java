package br.com.calculadorainvestimentos;

public class Aporte {
	private double valorInicial;
	private double valor;
	private double valorRestante;
	private int posicao;

	public double getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(double valorInicial) {
		this.valorInicial = valorInicial;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	

	public double getValorRestante() {
		return valorRestante;
	}

	public void setValorRestante(double valorRestante) {
		this.valorRestante = valorRestante;
	}

	@Override
	public String toString() {
		return "Aporte [valorInicial=" + valorInicial + ", valor=" + valor + ", valorRestante=" + valorRestante
				+ ", posicao=" + posicao + "]";
	}

	 

}
