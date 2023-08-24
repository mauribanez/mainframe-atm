package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperacionesBancariasService {
    String query = "SELECT id, saldo, nombre FROM usuarios WHERE pin = ?";


    public static void consultarSaldo(Connection connection) {
        saldo = resultSet.getDouble("saldo");
        System.out.println("Su saldo actual es: $" + saldo);
    }

    public static void realizarDeposito(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else {
            try {
                saldo += cantidad;
                actualizarSaldoEnBaseDeDatos(connection, saldo);
                registrarOperacion(connection, "DEPOSITO", cantidad); // Registrar la operación en historico
                System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al actualizar el saldo en la base de datos.");
            }
        }
    }

    public static void realizarRetiro(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            try {
                saldo -= cantidad;
                actualizarSaldoEnBaseDeDatos(connection, saldo);
                registrarOperacion(connection, "RETIRO", cantidad); // Registrar la operación en historico
                System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al actualizar el saldo en la base de datos.");
            }
        }
    }

    public static void cambiarPIN(Connection connection) {
        Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese su PIN actual: ");
    int pinIngresado = scanner.nextInt();

    if (pinIngresado == pinActual) {
        System.out.print("Ingrese su nuevo PIN: ");
        int nuevoPin = scanner.nextInt();
        System.out.print("Confirme su nuevo PIN: ");
        int confirmacionPin = scanner.nextInt();

        if (nuevoPin == confirmacionPin) {
            try {
                String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, nuevoPin);
                    updateStatement.setInt(2, usuarioId);
                    updateStatement.executeUpdate();
                }
                pinActual = nuevoPin;
                System.out.println("PIN actualizado con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al actualizar el PIN en la base de datos.");
            }
        } else {
            System.out.println("Los PINs no coinciden.");
        }
    } else {
        System.out.println("PIN incorrecto.");
    }
    }

    private static void actualizarSaldoEnBaseDeDatos(Connection connection, double nuevoSaldo, int usuarioId) throws SQLException {
        String query = "UPDATE usuarios SET saldo = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, nuevoSaldo);
            preparedStatement.setInt(2, usuarioId);
            preparedStatement.executeUpdate();
        }
    }

    private static void registrarOperacion(Connection connection, int usuarioId, String tipoOperacion, double cantidad) {
        String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, usuarioId);
            insertStatement.setString(2, tipoOperacion);
            insertStatement.setDouble(3, cantidad);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar la operación en la tabla historico.");
        }
    }
}
