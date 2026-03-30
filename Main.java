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
                                "3 - Moto");
    }
    //função de escolha para chamada dos metodos
    //public static int escolhaSubmenu(int escolha){
     //   
    //}
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
        do{
        imprime("informe o horario de entrada (HH:MM): ");
        horario = lerString();
        for (int i = 0; i < horario.length(); i++) {
            if(horario.charAt(i) == '-' || horario.charAt(i) == ' '){
                imprime("Horário não pode conter espaços e hifens!");
            }
        }
        }while(horario.length() != 5 || horario.charAt(2) != ':' || horario.charAt(0) == '0' || horario.charAt(0) == '1' || horario.charAt(0) == '2' ||
                horario.charAt(1) < '0' || horario.charAt(1) > '9' || horario.charAt(3) < '0' || horario.charAt(3) > '5' ||
                horario.charAt(4) < '0' || horario.charAt(4) > '9');
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
    // FUNCIONALIDADE 3 - REGISTRAR SAIDA
    // varrer listas procurando a posição pela placa
    public static int localizarVeículoPlaca(String placaBuscada, String[] placasCarroPequeno,
                                             String[] placasCarroGrande, String[] placasMoto){
        for (int i = 0; i < placasCarroPequeno.length; i++) {
            if (placasCarroPequeno[i] != null && placasCarroPequeno[i].equals(placaBuscada)) {
                return i;
            }
        }
        for (int i = 0; i < placasCarroGrande.length; i++) {
            if (placasCarroGrande[i] != null && placasCarroGrande[i].equals(placaBuscada)) {
                return i;
            }
        }
        for (int i = 0; i < placasMoto.length; i++) {
            if (placasMoto[i] != null && placasMoto[i].equals(placaBuscada)) {
                return i;
            }
        }
        return -1; //não encontrado
    }
    public static void registrarSaidaVeiculo(String placaBuscada, String placasCarroPequeno[],
                                             String placasCarroGrande[], String placasMoto[]){

        int posicao = localizarVeículoPlaca(placaBuscada,placasCarroPequeno, placasCarroGrande, placasMoto );


    }
    public static void main(String[] args) {
        int[] carroPequeno = new int[100];
        String[] placasCarroPequeno = new String[100];
        String[] horariosEntradaCarroPequeno = new String[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroPequeno = new double[2];


        int[] carroGrande = new int[100];
        String[] placasCarroGrande = new String[100];
        String[] horariosEntradaCarroGrande = new String[100];
        // Vetor que armazena o valor das TARIFAS - 3 primeiras horas na pos 0 e valor extra na pos 1.
        double[] valorTarifasCarroGrande = new double[2];

        int[] moto = new int[100];
        String[] placasMoto = new String[100];
        String[] horariosEntradaMoto = new String[100];
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
                        } else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 3);
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
                        } else {
                            imprime("Veículo inválido!");
                        }
                    } while (escolha < 1 || escolha > 3);
                    break;
                case 3:
                    imprime("Informe a placa a ser buscada: ");
                    String placa = lerString();
                    registrarSaidaVeiculo(placa,placasCarroPequeno,placasCarroGrande,placasMoto);
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

