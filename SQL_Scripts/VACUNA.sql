BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE vacuna CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE vacuna_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_vacuna';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;


CREATE SEQUENCE seq_vacuna START WITH 1 INCREMENT BY 1;
/

CREATE TABLE vacuna
(
    id_vacuna          NUMBER PRIMARY KEY,
    nombre_vacuna      VARCHAR2(100),
    descripcion        VARCHAR2(250),
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE vacuna IS 'Catálogo de tipos de vacunas disponibles';
COMMENT ON COLUMN vacuna.id_vacuna IS 'ID principal de la vacuna';
COMMENT ON COLUMN vacuna.nombre_vacuna IS 'Nombre de la vacuna';
COMMENT ON COLUMN vacuna.descripcion IS 'Descripción general de la vacuna';
COMMENT ON COLUMN vacuna.estado IS 'Estado lógico de la vacuna';
COMMENT ON COLUMN vacuna.fecha_registro IS 'Fecha de registro en la db';

CREATE TABLE vacuna_bit
(
    id_vacuna_bit      VARCHAR2(36) NOT NULL,
    id_vacuna          NUMBER,
    nombre_vacuna      VARCHAR2(100),
    descripcion        VARCHAR2(250),
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP,
    fecha_creacion     DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion             VARCHAR2(25),
    ip                 VARCHAR2(30),
    CONSTRAINT pk_id_vacuna_bit PRIMARY KEY (id_vacuna_bit)
);

COMMENT ON TABLE vacuna_bit IS 'Tabla bitácora del catálogo de vacunas';
COMMENT ON COLUMN vacuna_bit.id_vacuna_bit IS 'ID UUID de identificador de tabla vacuna_bit';
COMMENT ON COLUMN vacuna_bit.id_vacuna IS 'ID de vacuna para auditoría';
COMMENT ON COLUMN vacuna_bit.nombre_vacuna IS 'Nombre de la vacuna para auditoría';
COMMENT ON COLUMN vacuna_bit.descripcion IS 'Descripción de la vacuna para auditoría';
COMMENT ON COLUMN vacuna_bit.estado IS 'Estado lógico de la vacuna para auditoría';
COMMENT ON COLUMN vacuna_bit.fecha_registro IS 'Fecha de registro de la vacuna para auditoría';
COMMENT ON COLUMN vacuna_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN vacuna_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN vacuna_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN vacuna_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_vacuna_bit
    AFTER INSERT OR UPDATE ON vacuna
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
        v_fecha_creacion := :old.fecha_registro;

        IF :old.estado = 1 AND :new.estado = 0 THEN
            v_accion := 'Desactivate';
        ELSIF :old.estado = 0 AND :new.estado = 1 THEN
            v_accion := 'Activate';
        ELSE
            v_accion := 'UPDATE';
        END IF;

    END IF;

    INSERT INTO vacuna_bit (
        id_vacuna_bit, id_vacuna, nombre_vacuna, descripcion,
        estado, fecha_registro, fecha_creacion, fecha_modificacion, accion, ip
    ) VALUES (
        SYS_GUID(),
        :new.id_vacuna,
        :new.nombre_vacuna,
        :new.descripcion,
        :new.estado,
        :new.fecha_registro,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/