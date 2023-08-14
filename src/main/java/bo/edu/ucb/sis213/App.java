package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CajeroAutomaticoDB2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";
    private static double saldo = 0.0; // Agrega la variable de saldo aquí
    private static int pinActual = 0; // Agrega la variable de pinActual aquí

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Bienvenido al Cajero Automático.");
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int intentos = 3;
    
            while (intentos > 0) {
                System.out.print("Ingrese su PIN de 4 dígitos: ");
                int pinIngresado = scanner.nextInt();
    
                if (validarPIN(connection, pinIngresado)) {
                    mostrarMenu(connection); // Pasar la conexión al menú
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean validarPIN(Connection connection, int pinIngresado) {
        String query = "SELECT PIN, saldo, Nombre FROM Usuarios WHERE PIN = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pinIngresado);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    pinActual = resultSet.getInt("PIN");
                    saldo = resultSet.getDouble("saldo");
                    String nombreUsuario = resultSet.getString("Nombre");
                    System.out.println("Bienvenido, " + nombreUsuario + ", al Cajero Automático.");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción
        }
        return false;
    }

    private static void actualizarSaldoEnBaseDeDatos(Connection connection) throws SQLException {
        String query = "UPDATE Usuarios SET saldo = ? WHERE PIN = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, saldo);
            statement.setInt(2, pinActual);
            statement.executeUpdate();
        }
    }
    
    
    

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
                    consultarSaldo(connection);
                    break;
                case 2:
                    realizarDeposito(connection);
                    break;
                case 3:
                    realizarRetiro(connection);
                    break;
                case 4:
                    cambiarPIN();
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

    public static void consultarSaldo(Connection connection) {
        try {
            cargarDatosDesdeBaseDeDatos(connection); // Cargar datos actuales desde la base de datos
            System.out.println("Su saldo actual es: $" + saldo);
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción aquí, puede imprimir un mensaje de error
        }
    }
    
    
    public static void realizarDeposito(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();
    
        try {
            if (cantidad <= 0) {
                System.out.println("Cantidad no válida.");
            } else {
                cargarDatosDesdeBaseDeDatos(connection); // Cargar datos actuales desde la base de datos
                saldo += cantidad;
                //actualizarSaldoEnBaseDeDatos(connection);
                System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción aquí, puede imprimir un mensaje de error
        }
    }
    
    public static void realizarRetiro(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();
    
        try {
            if (cantidad <= 0) {
                System.out.println("Cantidad no válida.");
            } else {
                cargarDatosDesdeBaseDeDatos(connection); // Cargar datos actuales desde la base de datos
                if (cantidad > saldo) {
                    System.out.println("Saldo insuficiente.");
                } else {
                    saldo -= cantidad;
                    //actualizarSaldoEnBaseDeDatos(connection);
                    System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción aquí, puede imprimir un mensaje de error
        }
    }
    private static void cargarDatosDesdeBaseDeDatos(Connection connection) throws SQLException {
        String query = "SELECT saldo FROM Usuarios WHERE PIN = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pinActual);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    saldo = resultSet.getDouble("saldo");
                }
            }
        }
    }
    

    public static void cambiarPIN() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su PIN actual: ");
        int pinIngresado = scanner.nextInt();

        if (pinIngresado == pinActual) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                pinActual = nuevoPin;
                System.out.println("PIN actualizado con éxito.");
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }
    
}
