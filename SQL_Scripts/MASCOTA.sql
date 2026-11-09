BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE mascota CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE mascota_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_mascota';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;

CREATE SEQUENCE seq_mascota START WITH 1 INCREMENT BY 1;
/

CREATE TABLE mascota
(
    id_mascota       NUMBER PRIMARY KEY,
    nombre           VARCHAR2(100),
    especie          VARCHAR2(100),
    raza             VARCHAR2(100),
    edad             NUMBER,
    sexo             VARCHAR2(20),
    fecha_ingreso    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_adopcion   TIMESTAMP,
    id_adoptante     NUMBER,
    estado           NUMBER DEFAULT 1,
    CONSTRAINT fk_mascota_adoptante FOREIGN KEY (id_adoptante) REFERENCES adoptante(id_adoptante)
);

COMMENT ON TABLE mascota IS 'Tabla entidad de mascota';
COMMENT ON COLUMN mascota.id_mascota IS 'ID number de identificador de tabla mascota';
COMMENT ON COLUMN mascota.nombre IS 'Nombre de la mascota';
COMMENT ON COLUMN mascota.especie IS 'Especie de la mascota';
COMMENT ON COLUMN mascota.raza IS 'Raza de la mascota';
COMMENT ON COLUMN mascota.edad IS 'Edad de la mascota';
COMMENT ON COLUMN mascota.sexo IS 'Sexo de la mascota';
COMMENT ON COLUMN mascota.fecha_ingreso IS 'Fecha de ingreso de la mascota al refugio';
COMMENT ON COLUMN mascota.fecha_adopcion IS 'Fecha en que la mascota fue adoptada';
COMMENT ON COLUMN mascota.id_adoptante IS 'ID del adoptante (llave foránea)';
COMMENT ON COLUMN mascota.estado IS 'Estado lógico de la mascota';

CREATE TABLE mascota_bit
(
    id_mascota_bit     VARCHAR2(36) NOT NULL,
    id_mascota         NUMBER,
    nombre             VARCHAR2(100),
    especie            VARCHAR2(100),
    raza               VARCHAR2(100),
    edad               NUMBER,
    sexo               VARCHAR2(20),
    fecha_ingreso      TIMESTAMP,
    fecha_adopcion     TIMESTAMP,
    id_adoptante       NUMBER,
    estado             NUMBER DEFAULT 1,
    fecha_creacion     DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion             VARCHAR2(25),
    ip                 VARCHAR2(30),
    CONSTRAINT pk_id_mascota_bit PRIMARY KEY (id_mascota_bit)
);

COMMENT ON TABLE mascota_bit IS 'Tabla bitácora de mascotas';
COMMENT ON COLUMN mascota_bit.id_mascota_bit IS 'ID UUID de identificador de tabla mascota_bit';
COMMENT ON COLUMN mascota_bit.id_mascota IS 'ID de mascota para auditoría';
COMMENT ON COLUMN mascota_bit.nombre IS 'Nombre de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.especie IS 'Especie de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.raza IS 'Raza de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.edad IS 'Edad de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.sexo IS 'Sexo de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.fecha_ingreso IS 'Fecha de ingreso para auditoría';
COMMENT ON COLUMN mascota_bit.fecha_adopcion IS 'Fecha de adopción para auditoría';
COMMENT ON COLUMN mascota_bit.id_adoptante IS 'ID del adoptante para auditoría';
COMMENT ON COLUMN mascota_bit.estado IS 'Estado lógico de la mascota para auditoría';
COMMENT ON COLUMN mascota_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN mascota_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN mascota_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN mascota_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_mascota_bit
    AFTER INSERT OR UPDATE ON mascota
    FOR EACH ROW
DECLARE
    v_accion                VARCHAR2(20);
    v_fecha_creacion        DATE := NULL;
    v_fecha_modificacion    DATE := NULL;
BEGIN
    -- Validación para INSERT
    if inserting then
        v_accion := 'insert';
        v_fecha_creacion := SYSDATE;

    -- Validación para UPDATE
    elsif updating then
        v_fecha_modificacion := SYSDATE;
        v_fecha_creacion := :old.fecha_ingreso; -- Usando fecha_ingreso como referencia de creación

        IF :old.estado = 1 AND :new.estado = 0 THEN
            v_accion := 'Desactivate';
        ELSIF :old.estado = 0 AND :new.estado = 1 THEN
            v_accion := 'Activate';
        ELSE
            v_accion := 'UPDATE';
        END IF;

    END IF;

    INSERT INTO mascota_bit (
        id_mascota_bit, id_mascota, nombre, especie, raza, edad, sexo,
        fecha_ingreso, fecha_adopcion, id_adoptante, estado,
        fecha_creacion, fecha_modificacion, accion, ip
    )
    VALUES (
        SYS_GUID(),
        :new.id_mascota,
        :new.nombre,
        :new.especie,
        :new.raza,
        :new.edad,
        :new.sexo,
        :new.fecha_ingreso,
        :new.fecha_adopcion,
        :new.id_adoptante,
        :new.estado,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/