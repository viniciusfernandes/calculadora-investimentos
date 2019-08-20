package br.com.calculadorainvestimentos;

public class Main {
    public static void main(final String[] args) {
        new CalculadoraInvestimento(10, 6, 10, 4.5, 12 * 20, 12 * 25, 5000, 1000).calcular();
    }

}
