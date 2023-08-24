import bo.edu.ucb.sis213.logica_negocio.OperacionesBancariasService;

import java.sql.Connection;
import java.util.Scanner;

public class MenuPrincipal {

    public static void mostrarMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    OperacionesBancariasService.consultarSaldo(connection);
                    break;
                case 2:
                    OperacionesBancariasService.realizarDeposito(connection);
                    break;
                case 3:
                    OperacionesBancariasService.realizarRetiro(connection);
                    break;
                case 4:
                    OperacionesBancariasService.cambiarPIN(connection);
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
}
