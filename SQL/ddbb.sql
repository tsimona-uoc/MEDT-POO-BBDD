USE MEDT_POO_DDBB;

DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS articulo;
DROP TABLE IF EXISTS cliente;

/*
ARTICULO
*/
create table articulo (
                          codigo NVARCHAR(200) PRIMARY KEY,
                          descripcion NVARCHAR(200) NOT NULL,
                          precio DECIMAL NOT NULL,
                          gastosEnvio DECIMAL NOT NULL,
                          tiempoPreparacion INT NOT NULL
);

/*
CLIENTE
*/
create table cliente (
                         nif nvarchar(20) PRIMARY KEY,
                         nombre nvarchar(200) NOT NULL,
                         domicilio nvarchar(200) NOT NULL,
                         email nvarchar(200) NOT NULL,
                         tipo nvarchar(20) NOT NULL
);

/*
PEDIDO
*/
create table pedido (
                        numeroPedido INT PRIMARY KEY,
                        fechaHora DATETIME NOT NULL,
                        cantidad INT NOT NULL,
                        codigoArticulo NVARCHAR(200),
                        nifCliente NVARCHAR(20),

                        FOREIGN KEY (codigoArticulo) REFERENCES articulo(codigo) ON DELETE CASCADE,
                        FOREIGN KEY (nifCliente) REFERENCES cliente(nif) ON DELETE CASCADE
);

DROP PROCEDURE IF EXISTS obtenerPedidosEnviados;

-- STORE PROCEDURE PARA RECUPERAR LOS PEDIDOS ENVIADOS
DELIMITER //
CREATE PROCEDURE obtenerPedidosEnviados(IN nif VARCHAR(50))
BEGIN
    IF nif = '' THEN
        select p.*, a.*, c.* from
            pedido p
                inner join articulo a on p.codigoArticulo = a.codigo
                inner join cliente c on c.nif = p.nifCliente
        where
            ADDTIME(p.fechaHora, SEC_TO_TIME(60 * a.tiempoPreparacion)) < NOW();
    ELSE
        select p.*, a.*, c.* from
            pedido p
                inner join articulo a on p.codigoArticulo = a.codigo
                inner join cliente c on c.nif = p.nifCliente
        where
            ADDTIME(p.fechaHora, SEC_TO_TIME(60 * a.tiempoPreparacion)) < NOW()
          AND p.nifCliente = nif;
    END IF;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS obtenerPedidosPendientes;

-- STORE PROCEDURE PARA RECUPERAR LOS PEDIDOS ENVIADOS
DELIMITER //
CREATE PROCEDURE obtenerPedidosPendientes(IN nif VARCHAR(50))
BEGIN
    IF nif = '' THEN
        select p.*, a.*, c.* from
            pedido p
                inner join articulo a on p.codigoArticulo = a.codigo
                inner join cliente c on c.nif = p.nifCliente
        where
            ADDTIME(p.fechaHora, SEC_TO_TIME(60 * a.tiempoPreparacion)) >= NOW();
    ELSE
        select p.*, a.*, c.* from
            pedido p
                inner join articulo a on p.codigoArticulo = a.codigo
                inner join cliente c on c.nif = p.nifCliente
        where
            ADDTIME(p.fechaHora, SEC_TO_TIME(60 * a.tiempoPreparacion)) >= NOW()
          AND p.nifCliente = nif;
    END IF;
END //