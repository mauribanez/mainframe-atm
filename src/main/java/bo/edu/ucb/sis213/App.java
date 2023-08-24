package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = Database.getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (UsuarioDAO.validarPIN(connection, pinIngresado)) {
                Usuario usuario = UsuarioDAO.obtenerUsuarioPorPIN(connection, pinIngresado);
                mostrarMenu(usuario, connection);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }

    public static void mostrarMenu(Usuario usuario, Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nBienvenido, " + usuario.getNombre() + ", al Menú Principal:");

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
                    UsuarioBO.consultarSaldo(usuario);
                    break;
                case 2:
                    UsuarioBO.realizarDeposito(connection, usuario);
                    break;
                case 3:
                    UsuarioBO.realizarRetiro(connection, usuario);
                    break;
                case 4:
                    UsuarioBO.cambiarPIN(connection, usuario);
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
