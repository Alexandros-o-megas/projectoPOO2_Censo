/*package censo.mz.service;

import censo.mz.util.AnsiColors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validacao {
    private static final Pattern NOME_PATTERN = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]+$"); 
    private static final Pattern CONTACTO_PATTERN = Pattern.compile("^\\+?\\d{7,15}$");

    public static String validarStringNaoVazia(Scanner scanner, String msg) {
        String input;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + AnsiColors.RESET);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(AnsiColors.RED + "Este campo não pode estar vazio. Tente novamente." + AnsiColors.RESET);
        }
    }
    
    public static String validarNome(Scanner scanner, String msg) {
        String nome;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + AnsiColors.RESET);
            nome = scanner.nextLine().trim();
            if (!nome.isEmpty() && NOME_PATTERN.matcher(nome).matches() && nome.length() >= 3) {
                return nome;
            }
            System.out.println(AnsiColors.RED + "Nome inválido. Deve conter pelo menos 3 caracteres e apenas letras, espaços e '.-. Tente novamente." + AnsiColors.RESET);
        }
    }

    public static String validarGenero(Scanner scanner, String msg) {
        String genero;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + " (Masculino/Feminino/Outro): " + AnsiColors.RESET);
            genero = scanner.nextLine().trim().toLowerCase();
            if (genero.equals("masculino") || genero.equals("feminino") || genero.equals("outro")) {
                return genero.substring(0, 1).toUpperCase() + genero.substring(1); // Capitaliza
            }
            System.out.println(AnsiColors.RED + "Género inválido. Use 'Masculino', 'Feminino' ou 'Outro'. Tente novamente." + AnsiColors.RESET);
        }
    }

    public static String validarEstadoCivil(Scanner scanner, String msg) {
        String estado;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + " (Solteiro/Casado/Divorciado/Viúvo): " + AnsiColors.RESET);
            estado = scanner.nextLine().trim().toLowerCase();
            if (estado.equals("solteiro") || estado.equals("casado") || estado.equals("divorciado") || estado.equals("viuvo") || estado.equals("viúvo")) {
                return estado.substring(0, 1).toUpperCase() + estado.substring(1); // Capitaliza
            }
            System.out.println(AnsiColors.RED + "Estado civil inválido. Use 'Solteiro', 'Casado', 'Divorciado' ou 'Viúvo'. Tente novamente." + AnsiColors.RESET);
        }
    }
    
    // Para campos de texto gerais como ocupacao, nacionalidade
    public static String validarOtherString(Scanner scanner, String msg) {
         String input;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + AnsiColors.RESET);
            input = scanner.nextLine().trim();
            if (!input.isEmpty() && input.length() >= 2) {
                return input;
            }
            System.out.println(AnsiColors.RED + "Entrada inválida. Tente novamente." + AnsiColors.RESET);
        }
    }


    public static byte validarByte(Scanner scanner, String msg, byte min, byte max) {
        byte valor;
        while (true) {
            try {
                System.out.print(AnsiColors.CYAN + msg + " (" + min + "-" + max + "): " + AnsiColors.RESET);
                valor = scanner.nextByte();
                scanner.nextLine(); // Consumir newline
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(AnsiColors.RED + "Valor fora do intervalo permitido [" + min + "-" + max + "]. Tente novamente." + AnsiColors.RESET);
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.RED + "Entrada inválida. Por favor, insira um número inteiro pequeno." + AnsiColors.RESET);
                scanner.nextLine();
            }
        }
    }
    
    public static String validarId(Scanner scanner, String msg) {
        String id;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + AnsiColors.RESET);
            id = scanner.nextLine().trim();
            if (!id.isEmpty() && ID_PATTERN.matcher(id).matches()) {
                return id;
            }
            System.out.println(AnsiColors.RED + "ID inválido. Deve ser alfanumérico e pode conter '-' ou '_'. Não pode estar vazio. Tente novamente." + AnsiColors.RESET);
        }
    }
    
    public static String[] validarMultiplosContactos(Scanner scanner, String msg) {
        System.out.println(AnsiColors.CYAN + msg + AnsiColors.RESET + AnsiColors.YELLOW + " (Insira os contactos separados por vírgula, ex: +258841234567,+258827654321. Deixe em branco para nenhum.):" + AnsiColors.RESET);
        String linha = scanner.nextLine().trim();
        if (linha.isEmpty()) {
            return new String[0];
        }
        String[] contactosInput = linha.split(",");
        java.util.List<String> contactosValidos = new java.util.ArrayList<>();
        for (String c : contactosInput) {
            String contactoTrimmed = c.trim();
            if (CONTACTO_PATTERN.matcher(contactoTrimmed).matches()) {
                contactosValidos.add(contactoTrimmed);
            } else if (!contactoTrimmed.isEmpty()) {
                 System.out.println(AnsiColors.RED + "Contacto '" + contactoTrimmed + "' inválido. Será ignorado." + AnsiColors.RESET);
            }
        }
        return contactosValidos.toArray(new String[0]);
    }
    
    public static String[] validarMultiplosEnderecos(Scanner scanner, String msg) {
        System.out.println(AnsiColors.CYAN + msg + AnsiColors.RESET + AnsiColors.YELLOW + " (Insira os endereços separados por ';' Ex: Rua A, Bairro B; Av. C, Cidade D. Deixe em branco para nenhum.):" + AnsiColors.RESET);
        String linha = scanner.nextLine().trim();
        if (linha.isEmpty()) {
            return new String[0];
        }
        String[] enderecosInput = linha.split(";");
        java.util.List<String> enderecosValidos = new java.util.ArrayList<>();
        for (String e : enderecosInput) {
            String enderecoTrimmed = e.trim();
            if (!enderecoTrimmed.isEmpty()) {
                 enderecosValidos.add(enderecoTrimmed);
            }
        }
        return enderecosValidos.toArray(new String[0]);
    }


    public static Date validarDate(Scanner scanner, String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); 
        String dataStr;
        while (true) {
            System.out.print(AnsiColors.CYAN + msg + " (dd/MM/yyyy): " + AnsiColors.RESET);
            dataStr = scanner.nextLine().trim();
            try {
                Date data = sdf.parse(dataStr);
                if (data.after(new Date())) {
                     System.out.println(AnsiColors.RED + "Data de nascimento não pode ser no futuro. Tente novamente." + AnsiColors.RESET);
                     continue;
                }
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.YEAR, -120);
                if (data.before(cal.getTime())) {
                    System.out.println(AnsiColors.RED + "Data de nascimento muito antiga. Verifique e tente novamente." + AnsiColors.RESET);
                    continue;
                }
                return data;
            } catch (ParseException e) {
                System.out.println(AnsiColors.RED + "Formato de data inválido. Use dd/MM/yyyy. Tente novamente." + AnsiColors.RESET);
            }
        }
    }
    
    public static String validarPassword(Scanner scanner, String msg) {
        String pass;
        while(true) {
            System.out.print(AnsiColors.CYAN + msg + AnsiColors.RESET);
            pass = scanner.nextLine();
            if (pass.length() >= 4) { 
                return pass;
            }
            System.out.println(AnsiColors.RED + "A password deve ter pelo menos 4 caracteres." + AnsiColors.RESET);
        }
    }

    public static int validarIntervalo(Scanner scanner, String msg, int ini, int fim) {
        int valor;
        while (true) {
            try {
                System.out.print(AnsiColors.CYAN + msg + " (" + ini + "-" + fim + "): " + AnsiColors.RESET);
                valor = scanner.nextInt();
                scanner.nextLine(); 
                if (valor >= ini && valor <= fim) {
                    return valor;
                }
                System.out.println(AnsiColors.RED + "Valor fora do intervalo permitido [" + ini + "-" + fim + "]. Tente novamente." + AnsiColors.RESET);
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.RED + "Entrada inválida. Por favor, insira um número inteiro." + AnsiColors.RESET);
                scanner.nextLine();
            }
        }
    }
}*/