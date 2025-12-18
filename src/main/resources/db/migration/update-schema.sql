ALTER TABLE articulo
    MODIFY codigo VARCHAR(255);

ALTER TABLE pedido
    MODIFY codigoArticulo VARCHAR(255);

ALTER TABLE articulo
    MODIFY descripcion VARCHAR(255);

ALTER TABLE articulo
    MODIFY descripcion VARCHAR(255) NULL;

ALTER TABLE cliente
    MODIFY domicilio VARCHAR(255);

ALTER TABLE cliente
    MODIFY domicilio VARCHAR(255) NULL;

ALTER TABLE cliente
    MODIFY email VARCHAR(255);

ALTER TABLE cliente
    MODIFY email VARCHAR(255) NULL;

ALTER TABLE pedido
    MODIFY fechaHora datetime NULL;

ALTER TABLE articulo
    DROP COLUMN gastosEnvio;

ALTER TABLE articulo
    DROP COLUMN precio;

ALTER TABLE articulo
    ADD gastosEnvio DOUBLE NOT NULL;

ALTER TABLE cliente
    MODIFY nif VARCHAR(255);

ALTER TABLE pedido
    MODIFY nifCliente VARCHAR(255);

ALTER TABLE cliente
    MODIFY nombre VARCHAR(255);

ALTER TABLE cliente
    MODIFY nombre VARCHAR(255) NULL;

ALTER TABLE articulo
    ADD precio DOUBLE NOT NULL;

ALTER TABLE articulo
    MODIFY tiempoPreparacion INT NULL;

ALTER TABLE cliente
    MODIFY tipo VARCHAR(255);

ALTER TABLE cliente
    MODIFY tipo VARCHAR(255) NULL;
ALTER TABLE articulo
    MODIFY codigo VARCHAR(255);

ALTER TABLE pedido
    MODIFY codigoArticulo VARCHAR(255);

ALTER TABLE articulo
    MODIFY descripcion VARCHAR(255);

ALTER TABLE articulo
    MODIFY descripcion VARCHAR(255) NULL;

ALTER TABLE cliente
    MODIFY domicilio VARCHAR(255);

ALTER TABLE cliente
    MODIFY domicilio VARCHAR(255) NULL;

ALTER TABLE cliente
    MODIFY email VARCHAR(255);

ALTER TABLE cliente
    MODIFY email VARCHAR(255) NULL;

ALTER TABLE pedido
    MODIFY fechaHora datetime NULL;

ALTER TABLE articulo
    DROP COLUMN gastosEnvio;

ALTER TABLE articulo
    DROP COLUMN precio;

ALTER TABLE articulo
    ADD gastosEnvio DOUBLE NOT NULL;

ALTER TABLE cliente
    MODIFY nif VARCHAR(255);

ALTER TABLE pedido
    MODIFY nifCliente VARCHAR(255);

ALTER TABLE cliente
    MODIFY nombre VARCHAR(255);

ALTER TABLE cliente
    MODIFY nombre VARCHAR(255) NULL;

ALTER TABLE articulo
    ADD precio DOUBLE NOT NULL;

ALTER TABLE articulo
    MODIFY tiempoPreparacion INT NULL;

ALTER TABLE cliente
    MODIFY tipo VARCHAR(255);

ALTER TABLE cliente
    MODIFY tipo VARCHAR(255) NULL;