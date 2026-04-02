import java.util.Scanner;

// Projeto 01 - Sistema de Controle de Estacionamento
// Disciplina: Programação de Sistemas I - 2026-01
// Autores: Moabe e Haramaki

public class Main {

    // =========================================================
    // MÉTODOS UTILITÁRIOS - usados em várias partes do sistema
    // =========================================================

    public static void imprime(String texto) {
        System.out.println(texto);
    }

    public static void subMenu() {
        imprime("- Escolha qual veículo: -\n" +
                "1 - Carro Pequeno\n" +
                "2 - Carro Grande\n" +
                "3 - Moto\n" +
                "4 - Voltar ao menu principal");
    }

    public static double lerDouble() {
        Scanner entrada = new Scanner(System.in);
        double valor = entrada.nextDouble();
        entrada.nextLine();
        return valor;
    }

    public static int lerInt() {
        Scanner entrada = new Scanner(System.in);
        int valor = entrada.nextInt();
        entrada.nextLine();
        return valor;
    }

    public static String lerString() {
        Scanner entrada = new Scanner(System.in);
        return entrada.nextLine();
    }

    // =========================================================
    // FUNCIONALIDADE 1 - CADASTRAR TARIFAS
    // =========================================================

    public static void cadastrarTarifas(double vetor[]) {
        imprime("Informe o valor da tarifa das 3 primeiras horas: ");
        vetor[0] = lerDouble();
        imprime("Informe o valor da tarifa de hora adicional: ");
        vetor[1] = lerDouble();
        imprime("Tarifas cadastradas com sucesso!");
    }

    // =========================================================
    // FUNCIONALIDADE 2 - REGISTRAR ENTRADA DE VEÍCULO
    // =========================================================

    public static int buscarVagaLivre(int vetorRegistro[]) {
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public static String lerPlacaValida() {
        String placa;
        boolean placaValida;
        do {
            placaValida = true;
            imprime("Informe a placa com 7 caracteres (ou digite 0 para voltar): ");
            placa = lerString().toUpperCase();

            if (placa.equals("0")) {
                imprime("Operação cancelada. Voltando ao menu...");
                return null;
            }

            if (placa.length() != 7) {
                imprime("Erro: A placa deve conter exatamente 7 caracteres!");
                placaValida = false;
            } else {
                for (int i = 0; i < placa.length(); i++) {
                    if (placa.charAt(i) == '-' || placa.charAt(i) == ' ') {
                        imprime("Erro: A placa não pode conter espaços ou hífens!");
                        placaValida = false;
                        break;
                    }
                }
            }

        } while (!placaValida);
        return placa;
    }

    public static String lerHorarioValido() {
        String horario;
        boolean horarioValido;
        do {
            horarioValido = true;
            imprime("Informe o horário (HH:MM): ");
            horario = lerString();

            if (horario.length() == 5 && horario.charAt(2) == ':') {
                int hora = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
                int minuto = Integer.parseInt("" + horario.charAt(3) + horario.charAt(4));

                if (hora < 0 || hora > 23) {
                    imprime("Erro: Hora deve ser entre 00 e 23!");
                    horarioValido = false;
                } else if (minuto < 0 || minuto > 59) {
                    imprime("Erro: Minuto deve ser entre 00 e 59!");
                    horarioValido = false;
                }
            } else {
                imprime("Erro: Horário deve estar no formato HH:MM (ex: 08:30)!");
                horarioValido = false;
            }

        } while (!horarioValido);
        return horario;
    }

    public static int horarioParaMinutos(String horario) {
        int hora = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
        int minuto = Integer.parseInt("" + horario.charAt(3) + horario.charAt(4));
        return hora * 60 + minuto;
    }

    public static void registrarEntradaVeiculo(int vetorRegistro[], String vetorPlaca[], String vetorHorario[]) {
        int posicao = buscarVagaLivre(vetorRegistro);

        if (posicao == -1) {
            imprime("Estacionamento lotado! Não há vagas disponíveis.");
            return;
        }

        String placa = lerPlacaValida();
        if (placa == null) {
            return;
        }

        vetorRegistro[posicao] = 1;
        vetorPlaca[posicao] = placa;
        vetorHorario[posicao] = lerHorarioValido();
        imprime("Entrada registrada com sucesso na vaga " + (posicao + 1) + "!");
    }

    // =========================================================
    // FUNCIONALIDADE 3 - REGISTRAR SAÍDA DE VEÍCULO
    // =========================================================

    public static int procurarVeiculo(int vetorRegistro[], String vetorPlaca[]) {
        boolean encontrado;
        do {
            String placa = lerPlacaValida();

            if (placa == null) {
                return -2;
            }

            encontrado = false;
            for (int i = 0; i < vetorRegistro.length; i++) {
                if (vetorRegistro[i] == 1 && vetorPlaca[i].equalsIgnoreCase(placa)) {
                    encontrado = true;
                    return i;
                }
            }

            if (!encontrado) {
                imprime("Veículo com placa " + placa + " não encontrado. Tente novamente ou digite 0 para voltar.");
            }

        } while (!encontrado);
        return -2;
    }

    public static double calcularValor(int minutosTotal, double tarifas[]) {
        int horasTotal = (int) Math.ceil(minutosTotal / 60.0);

        if (horasTotal <= 3) {
            return tarifas[0];
        } else {
            int horasExtras = horasTotal - 3;
            return tarifas[0] + horasExtras * tarifas[1];
        }
    }

    // --- SUBFUNÇÕES de registrarSaidaVeiculo ---

    // Lê o horário de saída garantindo que não seja anterior ao de entrada
    public static String lerHorarioSaidaValido(String horarioEntrada) {
        String horarioSaida;
        int minutosEntrada = horarioParaMinutos(horarioEntrada);
        do {
            horarioSaida = lerHorarioValido();
            if (horarioParaMinutos(horarioSaida) < minutosEntrada) {
                imprime("Erro: Horário de saída não pode ser anterior ao de entrada (" + horarioEntrada + ")!");
            }
        } while (horarioParaMinutos(horarioSaida) < minutosEntrada);
        return horarioSaida;
    }

    // Retorna int[] com { horas, minutos, totalMinutos } de permanência
    public static int[] calcularPermanencia(String horarioEntrada, String horarioSaida) {
        int minutosEntrada = horarioParaMinutos(horarioEntrada);
        int minutosSaida = horarioParaMinutos(horarioSaida);
        int total = minutosSaida - minutosEntrada;
        return new int[]{ total / 60, total % 60, total };
    }

    // Pergunta a forma de pagamento e retorna double[] com { valorFinal, desconto }
    public static double[] aplicarFormaPagamento(double valorOriginal) {
        imprime("Forma de pagamento:\n1 - PIX (5% de desconto)\n2 - Outros");
        int pagamento = lerInt();

        double desconto = 0;
        double valorFinal = valorOriginal;

        if (pagamento == 1) {
            desconto = valorOriginal * 0.05;
            valorFinal = valorOriginal - desconto;
        }
        return new double[]{ valorFinal, desconto };
    }

    // Exibe o resumo formatado da saída no console
    public static void exibirResumoSaida(String placa, String nomeVeiculo,
            String horarioEntrada, String horarioSaida,
            int[] permanencia, double valorOriginal, double[] pagamento) {

        imprime("\n===== RESUMO DA SAÍDA =====");
        imprime("Placa: " + placa);
        imprime("Tipo de veículo: " + nomeVeiculo);
        imprime("Horário de entrada: " + horarioEntrada);
        imprime("Horário de saída: " + horarioSaida);
        imprime("Tempo de permanência: " + permanencia[0] + "h " + permanencia[1] + "min");
        imprime(String.format("Valor original: R$ %.2f", valorOriginal));
        if (pagamento[1] > 0) {
            imprime(String.format("Desconto (PIX 5%%): R$ %.2f", pagamento[1]));
        }
        imprime(String.format("Valor final pago: R$ %.2f", pagamento[0]));
        imprime("===========================\n");
    }

    // Salva os dados do atendimento encerrado no histórico e libera a vaga
    public static void salvarHistoricoELiberarVaga(
            int posicao, String placa, String horarioSaida, double valorFinal,
            int vetorRegistro[], String vetorPlaca[], String vetorHorarioEntrada[],
            String vetorPlacaHistorico[], String vetorEntradaHistorico[],
            String vetorSaidaHistorico[], double vetorValorHistorico[],
            int contadorHistorico[]) {

        int idx = contadorHistorico[0];
        vetorPlacaHistorico[idx] = placa;
        vetorEntradaHistorico[idx] = vetorHorarioEntrada[posicao];
        vetorSaidaHistorico[idx] = horarioSaida;
        vetorValorHistorico[idx] = valorFinal;
        contadorHistorico[0]++;

        // Libera a vaga somente após persistir os dados no histórico
        vetorRegistro[posicao] = 0;
        vetorPlaca[posicao] = null;
        vetorHorarioEntrada[posicao] = null;
    }

    // Orquestra as subfunções de saída, mantendo responsabilidade de fluxo
    public static void registrarSaidaVeiculo(
            int vetorRegistro[], String vetorPlaca[], String vetorHorarioEntrada[],
            String vetorPlacaHistorico[], String vetorEntradaHistorico[],
            String vetorSaidaHistorico[], double vetorValorHistorico[],
            int contadorHistorico[], double tarifas[], String nomeVeiculo) {

        int posicao = procurarVeiculo(vetorRegistro, vetorPlaca);
        if (posicao == -2) return;

        // Salva a placa antes de qualquer alteração nos vetores
        String placa = vetorPlaca[posicao];
        String horarioEntrada = vetorHorarioEntrada[posicao];

        String horarioSaida = lerHorarioSaidaValido(horarioEntrada);
        int[] permanencia = calcularPermanencia(horarioEntrada, horarioSaida);
        double valorOriginal = calcularValor(permanencia[2], tarifas);
        double[] pagamento = aplicarFormaPagamento(valorOriginal);

        exibirResumoSaida(placa, nomeVeiculo, horarioEntrada, horarioSaida,
                permanencia, valorOriginal, pagamento);

        salvarHistoricoELiberarVaga(
                posicao, placa, horarioSaida, pagamento[0],
                vetorRegistro, vetorPlaca, vetorHorarioEntrada,
                vetorPlacaHistorico, vetorEntradaHistorico,
                vetorSaidaHistorico, vetorValorHistorico, contadorHistorico);
    }

    // =========================================================
    // FUNCIONALIDADE 4 - GERAR RELATÓRIO DIÁRIO
    // =========================================================

    public static int contarVeiculosPresentes(int vetorRegistro[]) {
        int contador = 0;
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 1) contador++;
        }
        return contador;
    }

    // --- SUBFUNÇÕES de gerarRelatorioDiario ---

    // Retorna o total de entradas do dia: veículos presentes + veículos que já saíram
    public static int calcularTotalEntradas(
            int carroPequeno[], int contHistCp[],
            int carroGrande[], int contHistCg[],
            int moto[], int contHistMoto[]) {

        return contarVeiculosPresentes(carroPequeno) + contHistCp[0]
             + contarVeiculosPresentes(carroGrande) + contHistCg[0]
             + contarVeiculosPresentes(moto) + contHistMoto[0];
    }

    // Retorna int[] com { mediaHoras, mediaMinutos } de permanência entre todos os atendimentos
    public static int[] calcularTempoMedioPermanencia(
            String entradaHistCp[], String saidaHistCp[], int contHistCp[],
            String entradaHistCg[], String saidaHistCg[], int contHistCg[],
            String entradaHistMoto[], String saidaHistMoto[], int contHistMoto[]) {

        int totalSaidas = contHistCp[0] + contHistCg[0] + contHistMoto[0];
        int somaMinutos = 0;

        for (int i = 0; i < contHistCp[0]; i++)
            somaMinutos += horarioParaMinutos(saidaHistCp[i]) - horarioParaMinutos(entradaHistCp[i]);
        for (int i = 0; i < contHistCg[0]; i++)
            somaMinutos += horarioParaMinutos(saidaHistCg[i]) - horarioParaMinutos(entradaHistCg[i]);
        for (int i = 0; i < contHistMoto[0]; i++)
            somaMinutos += horarioParaMinutos(saidaHistMoto[i]) - horarioParaMinutos(entradaHistMoto[i]);

        int mediaMinutos = (totalSaidas > 0) ? somaMinutos / totalSaidas : 0;
        return new int[]{ mediaMinutos / 60, mediaMinutos % 60 };
    }

    // Soma o valor total arrecadado no dia considerando os três tipos de veículo
    public static double calcularTotalArrecadado(
            double valorHistCp[], int contHistCp[],
            double valorHistCg[], int contHistCg[],
            double valorHistMoto[], int contHistMoto[]) {

        double total = 0;
        for (int i = 0; i < contHistCp[0]; i++) total += valorHistCp[i];
        for (int i = 0; i < contHistCg[0]; i++) total += valorHistCg[i];
        for (int i = 0; i < contHistMoto[0]; i++) total += valorHistMoto[i];
        return total;
    }

    // Exibe o relatório diário com os dados já calculados pelas subfunções
    public static void exibirRelatorioDiario(int totalEntradas, int totalSaidas,
            int[] tempoMedio, double totalArrecadado) {

        imprime("\n===== RELATÓRIO DIÁRIO =====");
        imprime("Total de veículos que entraram: " + totalEntradas);
        imprime("Total de veículos que saíram: " + totalSaidas);
        imprime("Tempo médio de permanência: " + tempoMedio[0] + "h " + tempoMedio[1] + "min");
        imprime(String.format("Valor total arrecadado: R$ %.2f", totalArrecadado));
        imprime("============================\n");
    }

    // Orquestra as subfunções do relatório diário
    public static void gerarRelatorioDiario(
            int carroPequeno[], int carroGrande[], int moto[],
            String placasHistCp[], String entradaHistCp[], String saidaHistCp[], double valorHistCp[], int contHistCp[],
            String placasHistCg[], String entradaHistCg[], String saidaHistCg[], double valorHistCg[], int contHistCg[],
            String placasHistMoto[], String entradaHistMoto[], String saidaHistMoto[], double valorHistMoto[], int contHistMoto[]) {

        int totalEntradas = calcularTotalEntradas(
                carroPequeno, contHistCp, carroGrande, contHistCg, moto, contHistMoto);

        int totalSaidas = contHistCp[0] + contHistCg[0] + contHistMoto[0];

        int[] tempoMedio = calcularTempoMedioPermanencia(
                entradaHistCp, saidaHistCp, contHistCp,
                entradaHistCg, saidaHistCg, contHistCg,
                entradaHistMoto, saidaHistMoto, contHistMoto);

        double totalArrecadado = calcularTotalArrecadado(
                valorHistCp, contHistCp, valorHistCg, contHistCg, valorHistMoto, contHistMoto);

        exibirRelatorioDiario(totalEntradas, totalSaidas, tempoMedio, totalArrecadado);
    }

    // =========================================================
    // FUNCIONALIDADE 5 - RELATÓRIO POR TIPO DE VEÍCULO
    // =========================================================

    public static double calcularMediaValor(double vetorValores[], int quantidade) {
        if (quantidade == 0) return 0;
        double soma = 0;
        for (int i = 0; i < quantidade; i++) soma += vetorValores[i];
        return soma / quantidade;
    }

    // --- SUBFUNÇÕES de gerarRelatorioTipoVeiculo ---

    // Retorna o nome do tipo de veículo com maior número de atendimentos
    public static String determinarTipoMaisFrequente(int qtdCp, int qtdCg, int qtdMoto) {
        if (qtdCp >= qtdCg && qtdCp >= qtdMoto) return "Carro Pequeno";
        if (qtdCg >= qtdCp && qtdCg >= qtdMoto) return "Carro Grande";
        return "Moto";
    }

    // Exibe o relatório por tipo com os dados já calculados pelas subfunções
    public static void exibirRelatorioTipoVeiculo(
            int qtdCp, double mediaCp,
            int qtdCg, double mediaCg,
            int qtdMoto, double mediaMoto,
            String maisFrequente) {

        imprime("\n===== RELATÓRIO POR TIPO DE VEÍCULO =====");
        imprime("Carros Pequenos atendidos: " + qtdCp);
        imprime(String.format("  Média de valor pago: R$ %.2f", mediaCp));
        imprime("Carros Grandes atendidos: " + qtdCg);
        imprime(String.format("  Média de valor pago: R$ %.2f", mediaCg));
        imprime("Motos atendidas: " + qtdMoto);
        imprime(String.format("  Média de valor pago: R$ %.2f", mediaMoto));
        imprime("Tipo mais frequente: " + maisFrequente);
        imprime("==========================================\n");
    }

    // Orquestra as subfunções do relatório por tipo de veículo
    public static void gerarRelatorioTipoVeiculo(
            int carroPequeno[], int carroGrande[], int moto[],
            double valorHistCp[], int contHistCp[],
            double valorHistCg[], int contHistCg[],
            double valorHistMoto[], int contHistMoto[]) {

        int qtdCp = contHistCp[0];
        int qtdCg = contHistCg[0];
        int qtdMoto = contHistMoto[0];

        double mediaCp = calcularMediaValor(valorHistCp, qtdCp);
        double mediaCg = calcularMediaValor(valorHistCg, qtdCg);
        double mediaMoto = calcularMediaValor(valorHistMoto, qtdMoto);

        String maisFrequente = determinarTipoMaisFrequente(qtdCp, qtdCg, qtdMoto);

        exibirRelatorioTipoVeiculo(qtdCp, mediaCp, qtdCg, mediaCg, qtdMoto, mediaMoto, maisFrequente);
    }

    // =========================================================
    // MÉTODO PRINCIPAL
    // =========================================================
    public static void main(String[] args) {

        int[] carroPequeno = new int[100];
        String[] placasCarroPequeno = new String[100];
        String[] horariosEntradaCarroPequeno = new String[100];
        double[] valorTarifasCarroPequeno = new double[2];

        int[] carroGrande = new int[100];
        String[] placasCarroGrande = new String[100];
        String[] horariosEntradaCarroGrande = new String[100];
        double[] valorTarifasCarroGrande = new double[2];

        int[] moto = new int[100];
        String[] placasMoto = new String[100];
        String[] horariosEntradaMoto = new String[100];
        double[] valorTarifasMoto = new double[2];

        String[] placasHistCp = new String[1000];
        String[] entradaHistCp = new String[1000];
        String[] saidaHistCp = new String[1000];
        double[] valorHistCp = new double[1000];
        int[] contHistCp = new int[1];

        String[] placasHistCg = new String[1000];
        String[] entradaHistCg = new String[1000];
        String[] saidaHistCg = new String[1000];
        double[] valorHistCg = new double[1000];
        int[] contHistCg = new int[1];

        String[] placasHistMoto = new String[1000];
        String[] entradaHistMoto = new String[1000];
        String[] saidaHistMoto = new String[1000];
        double[] valorHistMoto = new double[1000];
        int[] contHistMoto = new int[1];

        Scanner entrada = new Scanner(System.in);
        int opcao;

        do {
            imprime("\n=== SISTEMA DE CONTROLE DE ESTACIONAMENTO ===\n" +
                    "1. Cadastrar Tarifas\n" +
                    "2. Registrar Entrada de Veículo\n" +
                    "3. Registrar Saída de Veículo\n" +
                    "4. Gerar Relatório Diário\n" +
                    "5. Gerar Relatório por Tipo de Veículo\n" +
                    "6. Sair\n" +
                    "Selecione uma opção:");
            opcao = entrada.nextInt();
            entrada.nextLine();

            int escolha;
            switch (opcao) {

                case 1:
                    do {
                        subMenu();
                        escolha = lerInt();
                        if (escolha == 1) {
                            cadastrarTarifas(valorTarifasCarroPequeno);
                        } else if (escolha == 2) {
                            cadastrarTarifas(valorTarifasCarroGrande);
                        } else if (escolha == 3) {
                            cadastrarTarifas(valorTarifasMoto);
                        } else if (escolha == 4) {
                            break;
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                case 2:
                    do {
                        subMenu();
                        escolha = lerInt();
                        if (escolha == 1) {
                            registrarEntradaVeiculo(carroPequeno, placasCarroPequeno, horariosEntradaCarroPequeno);
                        } else if (escolha == 2) {
                            registrarEntradaVeiculo(carroGrande, placasCarroGrande, horariosEntradaCarroGrande);
                        } else if (escolha == 3) {
                            registrarEntradaVeiculo(moto, placasMoto, horariosEntradaMoto);
                        } else if (escolha == 4) {
                            break;
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                case 3:
                    do {
                        subMenu();
                        escolha = lerInt();
                        if (escolha == 1) {
                            registrarSaidaVeiculo(
                                    carroPequeno, placasCarroPequeno, horariosEntradaCarroPequeno,
                                    placasHistCp, entradaHistCp, saidaHistCp, valorHistCp,
                                    contHistCp, valorTarifasCarroPequeno, "Carro Pequeno");
                        } else if (escolha == 2) {
                            registrarSaidaVeiculo(
                                    carroGrande, placasCarroGrande, horariosEntradaCarroGrande,
                                    placasHistCg, entradaHistCg, saidaHistCg, valorHistCg,
                                    contHistCg, valorTarifasCarroGrande, "Carro Grande");
                        } else if (escolha == 3) {
                            registrarSaidaVeiculo(
                                    moto, placasMoto, horariosEntradaMoto,
                                    placasHistMoto, entradaHistMoto, saidaHistMoto, valorHistMoto,
                                    contHistMoto, valorTarifasMoto, "Moto");
                        } else if (escolha == 4) {
                            break;
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                case 4:
                    gerarRelatorioDiario(
                            carroPequeno, carroGrande, moto,
                            placasHistCp, entradaHistCp, saidaHistCp, valorHistCp, contHistCp,
                            placasHistCg, entradaHistCg, saidaHistCg, valorHistCg, contHistCg,
                            placasHistMoto, entradaHistMoto, saidaHistMoto, valorHistMoto, contHistMoto);
                    break;

                case 5:
                    gerarRelatorioTipoVeiculo(
                            carroPequeno, carroGrande, moto,
                            valorHistCp, contHistCp,
                            valorHistCg, contHistCg,
                            valorHistMoto, contHistMoto);
                    break;

                case 6:
                    imprime("Encerrando o sistema. Até logo!");
                    break;

                default:
                    imprime("\nOPÇÃO INVÁLIDA! Selecione uma opção entre 1 e 6.\n");
            }

        } while (opcao != 6);

        entrada.close();
    }
}
