BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE adoptante CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE adoptante_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_adoptante';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;


CREATE SEQUENCE seq_adoptante START WITH 1 INCREMENT BY 1;
/

CREATE TABLE adoptante
(
    id_adoptante   NUMBER PRIMARY KEY,
    nombre         VARCHAR2(100),
    apellido       VARCHAR2(100),
    telefono       VARCHAR2(20),
    correo         VARCHAR2(150) UNIQUE,
    direccion      VARCHAR2(200),
    estado         NUMBER DEFAULT 1,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE adoptante IS 'Tabla entidad de adoptante';
COMMENT ON COLUMN adoptante.id_adoptante IS 'ID number de identificador de tabla';
COMMENT ON COLUMN adoptante.nombre IS 'nombre de adoptante';
COMMENT ON COLUMN adoptante.apellido IS 'apellido de adoptante';
COMMENT ON COLUMN adoptante.telefono IS 'telefono de adoptante';
COMMENT ON COLUMN adoptante.correo IS 'correo de adoptante';
COMMENT ON COLUMN adoptante.direccion IS 'dirección del adoptante';
COMMENT ON COLUMN adoptante.estado IS 'estado logico del adoptante';
COMMENT ON COLUMN adoptante.fecha_registro IS 'fecha de registro en db del adoptante';

CREATE TABLE adoptante_bit
(
    id_adoptante_bit VARCHAR2(36) NOT NULL,
    id_adoptante     NUMBER,
    nombre           VARCHAR2(100),
    apellido         VARCHAR2(100),
    telefono         VARCHAR2(20),
    correo           VARCHAR2(150),
    direccion        VARCHAR2(200), -- Dirección del adoptante para auditoría
    estado           NUMBER DEFAULT 1,
    fecha_registro   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion   DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion           VARCHAR2(25),
    ip               VARCHAR2(30),
    CONSTRAINT pk_id_adoptante_bit PRIMARY KEY (id_adoptante_bit)
);

COMMENT ON TABLE adoptante_bit IS 'Tabla bitácora de adoptantes';
COMMENT ON COLUMN adoptante_bit.id_adoptante_bit IS 'ID UUID de identificador de tabla adoptante_bit';
COMMENT ON COLUMN adoptante_bit.id_adoptante IS 'ID de adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.nombre IS 'Nombre del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.apellido IS 'Apellido del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.telefono IS 'Teléfono del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.correo IS 'Correo del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.direccion IS 'Dirección del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.estado IS 'Estado lógico del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.fecha_registro IS 'Fecha de registro del adoptante para auditoría';
COMMENT ON COLUMN adoptante_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN adoptante_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN adoptante_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN adoptante_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_adoptante_bit
    AFTER INSERT OR UPDATE ON adoptante
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

    INSERT INTO adoptante_bit (
        id_adoptante_bit, id_adoptante, nombre, apellido, telefono, correo,
        direccion, estado, fecha_registro, fecha_creacion, fecha_modificacion, accion, ip
    )
    VALUES (
        SYS_GUID(),
        :new.id_adoptante,
        :new.nombre,
        :new.apellido,
        :new.telefono,
        :new.correo,
        :new.direccion,
        :new.estado,
        :new.fecha_registro,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/