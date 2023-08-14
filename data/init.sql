CREATE DATABASE CajeroAutomaticoDB2;
USE CajeroAutomaticoDB2;

CREATE TABLE Usuarios (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    PIN INT NOT NULL,
    Saldo DECIMAL(10, 2) NOT NULL,
    Estado ENUM('ACTIVO', 'BLOQUEADO') NOT NULL
);

CREATE TABLE HistoricoOperaciones (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UsuarioID INT,
    TipoOperacion ENUM('DEPOSITO', 'RETIRO') NOT NULL,
    Monto DECIMAL(10, 2) NOT NULL,
    FechaOperacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Usuarios (Nombre, PIN, Saldo, Estado) VALUES ('Alice', 1111, 1500.00, 'ACTIVO');
INSERT INTO Usuarios (Nombre, PIN, Saldo, Estado) VALUES ('Bob', 2222, 2000.00, 'ACTIVO');
INSERT INTO Usuarios (Nombre, PIN, Saldo, Estado) VALUES ('Carol', 3333, 800.00, 'ACTIVO');
INSERT INTO Usuarios (Nombre, PIN, Saldo, Estado) VALUES ('David', 4444, 1200.00, 'ACTIVO');

-- Depósito de $200 en la cuenta de Alice
INSERT INTO HistoricoOperaciones (UsuarioID, TipoOperacion, Monto) VALUES (1, 'DEPOSITO', 200.00);

-- Retiro de $50 de la cuenta de Bob
INSERT INTO HistoricoOperaciones (UsuarioID, TipoOperacion, Monto) VALUES (2, 'RETIRO', 50.00);

-- Depósito de $100 en la cuenta de Carol
INSERT INTO HistoricoOperaciones (UsuarioID, TipoOperacion, Monto) VALUES (3, 'DEPOSITO', 100.00);

-- Retiro de $300 de la cuenta de David
INSERT INTO HistoricoOperaciones (UsuarioID, TipoOperacion, Monto) VALUES (4, 'RETIRO', 300.00);

