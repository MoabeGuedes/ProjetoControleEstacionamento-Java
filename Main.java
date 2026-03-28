import java.util.Scanner;
//moabe
//haramaki
public class Main
{
    //função que imprime conteúdo
    public static void imprime(String texto){
        System.out.println(texto);
    }
    public static double lerDouble(){
        Scanner entrada = new Scanner(System.in);
        return entrada.nextDouble();
    }
    public static int lerInt(){
        Scanner entrada = new Scanner(System.in);
        return entrada.nextInt();
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
    public static void registrarEntradaVeiculo(int vetorRegistro[], String vetorPlaca[], double vetorHorario[]){
        for(int i = 0;i < vetorRegistro.length; i++ ){
            if(vetorRegistro[i] == 0){
                vetorRegistro[i] = 1;
                break;
            }
            break;
        }
        imprime("Informe a placa: ");
        for(int i = 0;i < vetorRegistro.length; i++ ){
            if(vetorPlaca[i] == null){
                do{
                    vetorPlaca[i] = lerString();
                    //perguntar se pode usar o charAt para validar a placa
                    if (vetorPlaca[i] == "") {
                         imprime("Placa inválida!\n");
                    }
                }while((vetorPlaca[i].isEmpty()));
                break;
            }
            break;
        }
        imprime("Informe o horário de entrada nesse formato: 00h.00min ");
        for (int i = 0; i < vetorHorario.length; i++) {
            if(vetorHorario[i] == 0.0){
                double valor;
                int hora, minutos;
                do {
                    valor = lerDouble();
                    hora = (int) valor;
                    //Perguntar se pode usar math, aqui estamos arredondando o valor para evitar 00.59
                    minutos = (int) Math.round((valor - hora) * 100);

                    if (hora < 0 || hora > 23 || minutos < 0 || minutos > 59) {
                        imprime("Horário inválido!");
                    }
                } while (hora < 0 || hora > 23 || minutos < 0 || minutos > 59);
                vetorHorario[i] = valor;
                break;
            }
            break;
        }
    }
    public static void main(String[] args) {
        int[] carroPequeno = new int[100];
        String[] placasCarroPequeno = new String[100];
        double[] horariosEntradaCarroPequeno = new double[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroPequeno = new double[2];


        int[] carroGrande = new int[100];
        String[] placasCarroGrande = new String[100];
        double[] horariosEntradaCarroGrande = new double[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroGrande = new double[2];

        int[] moto = new int[100];
        String[] placasMoto = new String[100];
        double[] horariosEntradaMoto = new double[100];
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
            //LIMPA O BUFFER
            entrada.nextLine();
            switch (opcao) {
                case 1:
                    int escolha;
                    do {
                        imprime("- Escolha qual veículo quer cadastrar tarifa -\n" +
                                "1 - Carro pequeno\n" +
                                "2 - Carro Grande\n" +
                                "3 - Moto");
                        escolha = lerInt();
                        //LIMPA O BUFFER
                        entrada.nextLine();
                        if (escolha == 1) {
                            cadastrarTarifas(valorTarifasCarroPequeno);
                        } else if (escolha == 2) {
                            cadastrarTarifas(valorTarifasCarroGrande);
                        } else if (escolha == 3) {
                            cadastrarTarifas(valorTarifasMoto);
                        } else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 3);
                    break;
                case 2:
                    do {
                        imprime("- Escolha qual veículo quer resgistrar entrada -\n" +
                                "1 - Carro pequeno\n" +
                                "2 - Carro Grande\n" +
                                "3 - Moto");
                        escolha = lerInt();
                        if (escolha == 1) {
                            registrarEntradaVeiculo(carroPequeno, placasCarroPequeno, horariosEntradaCarroPequeno);
                        } else if (escolha == 2) {
                            registrarEntradaVeiculo(carroGrande, placasCarroGrande, horariosEntradaCarroGrande);
                        } else if (escolha == 3) {
                            registrarEntradaVeiculo(moto, placasMoto, horariosEntradaMoto);
                        } else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 3);
                    break;
                case 3:
                    //registrarSaidaVeiculo();
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

