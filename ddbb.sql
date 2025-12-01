/* 1. REINICIAR BASE DE DATOS (OPCIONAL PERO RECOMENDADO) */
DROP DATABASE IF EXISTS MEDT_POO_DDBB;
CREATE DATABASE MEDT_POO_DDBB;
USE MEDT_POO_DDBB;

/* 2. LIMPIEZA DE TABLAS */
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS articulo;
DROP TABLE IF EXISTS cliente;

/* 3. CREACIÓN DE TABLAS */

/* ARTICULO */
CREATE TABLE articulo (
    codigo NVARCHAR(200) PRIMARY KEY,
    descripcion NVARCHAR(200) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,      /* (10,2) permite guardar céntimos */
    gastosEnvio DECIMAL(10,2) NOT NULL, /* (10,2) permite guardar céntimos */
    tiempoPreparacion INT NOT NULL
);

/* CLIENTE */
CREATE TABLE cliente (
    nif NVARCHAR(20) PRIMARY KEY,
    nombre NVARCHAR(200) NOT NULL,
    domicilio NVARCHAR(200) NOT NULL,
    email NVARCHAR(200) NOT NULL,
    tipo NVARCHAR(20) NOT NULL
);

/* PEDIDO */
CREATE TABLE pedido (
    numeroPedido INT PRIMARY KEY,
    fechaHora DATETIME NOT NULL,
    cantidad INT NOT NULL,
    codigoArticulo NVARCHAR(200),
    nifCliente NVARCHAR(20),

    FOREIGN KEY (codigoArticulo) REFERENCES articulo(codigo) ON DELETE CASCADE,
    FOREIGN KEY (nifCliente) REFERENCES cliente(nif) ON DELETE CASCADE
);