import java.util.Scanner;

// Projeto 01 - Sistema de Controle de Estacionamento
// Disciplina: Programação de Sistemas I - 2026-01
// Autores: Moabe e Haramaki

public class Main {

    // =========================================================
    // MÉTODOS UTILITÁRIOS - usados em várias partes do sistema
    // =========================================================

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
    public static double lerDouble() {
        Scanner entrada = new Scanner(System.in);
        double valor = entrada.nextDouble();
        entrada.nextLine(); // limpa o buffer para não afetar a próxima leitura
        return valor;
    }

    // Lê um número inteiro do teclado e limpa o buffer
    public static int lerInt() {
        Scanner entrada = new Scanner(System.in);
        int valor = entrada.nextInt();
        entrada.nextLine(); // limpa o buffer
        return valor;
    }

    // Lê uma linha de texto do teclado
    public static String lerString() {
        Scanner entrada = new Scanner(System.in);
        return entrada.nextLine();
    }

    // =========================================================
    // FUNCIONALIDADE 1 - CADASTRAR TARIFAS
    // =========================================================

    // Cadastra as tarifas para um tipo de veículo
    // vetor[0] = valor das 3 primeiras horas
    // vetor[1] = valor de cada hora adicional
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
    // O usuário pode digitar "0" a qualquer momento para CANCELAR e voltar ao menu.
    // Retorna a placa válida (em maiúsculo), ou null se o usuário cancelar.
    // Regras de validação:
    // - deve ter exatamente 7 caracteres
    // - não pode conter espaços ou hífens
    public static String lerPlacaValida() {
        String placa;
        boolean placaValida;
        do {
            placaValida = true; // assume válida e verifica abaixo
            imprime("Informe a placa com 7 caracteres (ou digite 0 para voltar): ");
            placa = lerString().toUpperCase(); // padroniza em maiúsculo

            // Verifica se o usuário quer cancelar a operação
            if (placa.equals("0")) {
                imprime("Operação cancelada. Voltando ao menu...");
                return null; // null é o sinal de cancelamento para quem chamou este método
            }

            if (placa.length() != 7) {
                imprime("Erro: A placa deve conter exatamente 7 caracteres!");
                placaValida = false;
                continue; // volta para pedir de novo sem checar o resto
            }

            // Verifica cada caractere em busca de espaço ou hífen
            for (int i = 0; i < placa.length(); i++) {
                if (placa.charAt(i) == '-' || placa.charAt(i) == ' ') {
                    imprime("Erro: A placa não pode conter espaços ou hífens!");
                    placaValida = false;
                    break;
                }
            }

        } while (!placaValida);
        return placa;
    }

    // Solicita e valida um horário no formato HH:MM:
    // - deve ter exatamente 5 caracteres
    // - ':' deve estar na posição 2
    // - hora entre 00 e 23
    // - minuto entre 00 e 59
    public static String lerHorarioValido() {
        String horario;
        boolean horarioValido;
        do {
            horarioValido = true; // assume válido e verifica abaixo
            imprime("Informe o horário (HH:MM): ");
            horario = lerString();

            if (horario.length() == 5 && horario.charAt(2) == ':') {
                // Converte os dígitos de hora e minuto para inteiros
                // Usamos "" + char para transformar o char em String antes de fazer parseInt
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

    // Converte um horário "HH:MM" para o total em minutos
    // Exemplo: "08:30" → 510 minutos
    // Isso facilita o cálculo da diferença entre entrada e saída
    public static int horarioParaMinutos(String horario) {
        int hora = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
        int minuto = Integer.parseInt("" + horario.charAt(3) + horario.charAt(4));
        return hora * 60 + minuto;
    }

    // Registra a entrada de um veículo:
    // 1. Busca vaga livre
    // 2. Lê a placa — se o usuário cancelar (retorno null), encerra sem registrar
    // 3. Marca a vaga como ocupada (1)
    // 4. Lê e armazena o horário de entrada
    public static void registrarEntradaVeiculo(int vetorRegistro[], String vetorPlaca[], String vetorHorario[]) {
        int posicao = buscarVagaLivre(vetorRegistro);

        if (posicao == -1) {
            imprime("Estacionamento lotado! Não há vagas disponíveis.");
            return; // encerra o método sem registrar
        }

        // Tenta ler a placa — lerPlacaValida retorna null se o usuário digitar "0"
        String placa = lerPlacaValida();
        if (placa == null) {
            return; // usuário cancelou, volta sem registrar nada
        }

        vetorRegistro[posicao] = 1;                    // marca vaga como ocupada
        vetorPlaca[posicao] = placa;                   // armazena a placa
        vetorHorario[posicao] = lerHorarioValido();    // lê e armazena o horário de entrada
        imprime("Entrada registrada com sucesso na vaga " + (posicao + 1) + "!");
    }

    // =========================================================
    // FUNCIONALIDADE 3 - REGISTRAR SAÍDA DE VEÍCULO
    // =========================================================

    // Procura um veículo pela placa no estacionamento.
    // Só considera vagas com status 1 (ocupadas).
    // Reutiliza lerPlacaValida(), então o usuário pode digitar "0" para cancelar.
    // Retorna -2 se o usuário cancelar, ou o índice da vaga se encontrar o veículo.
    public static int procurarVeiculo(int vetorRegistro[], String vetorPlaca[]) {
        boolean encontrado;
        do {
            // Reutiliza lerPlacaValida para aproveitar validações e a opção de cancelamento
            String placa = lerPlacaValida();

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
        return -2; // nunca chega aqui, mas o Java exige um return no final
    }

    // Calcula o valor a pagar com base no tempo de permanência e na tarifa.
    // Regra: primeiras 3 horas = tarifa fixa; cada hora acima disso = tarifa adicional.
    // O tempo é arredondado para cima (cobra hora cheia).
    public static double calcularValor(int minutosTotal, double tarifas[]) {
        // Math.ceil arredonda para cima
        // Ex: 130 minutos → ceil(130/60.0) = ceil(2.166) = 3 horas
        int horasTotal = (int) Math.ceil(minutosTotal / 60.0);

        if (horasTotal <= 3) {
            // Permanência dentro das primeiras 3 horas: cobra só a tarifa inicial
            return tarifas[0];
        } else {
            // Cobra a tarifa inicial + horas excedentes × tarifa adicional
            int horasExtras = horasTotal - 3;
            return tarifas[0] + horasExtras * tarifas[1];
        }
    }

    // Registra a saída de um veículo:
    // 1. Localiza o veículo pela placa (com opção de cancelar digitando 0)
    // 2. Lê o horário de saída validando que é >= horário de entrada
    // 3. Calcula tempo de permanência e valor a pagar
    // 4. Pergunta a forma de pagamento (desconto de 5% no PIX)
    // 5. Exibe o resumo completo
    // 6. Libera a vaga e salva os dados no histórico para os relatórios
    public static void registrarSaidaVeiculo(
            int vetorRegistro[], String vetorPlaca[], String vetorHorarioEntrada[],
            String vetorPlacaHistorico[], String vetorEntradaHistorico[],
            String vetorSaidaHistorico[], double vetorValorHistorico[],
            int contadorHistorico[], double tarifas[], String nomeVeiculo) {

        // Localiza o veículo — retorna -2 se o usuário cancelou digitando "0"
        int posicao = procurarVeiculo(vetorRegistro, vetorPlaca);
        if (posicao == -2) {
            return; // usuário cancelou, volta ao menu sem fazer nada
        }

        // Lê o horário de saída garantindo que seja >= horário de entrada
        String horarioSaida;
        int minutosEntrada = horarioParaMinutos(vetorHorarioEntrada[posicao]);
        int minutosSaida;
        do {
            horarioSaida = lerHorarioValido();
            minutosSaida = horarioParaMinutos(horarioSaida);
            if (minutosSaida < minutosEntrada) {
                imprime("Erro: Horário de saída não pode ser anterior ao de entrada (" + vetorHorarioEntrada[posicao] + ")!");
            }
        } while (minutosSaida < minutosEntrada);

        // Calcula tempo de permanência em horas e minutos
        int minutosTotal = minutosSaida - minutosEntrada;
        int horas = minutosTotal / 60;
        int minutos = minutosTotal % 60;

        // Calcula o valor original a pagar de acordo com a tarifa do tipo de veículo
        double valorOriginal = calcularValor(minutosTotal, tarifas);

        // Pergunta a forma de pagamento
        imprime("Forma de pagamento:\n1 - PIX (5% de desconto)\n2 - Outros");
        int pagamento = lerInt();

        double desconto = 0;
        double valorFinal = valorOriginal;

        if (pagamento == 1) {
            // Aplica 5% de desconto para pagamento via PIX
            desconto = valorOriginal * 0.05;
            valorFinal = valorOriginal - desconto;
        }

        // Salva a placa em variável local ANTES de limpar a vaga
        // Importante: se limparmos vetorPlaca[posicao] antes de exibir o resumo,
        // a placa some e o resumo fica incompleto
        String placaSalva = vetorPlaca[posicao];

        // Exibe o resumo da saída
        imprime("\n===== RESUMO DA SAÍDA =====");
        imprime("Placa: " + placaSalva);
        imprime("Tipo de veículo: " + nomeVeiculo);
        imprime("Horário de entrada: " + vetorHorarioEntrada[posicao]);
        imprime("Horário de saída: " + horarioSaida);
        imprime("Tempo de permanência: " + horas + "h " + minutos + "min");
        imprime(String.format("Valor original: R$ %.2f", valorOriginal));
        if (desconto > 0) {
            imprime(String.format("Desconto (PIX 5%%): R$ %.2f", desconto));
        }
        imprime(String.format("Valor final pago: R$ %.2f", valorFinal));
        imprime("===========================\n");

        // Salva os dados no histórico para uso nos relatórios (func. 4 e 5)
        int idx = contadorHistorico[0]; // posição atual no vetor de histórico
        vetorPlacaHistorico[idx] = placaSalva;
        vetorEntradaHistorico[idx] = vetorHorarioEntrada[posicao];
        vetorSaidaHistorico[idx] = horarioSaida;
        vetorValorHistorico[idx] = valorFinal;
        contadorHistorico[0]++; // avança o ponteiro do histórico

        // Libera a vaga no estacionamento (só após ter salvo tudo)
        vetorRegistro[posicao] = 0;
        vetorPlaca[posicao] = null;
        vetorHorarioEntrada[posicao] = null;
    }

    // =========================================================
    // FUNCIONALIDADE 4 - GERAR RELATÓRIO DIÁRIO
    // =========================================================

    // Conta quantos veículos estão atualmente no estacionamento (status = 1)
    public static int contarVeiculosPresentes(int vetorRegistro[]) {
        int contador = 0;
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 1) {
                contador++;
            }
        }
        return contador;
    }

    // Gera o relatório diário consolidando dados de todos os tipos de veículo
    public static void gerarRelatorioDiario(
            int carroPequeno[], int carroGrande[], int moto[],
            String placasHistCp[], String entradaHistCp[], String saidaHistCp[], double valorHistCp[], int contHistCp[],
            String placasHistCg[], String entradaHistCg[], String saidaHistCg[], double valorHistCg[], int contHistCg[],
            String placasHistMoto[], String entradaHistMoto[], String saidaHistMoto[], double valorHistMoto[], int contHistMoto[]) {

        // Total de entradas = veículos ainda presentes + veículos que já saíram
        int totalEntradas = contarVeiculosPresentes(carroPequeno) + contHistCp[0]
                + contarVeiculosPresentes(carroGrande) + contHistCg[0]
                + contarVeiculosPresentes(moto) + contHistMoto[0];

        // Total de saídas = soma dos contadores de histórico dos três tipos
        int totalSaidas = contHistCp[0] + contHistCg[0] + contHistMoto[0];

        // Soma os minutos de permanência de todos os veículos finalizados
        int somaMinutos = 0;
        for (int i = 0; i < contHistCp[0]; i++) {
            somaMinutos += horarioParaMinutos(saidaHistCp[i]) - horarioParaMinutos(entradaHistCp[i]);
        }
        for (int i = 0; i < contHistCg[0]; i++) {
            somaMinutos += horarioParaMinutos(saidaHistCg[i]) - horarioParaMinutos(entradaHistCg[i]);
        }
        for (int i = 0; i < contHistMoto[0]; i++) {
            somaMinutos += horarioParaMinutos(saidaHistMoto[i]) - horarioParaMinutos(entradaHistMoto[i]);
        }

        // Calcula a média (o operador ternário evita divisão por zero)
        int mediaMinutos = (totalSaidas > 0) ? somaMinutos / totalSaidas : 0;
        int mediaHoras = mediaMinutos / 60;
        int mediaMin = mediaMinutos % 60;

        // Soma o valor total arrecadado no dia
        double totalArrecadado = 0;
        for (int i = 0; i < contHistCp[0]; i++) totalArrecadado += valorHistCp[i];
        for (int i = 0; i < contHistCg[0]; i++) totalArrecadado += valorHistCg[i];
        for (int i = 0; i < contHistMoto[0]; i++) totalArrecadado += valorHistMoto[i];

        // Exibe o relatório
        imprime("\n===== RELATÓRIO DIÁRIO =====");
        imprime("Total de veículos que entraram: " + totalEntradas);
        imprime("Total de veículos que saíram: " + totalSaidas);
        imprime("Tempo médio de permanência: " + mediaHoras + "h " + mediaMin + "min");
        imprime(String.format("Valor total arrecadado: R$ %.2f", totalArrecadado));
        imprime("============================\n");
    }

    // =========================================================
    // FUNCIONALIDADE 5 - RELATÓRIO POR TIPO DE VEÍCULO
    // =========================================================

    // Calcula a média dos valores pagos de um tipo de veículo
    // quantidade = número de registros válidos no vetor
    public static double calcularMediaValor(double vetorValores[], int quantidade) {
        if (quantidade == 0) return 0; // evita divisão por zero
        double soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += vetorValores[i];
        }
        return soma / quantidade;
    }

    // Gera o relatório separado por tipo de veículo
    public static void gerarRelatorioTipoVeiculo(
            int carroPequeno[], int carroGrande[], int moto[],
            double valorHistCp[], int contHistCp[],
            double valorHistCg[], int contHistCg[],
            double valorHistMoto[], int contHistMoto[]) {

        // Quantidade de atendimentos finalizados por tipo
        int qtdCp = contHistCp[0];
        int qtdCg = contHistCg[0];
        int qtdMoto = contHistMoto[0];

        // Determina qual tipo de veículo teve mais atendimentos
        String maisFrequente;
        if (qtdCp >= qtdCg && qtdCp >= qtdMoto) {
            maisFrequente = "Carro Pequeno";
        } else if (qtdCg >= qtdCp && qtdCg >= qtdMoto) {
            maisFrequente = "Carro Grande";
        } else {
            maisFrequente = "Moto";
        }

        // Calcula a média de valor pago por tipo
        double mediaCp = calcularMediaValor(valorHistCp, qtdCp);
        double mediaCg = calcularMediaValor(valorHistCg, qtdCg);
        double mediaMoto = calcularMediaValor(valorHistMoto, qtdMoto);

        // Exibe o relatório
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

    // =========================================================
    // MÉTODO PRINCIPAL - ponto de entrada do programa
    // =========================================================
    public static void main(String[] args) {

        // ----- Vetores do estacionamento (vagas ativas) -----
        // vetorRegistro: 0 = livre, 1 = ocupado
        int[] carroPequeno = new int[100];
        String[] placasCarroPequeno = new String[100];
        String[] horariosEntradaCarroPequeno = new String[100];
        double[] valorTarifasCarroPequeno = new double[2]; // [0]=3h fixo, [1]=hora extra

        int[] carroGrande = new int[100];
        String[] placasCarroGrande = new String[100];
        String[] horariosEntradaCarroGrande = new String[100];
        double[] valorTarifasCarroGrande = new double[2];

        int[] moto = new int[100];
        String[] placasMoto = new String[100];
        String[] horariosEntradaMoto = new String[100];
        double[] valorTarifasMoto = new double[2];

        // ----- Vetores de histórico (veículos que já saíram) -----
        // Separados dos vetores de vagas para não misturar dados ativos com finalizados.
        // Tamanho 1000 para comportar muitos atendimentos no dia.
        String[] placasHistCp = new String[1000];
        String[] entradaHistCp = new String[1000];
        String[] saidaHistCp = new String[1000];
        double[] valorHistCp = new double[1000];
        // Usamos vetor de tamanho 1 para o contador: em Java, int simples passado como
        // parâmetro é copiado (não é referência), então o método não conseguiria alterar
        // o valor original. Com vetor, passamos a referência e o método pode modificar contHistCp[0].
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
            entrada.nextLine(); // limpa o buffer após leitura do int

            int escolha;
            switch (opcao) {

                // ----- OPÇÃO 1: Cadastrar Tarifas -----
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

                // ----- OPÇÃO 3: Registrar Saída -----
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
