import java.util.Scanner;

// Projeto 01 - Sistema de Controle de Estacionamento
// Disciplina: Programação de Sistemas I - 2026-01
// Autores: Moabe RA:10748053 e Haramaki RA:10752924

public class Main {
    // MÉTODOS UTILITÁRIOS - usados em várias partes do sistema
   

    // Imprime uma linha de texto no console
    public static void imprime(String texto) {
        System.out.println(texto);
    }

    // Exibe o submenu de escolha de tipo de veículo
    public static void subMenu() {
        imprime("- Escolha qual veículo: -\n" +
                "1 - Carro Pequeno\n" +
                "2 - Carro Grande\n" +
                "3 - Moto\n" +
                "4 - Voltar ao menu principal");
    }

    // Lê um número decimal (double) do teclado e limpa o buffer
    public static double lerDouble(Scanner entrada) {
        double valor = entrada.nextDouble();
        entrada.nextLine(); // limpa o buffer para não afetar a próxima leitura
        return valor;
    }

    // Lê um número inteiro do teclado e limpa o buffer
    public static int lerInt(Scanner entrada) {
        int valor = entrada.nextInt();
        entrada.nextLine(); // limpa o buffer
        return valor;
    }

    // Lê uma linha de texto do teclado
    public static String lerString(Scanner entrada) {
        return entrada.nextLine();
    }


    // FUNCIONALIDADE 1 - CADASTRAR TARIFAS

    // Cadastra as tarifas para um tipo de veículo
    // vetor[0] = valor fixo das 3 primeiras horas
    // vetor[1] = valor de cada hora adicional após as 3 primeiras
    public static void cadastrarTarifas(double vetor[], Scanner entrada) {
        do{
            imprime("Informe o valor da tarifa das 3 primeiras horas: ");
            vetor[0] = lerDouble(entrada);
            imprime("Informe o valor da tarifa de hora adicional: ");
            vetor[1] = lerDouble(entrada);
            if(vetor[0] <= 0 || vetor[1] <= 0){
                imprime("Tarifas inválidas!");
            }else{
                imprime("Tarifas cadastradas com sucesso!");
            }
        }while(vetor[0] <= 0 || vetor[1] <= 0);
        
    }

    // =========================================================
    // FUNCIONALIDADE 2 - REGISTRAR ENTRADA DE VEÍCULO
    // =========================================================

    // Percorre o vetor de controle e retorna o índice da primeira vaga livre (valor 0)
    // Retorna -1 se todas as vagas estiverem ocupadas
    public static int buscarVagaLivre(int vetorRegistro[]) {
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // Solicita e valida a placa do veículo.
    // O usuário pode digitar "0" a qualquer momento para cancelar e voltar ao menu.
    // Retorna a placa válida (em maiúsculo), ou null se o usuário cancelar.
    // Regras de validação:
    //   - deve ter exatamente 7 caracteres
    //   - não pode conter espaços ou hífens
    public static String lerPlacaValida(Scanner entrada) {
        String placa;
        boolean placaValida;
        do {
            placaValida = true; // assume válida e verifica abaixo
            imprime("Informe a placa com 7 caracteres (ou digite 0 para voltar): ");
            placa = lerString(entrada).toUpperCase(); // padroniza em maiúsculo para comparações consistentes

            // Verifica se o usuário quer cancelar a operação
            if (placa.equals("0")) {
                imprime("Operação cancelada. Voltando ao menu...");
                return null; // null é o sinal de cancelamento para quem chamou este método
            }

            if (placa.length() != 7) {
                imprime("Erro: A placa deve conter exatamente 7 caracteres!");
                placaValida = false;
            } else {
                // Só verifica os caracteres se o tamanho já está correto
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

    // Solicita e valida um horário no formato HH:MM.
    // Critérios de validação:
    //   - deve ter exatamente 5 caracteres
    //   - ':' deve estar obrigatoriamente na posição 2
    //   - hora deve estar entre 00 e 23
    //   - minuto deve estar entre 00 e 59
    public static String lerHorarioValido(Scanner entrada) {
        String horario;
        boolean horarioValido;
        do {
            horarioValido = true; // assume válido e verifica abaixo
            imprime("Informe o horário (HH:MM): ");
            horario = lerString(entrada);

            if (horario.length() == 5 && horario.charAt(2) == ':') {
                // Converte os dígitos de hora e minuto para inteiros.
                // Usamos "" + char para transformar o char em String antes do parseInt.
                int hora   = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
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

    // Converte um horário "HH:MM" para o total equivalente em minutos.
    // Exemplo: "08:30" → 8 * 60 + 30 = 510 minutos.
    // Facilita o cálculo da diferença entre horário de entrada e saída.
    public static int horarioParaMinutos(String horario) {
        int hora   = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
        int minuto = Integer.parseInt("" + horario.charAt(3) + horario.charAt(4));
        return hora * 60 + minuto;
    }

    // Registra a entrada de um veículo:
    //   1. Busca uma vaga livre; encerra se o estacionamento estiver lotado
    //   2. Lê a placa — retorna sem registrar se o usuário cancelar (null)
    //   3. Marca a vaga como ocupada (1), armazena placa e horário de entrada
    public static void registrarEntradaVeiculo(int vetorRegistro[], String vetorPlaca[], String vetorHorario[], Scanner entrada) {
        int posicao = buscarVagaLivre(vetorRegistro);

        if (posicao == -1) {
            imprime("Estacionamento lotado! Não há vagas disponíveis.");
            return; // encerra o método sem registrar
        }

        // lerPlacaValida retorna null se o usuário digitar "0" para cancelar
        String placa = lerPlacaValida(entrada);
        if (placa == null) {
            return; // usuário cancelou, volta sem registrar nada
        }

        vetorRegistro[posicao] = 1;                 // marca vaga como ocupada
        vetorPlaca[posicao]    = placa;             // armazena a placa
        vetorHorario[posicao]  = lerHorarioValido(entrada);// lê e armazena o horário de entrada
        imprime("Entrada registrada com sucesso na vaga " + (posicao + 1) + "!");
    }

    // FUNCIONALIDADE 3 - REGISTRAR SAÍDA DE VEÍCULO


    // Procura um veículo pela placa entre as vagas ocupadas (status = 1).
    // Reutiliza lerPlacaValida(), então o usuário pode digitar "0" para cancelar.
    // Retorna o índice da vaga encontrada, ou -2 se o usuário cancelar.
    public static int procurarVeiculo(int vetorRegistro[], String vetorPlaca[], Scanner entrada) {
        boolean encontrado;
        do {
            String placa = lerPlacaValida(entrada);

            // Se retornou null, o usuário digitou "0" para cancelar
            if (placa == null) {
                return -2; // código especial para indicar cancelamento
            }

            encontrado = false;
            for (int i = 0; i < vetorRegistro.length; i++) {
                // equalsIgnoreCase compara ignorando diferença entre maiúsculas e minúsculas
                if (vetorRegistro[i] == 1 && vetorPlaca[i].equalsIgnoreCase(placa)) {
                    encontrado = true;
                    return i; // retorna a posição do veículo encontrado
                }
            }

            if (!encontrado) {
                imprime("Veículo com placa " + placa + " não encontrado. Tente novamente ou digite 0 para voltar.");
            }

        } while (!encontrado);
        return -2; // nunca alcançado, mas exigido pelo compilador Java
    }

    // Calcula o valor a pagar com base no tempo de permanência e na tarifa.
    // Regra de cobrança:
    //   - até 3 horas: cobra apenas a tarifa fixa inicial
    //   - acima de 3 horas: tarifa fixa + (horas excedentes × tarifa adicional)
    // O tempo é sempre arredondado para cima (cobra hora cheia iniciada).
    public static double calcularValor(int minutosTotal, double tarifas[]) {
        // Math.ceil arredonda para cima
        // Exemplo: 130 min → ceil(130/60.0) = ceil(2.166) = 3 horas cobradas
        int horasTotal = (int) Math.ceil(minutosTotal / 60.0);

        if (horasTotal <= 3) {
            return tarifas[0]; // permanência dentro da franquia: só a tarifa fixa
        } else {
            int horasExtras = horasTotal - 3;
            return tarifas[0] + horasExtras * tarifas[1]; // fixa + excedente
        }
    }

    // --- SUBFUNÇÕES de registrarSaidaVeiculo ---

    // Lê e valida o horário de saída, garantindo que seja >= ao horário de entrada.
    // Continua pedindo até o usuário informar um horário válido.
    public static String lerHorarioSaidaValido(String horarioEntrada, Scanner entrada) {
        String horarioSaida;
        int minutosEntrada = horarioParaMinutos(horarioEntrada);
        do {
            horarioSaida = lerHorarioValido(entrada);
            if (horarioParaMinutos(horarioSaida) < minutosEntrada) {
                imprime("Erro: Horário de saída não pode ser anterior ao de entrada (" + horarioEntrada + ")!");
            }
        } while (horarioParaMinutos(horarioSaida) < minutosEntrada);
        return horarioSaida;
    }

    // Calcula o tempo de permanência a partir dos horários de entrada e saída.
    // Retorna um vetor int[] com três posições:
    //   [0] = horas inteiras de permanência
    //   [1] = minutos restantes (parte fracionária da hora)
    //   [2] = total de minutos (usado para calcular o valor a cobrar)
    public static int[] calcularPermanencia(String horarioEntrada, String horarioSaida) {
        int minutosEntrada = horarioParaMinutos(horarioEntrada);
        int minutosSaida   = horarioParaMinutos(horarioSaida);
        int total = minutosSaida - minutosEntrada;
        return new int[]{ total / 60, total % 60, total };
    }

    // Pergunta ao usuário a forma de pagamento e aplica desconto se necessário.
    // Pagamento via PIX concede 5% de desconto sobre o valor original.
    // Retorna um vetor double[] com duas posições:
    //   [0] = valor final a pagar (após desconto, se houver)
    //   [1] = valor do desconto concedido (0 se não for PIX)
    public static double[] aplicarFormaPagamento(double valorOriginal, Scanner entrada) {
        imprime("Forma de pagamento:\n1 - PIX (5% de desconto)\n2 - Outros");
        int pagamento = lerInt(entrada);

        double desconto = 0;
        double valorFinal = valorOriginal;

        if (pagamento == 1) {
            desconto = valorOriginal * 0.05;
            valorFinal = valorOriginal - desconto;
        }
        return new double[]{ valorFinal, desconto };
    }

    // Exibe no console o resumo completo do atendimento de saída.
    // Recebe todos os dados já calculados pelas subfunções anteriores;
    // sua única responsabilidade é formatar e imprimir.
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
            // Linha de desconto só é exibida quando o desconto é maior que zero
            imprime(String.format("Desconto (PIX 5%%): R$ %.2f", pagamento[1]));
        }
        imprime(String.format("Valor final pago: R$ %.2f", pagamento[0]));
        imprime("===========================\n");
    }

    // Persiste os dados do atendimento encerrado no histórico e libera a vaga.
    // A ordem importa: o histórico é salvo antes de limpar os vetores da vaga,
    // evitando perda de dados caso os ponteiros sejam zerados prematuramente.
    public static void salvarHistoricoELiberarVaga(
            int posicao, String placa, String horarioSaida, double valorFinal,
            int vetorRegistro[], String vetorPlaca[], String vetorHorarioEntrada[],
            String vetorPlacaHistorico[], String vetorEntradaHistorico[],
            String vetorSaidaHistorico[], double vetorValorHistorico[],
            int contadorHistorico[]) {

        // Usa o contador como índice atual e avança após a inserção
        int idx = contadorHistorico[0];
        vetorPlacaHistorico[idx]   = placa;
        vetorEntradaHistorico[idx] = vetorHorarioEntrada[posicao];
        vetorSaidaHistorico[idx]   = horarioSaida;
        vetorValorHistorico[idx]   = valorFinal;
        contadorHistorico[0]++;

        // Libera a vaga somente após os dados estarem salvos no histórico
        vetorRegistro[posicao] = 0;
        vetorPlaca[posicao] = null;
        vetorHorarioEntrada[posicao] = null;
    }

    // Orquestra o fluxo completo de saída de um veículo, delegando cada etapa
    // à sua subfunção correspondente:
    //   1. Localiza o veículo pela placa (com opção de cancelar digitando 0)
    //   2. Lê o horário de saída validando que é >= ao de entrada
    //   3. Calcula tempo de permanência e valor a cobrar
    //   4. Aplica desconto conforme forma de pagamento
    //   5. Exibe o resumo do atendimento
    //   6. Salva no histórico e libera a vaga
    public static void registrarSaidaVeiculo(
            int vetorRegistro[], String vetorPlaca[], String vetorHorarioEntrada[],
            String vetorPlacaHistorico[], String vetorEntradaHistorico[],
            String vetorSaidaHistorico[], double vetorValorHistorico[],
            int contadorHistorico[], double tarifas[], String nomeVeiculo, Scanner entrada) {

        int posicao = procurarVeiculo(vetorRegistro, vetorPlaca, entrada);
        if (posicao == -2) return; // usuário cancelou; volta ao menu sem fazer nada

        // Captura os dados da vaga antes de qualquer alteração nos vetores
        String placa = vetorPlaca[posicao];
        String horarioEntrada = vetorHorarioEntrada[posicao];

        String   horarioSaida  = lerHorarioSaidaValido(horarioEntrada, entrada);
        int[]    permanencia   = calcularPermanencia(horarioEntrada, horarioSaida);
        double   valorOriginal = calcularValor(permanencia[2], tarifas);
        double[] pagamento = aplicarFormaPagamento(valorOriginal, entrada);

        exibirResumoSaida(placa, nomeVeiculo, horarioEntrada, horarioSaida,
                permanencia, valorOriginal, pagamento);

        salvarHistoricoELiberarVaga(
                posicao, placa, horarioSaida, pagamento[0],
                vetorRegistro, vetorPlaca, vetorHorarioEntrada,
                vetorPlacaHistorico, vetorEntradaHistorico,
                vetorSaidaHistorico, vetorValorHistorico, contadorHistorico);
    }


    // FUNCIONALIDADE 4 - GERAR RELATÓRIO DIÁRIO


    // Conta quantos veículos estão atualmente no estacionamento (status = 1 → ocupado)
    public static int contarVeiculosPresentes(int vetorRegistro[]) {
        int contador = 0;
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 1) contador++;
        }
        return contador;
    }

    // --- SUBFUNÇÕES de gerarRelatorioDiario ---

    // Calcula o total de entradas do dia somando:
    //   - veículos ainda presentes (por tipo)
    //   - veículos que já saíram e estão no histórico (por tipo)
    public static int calcularTotalEntradas(
            int carroPequeno[], int contHistCp[],
            int carroGrande[],  int contHistCg[],
            int moto[],         int contHistMoto[]) {

        return contarVeiculosPresentes(carroPequeno) + contHistCp[0]
             + contarVeiculosPresentes(carroGrande)  + contHistCg[0]
             + contarVeiculosPresentes(moto)          + contHistMoto[0];
    }

    // Calcula a média de permanência entre todos os atendimentos finalizados.
    // Percorre os históricos dos três tipos de veículo, soma todos os minutos
    // e divide pelo total de saídas registradas.
    // Retorna int[] com { horas, minutos } da média calculada.
    public static int[] calcularTempoMedioPermanencia(
            String entradaHistCp[],   String saidaHistCp[],   int contHistCp[],
            String entradaHistCg[],   String saidaHistCg[],   int contHistCg[],
            String entradaHistMoto[], String saidaHistMoto[], int contHistMoto[]) {

        int totalSaidas  = contHistCp[0] + contHistCg[0] + contHistMoto[0];
        int somaMinutos  = 0;

        for (int i = 0; i < contHistCp[0]; i++){
            somaMinutos += horarioParaMinutos(saidaHistCp[i])   - horarioParaMinutos(entradaHistCp[i]);
        }
        for (int i = 0; i < contHistCg[0]; i++){
            somaMinutos += horarioParaMinutos(saidaHistCg[i])   - horarioParaMinutos(entradaHistCg[i]);
        }
        for (int i = 0; i < contHistMoto[0]; i++){
            somaMinutos += horarioParaMinutos(saidaHistMoto[i]) - horarioParaMinutos(entradaHistMoto[i]);
        }

        // Operador ternário evita divisão por zero quando não há saídas registradas
        int mediaMinutos = (totalSaidas > 0) ? somaMinutos / totalSaidas : 0;
        return new int[]{ mediaMinutos / 60, mediaMinutos % 60 };
    }

    // Soma o valor total arrecadado no dia percorrendo os históricos
    // dos três tipos de veículo até o limite registrado em cada contador.
    public static double calcularTotalArrecadado(
            double valorHistCp[],   int contHistCp[],
            double valorHistCg[],   int contHistCg[],
            double valorHistMoto[], int contHistMoto[]) {

        double total = 0;
        for (int i = 0; i < contHistCp[0]; i++){
           total += valorHistCp[i];
        }
        for (int i = 0; i < contHistCg[0]; i++){
           total += valorHistCg[i];
        }
        for (int i = 0; i < contHistMoto[0]; i++){
         total += valorHistMoto[i];
        }
        return total;
    }

    // Exibe o relatório diário com os dados já calculados pelas subfunções.
    // Responsabilidade única: formatar e imprimir; nenhum cálculo é feito aqui.
    public static void exibirRelatorioDiario(int totalEntradas, int totalSaidas,
            int[] tempoMedio, double totalArrecadado) {

        imprime("\n===== RELATÓRIO DIÁRIO =====");
        imprime("Total de veículos que entraram: " + totalEntradas);
        imprime("Total de veículos que saíram: "   + totalSaidas);
        imprime("Tempo médio de permanência: " + tempoMedio[0] + "h " + tempoMedio[1] + "min");
        imprime(String.format("Valor total arrecadado: R$ %.2f", totalArrecadado));
        imprime("============================\n");
    }

    // Orquestra as subfunções do relatório diário, delegando cada cálculo
    // à função especializada e passando os resultados para a exibição.
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


    // FUNCIONALIDADE 5 - RELATÓRIO POR TIPO DE VEÍCULO


    // Calcula a média dos valores pagos para um tipo de veículo.
    // Parâmetro quantidade delimita quantas posições do vetor são válidas.
    // Retorna 0 quando não há registros, evitando divisão por zero.
    public static double calcularMediaValor(double vetorValores[], int quantidade) {
        if (quantidade == 0){
            return 0;
        }
        double soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += vetorValores[i];
        }
        return soma / quantidade;
    }

    // --- SUBFUNÇÕES de gerarRelatorioTipoVeiculo ---

    // Compara as quantidades de atendimento dos três tipos e retorna
    // o nome do tipo mais frequente como String.
    // Em caso de empate, a ordem de prioridade é: Carro Pequeno > Carro Grande > Moto.
    public static String determinarTipoMaisFrequente(int qtdCp, int qtdCg, int qtdMoto) {
        if (qtdCp >= qtdCg && qtdCp >= qtdMoto){
            return "Carro Pequeno";
        }
        if (qtdCg >= qtdCp && qtdCg >= qtdMoto){
            return "Carro Grande";
        }
        return "Moto";
    }

    // Exibe o relatório por tipo de veículo com os dados já calculados.
    // Responsabilidade única: formatar e imprimir; nenhum cálculo é feito aqui.
    public static void exibirRelatorioTipoVeiculo(
            int qtdCp,   double mediaCp,
            int qtdCg,   double mediaCg,
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

    // Orquestra as subfunções do relatório por tipo, calculando médias e
    // determinando o tipo mais frequente antes de chamar a exibição.
    public static void gerarRelatorioTipoVeiculo(
            int carroPequeno[], int carroGrande[], int moto[],
            double valorHistCp[],   int contHistCp[],
            double valorHistCg[],   int contHistCg[],
            double valorHistMoto[], int contHistMoto[]) {

        int qtdCp   = contHistCp[0];
        int qtdCg   = contHistCg[0];
        int qtdMoto = contHistMoto[0];

        double mediaCp   = calcularMediaValor(valorHistCp,   qtdCp);
        double mediaCg   = calcularMediaValor(valorHistCg,   qtdCg);
        double mediaMoto = calcularMediaValor(valorHistMoto, qtdMoto);

        String maisFrequente = determinarTipoMaisFrequente(qtdCp, qtdCg, qtdMoto);

        exibirRelatorioTipoVeiculo(qtdCp, mediaCp, qtdCg, mediaCg, qtdMoto, mediaMoto, maisFrequente);
    }

    // =========================================================
    // MÉTODO PRINCIPAL - ponto de entrada do programa
    // =========================================================
    public static void main(String[] args) {

        // ----- Vetores do estacionamento (vagas ativas) -----
        // vetorRegistro: 0 = livre, 1 = ocupado
        int[]    carroPequeno               = new int[100];
        String[] placasCarroPequeno         = new String[100];
        String[] horariosEntradaCarroPequeno = new String[100];
        double[] valorTarifasCarroPequeno   = new double[2]; // [0]=3h fixo, [1]=hora extra

        int[]    carroGrande               = new int[100];
        String[] placasCarroGrande         = new String[100];
        String[] horariosEntradaCarroGrande = new String[100];
        double[] valorTarifasCarroGrande   = new double[2];

        int[]    moto               = new int[100];
        String[] placasMoto         = new String[100];
        String[] horariosEntradaMoto = new String[100];
        double[] valorTarifasMoto   = new double[2];

        // ----- Vetores de histórico (veículos que já saíram) -----
        // Separados dos vetores de vagas para não misturar dados ativos com finalizados.
        // Tamanho 1000 para comportar muitos atendimentos ao longo do dia.
        String[] placasHistCp  = new String[1000];
        String[] entradaHistCp = new String[1000];
        String[] saidaHistCp   = new String[1000];
        double[] valorHistCp   = new double[1000];
        // Contador em vetor de tamanho 1: int simples passado como parâmetro é copiado
        // (não é referência); com vetor, o método receptor consegue alterar contHistCp[0].
        int[] contHistCp = new int[1];

        String[] placasHistCg  = new String[1000];
        String[] entradaHistCg = new String[1000];
        String[] saidaHistCg   = new String[1000];
        double[] valorHistCg   = new double[1000];
        int[]    contHistCg    = new int[1];

        String[] placasHistMoto  = new String[1000];
        String[] entradaHistMoto = new String[1000];
        String[] saidaHistMoto   = new String[1000];
        double[] valorHistMoto   = new double[1000];
        int[]    contHistMoto    = new int[1];

        Scanner entrada = new Scanner(System.in);
        int opcao;

        // Loop principal: o menu se repete até o usuário escolher sair (opção 6)
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
            entrada.nextLine(); // limpa buffer após nextInt

            int escolha;
            switch (opcao) {

                // ----- OPÇÃO 1: Cadastrar Tarifas -----
                case 1:
                    do {
                        subMenu();
                        escolha = lerInt(entrada);
                        if (escolha == 1) {
                            cadastrarTarifas(valorTarifasCarroPequeno, entrada);
                        } else if (escolha == 2) {
                            cadastrarTarifas(valorTarifasCarroGrande, entrada);
                        } else if (escolha == 3) {
                            cadastrarTarifas(valorTarifasMoto, entrada);
                        } else if (escolha == 4) {
                            break; // volta ao menu principal
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                // ----- OPÇÃO 2: Registrar Entrada -----
                case 2:
                    do {
                        subMenu();
                        escolha = lerInt(entrada);
                        if (escolha == 1) {
                            registrarEntradaVeiculo(carroPequeno, placasCarroPequeno, horariosEntradaCarroPequeno, entrada);
                        } else if (escolha == 2) {
                            registrarEntradaVeiculo(carroGrande, placasCarroGrande, horariosEntradaCarroGrande, entrada);
                        } else if (escolha == 3) {
                            registrarEntradaVeiculo(moto, placasMoto, horariosEntradaMoto, entrada);
                        } else if (escolha == 4) {
                            break;
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                // ----- OPÇÃO 3: Registrar Saída -----
                case 3:
                    do {
                        subMenu();
                        escolha = lerInt(entrada);
                        if (escolha == 1) {
                            registrarSaidaVeiculo(
                                    carroPequeno, placasCarroPequeno, horariosEntradaCarroPequeno,
                                    placasHistCp, entradaHistCp, saidaHistCp, valorHistCp,
                                    contHistCp, valorTarifasCarroPequeno, "Carro Pequeno", entrada);
                        } else if (escolha == 2) {
                            registrarSaidaVeiculo(
                                    carroGrande, placasCarroGrande, horariosEntradaCarroGrande,
                                    placasHistCg, entradaHistCg, saidaHistCg, valorHistCg,
                                    contHistCg, valorTarifasCarroGrande, "Carro Grande", entrada);
                        } else if (escolha == 3) {
                            registrarSaidaVeiculo(
                                    moto, placasMoto, horariosEntradaMoto,
                                    placasHistMoto, entradaHistMoto, saidaHistMoto, valorHistMoto,
                                    contHistMoto, valorTarifasMoto, "Moto", entrada);
                        } else if (escolha == 4) {
                            break;
                        } else {
                            imprime("Opção inválida! Tente novamente.");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;

                // ----- OPÇÃO 4: Relatório Diário -----
                case 4:
                    gerarRelatorioDiario(
                            carroPequeno, carroGrande, moto,
                            placasHistCp, entradaHistCp, saidaHistCp, valorHistCp, contHistCp,
                            placasHistCg, entradaHistCg, saidaHistCg, valorHistCg, contHistCg,
                            placasHistMoto, entradaHistMoto, saidaHistMoto, valorHistMoto, contHistMoto);
                    break;

                // ----- OPÇÃO 5: Relatório por Tipo -----
                case 5:
                    gerarRelatorioTipoVeiculo(
                            carroPequeno, carroGrande, moto,
                            valorHistCp, contHistCp,
                            valorHistCg, contHistCg,
                            valorHistMoto, contHistMoto);
                    break;

                // ----- OPÇÃO 6: Sair -----
                case 6:
                    imprime("Encerrando o sistema. Até logo!");
                    break;

                default:
                    imprime("\nOPÇÃO INVÁLIDA! Selecione uma opção entre 1 e 6.\n");
            }

        } while (opcao != 6);

        entrada.close(); // fecha o Scanner ao encerrar o programa
    }
}
