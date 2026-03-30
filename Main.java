import java.util.Scanner;
//moabe
//haramaki
public class Main
{
    //funçoes que imprime conteúdo
    public static void imprime(String texto){
        System.out.println(texto);
    }
    //funçao para sintetizar o menu
    public static void subMenu(){
        imprime("- Escolha qual veículo:-\n" +
                                "1 - Carro pequeno\n" +
                                "2 - Carro Grande\n" +
                                "3 - Moto\n" +
                                "4 - Voltar ao menu principal");
    }
    //função de escolha para chamada dos metodos
    public static double lerDouble(){
        Scanner entrada = new Scanner(System.in);
        double valor = entrada.nextDouble();
        //LIMPA O BUFFER
        entrada.nextLine();
        return valor;
    }
    public static int lerInt(){
        Scanner entrada = new Scanner(System.in);
        int valor = entrada.nextInt();
        //LIMPA O BUFFER
        entrada.nextLine();
        return valor;
    }
    public static String lerString(){
        Scanner entrada = new Scanner(System.in);
        return entrada.nextLine();
    }

    // CADASTRAR TARIFAS - FUNCIONALIDADE 1
    public static void cadastrarTarifas(double vetor[]){
        imprime("Informe o valor da tarifa das 3 primeiras horas: ");
        vetor[0] = lerDouble();
        imprime("Informe o valor da tarifa de hora adicional: ");
        vetor[1] = lerDouble();
    }

    // REGISTRAR ENTRADA DE VEÍCULO - FUNCIONALIDADE 2
    //Separei em funções menores para ficar mais legivel na principal
    //buscar vagas livre para usar posição
    public static int buscarVagaLivre(int vetorRegistro[]) {
        for (int i = 0; i < vetorRegistro.length; i++) {
            if (vetorRegistro[i] == 0) {
                return i;
            }
        }
        return -1;
    }
    // função validar placa
    public static String lerPlacaValida(){
        String placa;
        boolean placaValida;
        do {
            //inicializa como true e vai mudando como false caso encontre algum erro
            placaValida = true;
            imprime("Informe a placa com 7 caracteres: ");
            placa = lerString();
            if (placa.length() != 7) {
                imprime("Placa deve conter exatamente 7 caracteres!");
                placaValida = false;
            }
            for(int i = 0; i < placa.length(); i++){
                if(placa.charAt(i) == '-' || placa.charAt(i) == ' '){
                    imprime("Placa não pode conter espaços e hifens!");
                    placaValida = false;
                    break;
                }
            }
            
            
        } while (!placaValida);
        return placa;
    }
    // Função validar horário
    public static String lerHorarioValido() {
        String horario;
        Boolean horarioValido;
        do{
            //inicializa como true e vai mudando como false caso encontre algum erro
            horarioValido = true;
            imprime("informe o horario (HH:MM): ");
            horario = lerString();
            //Verifica se tem 5 caracteres e o ':' na posição correta
            if (horario.length() == 5 && horario.charAt(2) == ':') {
                //Usamos o integer parse para converter os caracteres em numeros e validar hora e minuto 
                // e pesquisamos e vimos que em java "" + char converte o char para string para o parseInt funcionar"
                int hora = Integer.parseInt("" + horario.charAt(0) + horario.charAt(1));
                int minuto = Integer.parseInt("" + horario.charAt(3) + horario.charAt(4));
            // valida hora e minuto
                if(hora < 0 || hora > 23) {
                    imprime("Hora deve ser entre 00 e 23!");
                    horarioValido = false;
                }
                else if(minuto < 0 || minuto > 59) {
                    imprime("Minuto deve ser entre 00 e 59!");
                    horarioValido = false;
                }
            } else {
                imprime("Horário deve estar no formato HH:MM!");
                horarioValido = false;
            }
        } while(!horarioValido);
        return horario;
    }

    //principal função registrarEntrada
    public static void registrarEntradaVeiculo(int vetorRegistro[], String vetorPlaca[], String vetorHorario[]) {
        // buscar vaga
        int posicao = buscarVagaLivre(vetorRegistro);
        if (posicao == -1) {
            imprime("Estacionamento lotado!");
        }
        // 2. ocupar vaga
        vetorRegistro[posicao] = 1;
        // 3. ler placa
        vetorPlaca[posicao] = lerPlacaValida();
        // 4. ler horário
        vetorHorario[posicao] = lerHorarioValido();
    }
    // FUNCIONALIDADE 3 - 3.1 REGISTRAR SAIDA
    //função para procurar o veículo
    public static int procurarVeiculo(int vetorRegistro[], String vetorPlaca[]) {
        boolean encontrado;
        do{ 
            imprime("Informe a placa do veículo que deseja registrar a saída: ");
            String placa = lerPlacaValida();
            encontrado = false;
            for (int i = 0; i < vetorRegistro.length; i++) {
                if (vetorRegistro[i] == 1 && vetorPlaca[i].equalsIgnoreCase(placa)) {
                    encontrado = true;
                    return i;
                }
            }
            if (!encontrado) {
                imprime("Veículo com placa " + placa + " não encontrado no estacionamento. Tente novamente.");
            }
        } while(!encontrado);
            return -1;
    }
    //função para solicitar horário de saída
    public static String solicitarHorarioSaida(){
        String horarioSaida;
        horarioSaida = lerHorarioValido();
        return horarioSaida;
    }
    public static void registrarSaidaVeiculo(int vetorRegistro[], String vetorPlaca[], String vetorHorarioSaida[]) {
        // procurar veículo
        int posicao = procurarVeiculo(vetorRegistro, vetorPlaca);
        if (posicao != -1) {
            // solicitar horário de saída
            String horarioSaida = solicitarHorarioSaida();
            // registrar saída (colocar -1 onde tem 1, e adicionar hrario de saída no vetor saidaa)
            vetorRegistro[posicao] = -1;
            vetorPlaca[posicao] = null;
            vetorHorarioSaida[posicao] = horarioSaida;
        }
    }

    public static void main(String[] args) {
        int[] carroPequeno = new int[100];
        String[] placasCarroPequeno = new String[100];
        String[] horariosEntradaCarroPequeno = new String[100];
        String[] horariosSaidaCarroPequeno = new String[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroPequeno = new double[2];


        int[] carroGrande = new int[100];
        String[] placasCarroGrande = new String[100];
        String[] horariosEntradaCarroGrande = new String[100];
        String[] horariosSaidaCarroGrande = new String[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroGrande = new double[2];

        int[] moto = new int[100];
        String[] placasMoto = new String[100];
        String[] horariosEntradaMoto = new String[100];
        String[] horariosSaidaMoto = new String[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasMoto = new double[2];

        Scanner entrada = new Scanner(System.in);
        int opcao;
        do {
            imprime("=== SISTEMA DE CONTROLE DE ESTACIONAMENTO ===\n" +
                    "1. Cadastrar Tarifas\n" +
                    "2. Registrar Entrada de Veículo\n" +
                    "3. Registrar Saída de Veículo\n" +
                    "4. Gerar Relatório diário\n" +
                    "5. Gerar Relatório por tipo de veículo\n" +
                    "6. Sair\n" +
                    "Selecione uma opção:");
            opcao = entrada.nextInt();
            switch (opcao) {
                case 1:
                    int escolha;
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
                        }
                        else {
                            imprime("Veículo inválido!");
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
                        }
                        else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;
                case 3:
                    do {
                        subMenu();
                        escolha = lerInt();
                        if (escolha == 1) {
                           registrarSaidaVeiculo(carroPequeno, placasCarroPequeno, horariosSaidaCarroPequeno);
                        } else if (escolha == 2) {
                           registrarSaidaVeiculo(carroGrande, placasCarroGrande, horariosSaidaCarroGrande);
                        } else if (escolha == 3) {
                           registrarSaidaVeiculo(moto, placasMoto, horariosSaidaMoto);
                        } else if (escolha == 4) {
                            break;
                        }
                        else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 4);
                    break;
                case 4:
                    //gerarRelatorioDiario();
                    break;
                case 5:
                    //gerarRelatorioTipoVeiculo();
                    break;
                case 6:
                    break;
                default:
                    imprime("\nOPÇÃO INVÁLIDA\n");
            }
        }while (opcao != 6);
    }
}

