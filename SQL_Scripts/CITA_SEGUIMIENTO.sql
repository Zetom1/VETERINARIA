BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE cita_seguimiento CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE cita_seguimiento_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_cita_seguimiento';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;

CREATE SEQUENCE seq_cita_seguimiento START WITH 1 INCREMENT BY 1;
/

CREATE TABLE cita_seguimiento
(
    id_cita          NUMBER PRIMARY KEY,
    id_mascota       NUMBER,
    fecha_cita       TIMESTAMP,
    motivo           VARCHAR2(250),
    observaciones    VARCHAR2(500),
    estatus          VARCHAR2(50),
    estado           NUMBER DEFAULT 1,
    fecha_registro   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota)
);

COMMENT ON TABLE cita_seguimiento IS 'Tabla entidad de cita de seguimiento';
COMMENT ON COLUMN cita_seguimiento.id_cita IS 'ID number de identificador de tabla cita_seguimiento';
COMMENT ON COLUMN cita_seguimiento.id_mascota IS 'ID de la mascota (llave foránea)';
COMMENT ON COLUMN cita_seguimiento.fecha_cita IS 'Fecha programada para la cita';
COMMENT ON COLUMN cita_seguimiento.motivo IS 'Motivo de la cita de seguimiento';
COMMENT ON COLUMN cita_seguimiento.observaciones IS 'Observaciones adicionales de la cita';
COMMENT ON COLUMN cita_seguimiento.estatus IS 'Estatus de la cita (ej. Pendiente, Completada)';
COMMENT ON COLUMN cita_seguimiento.estado IS 'Estado lógico de la cita';
COMMENT ON COLUMN cita_seguimiento.fecha_registro IS 'Fecha de registro en db';

CREATE TABLE cita_seguimiento_bit
(
    id_cita_bit        VARCHAR2(36) NOT NULL,
    id_cita            NUMBER,
    id_mascota         NUMBER,
    fecha_cita         TIMESTAMP,
    motivo             VARCHAR2(250),
    observaciones      VARCHAR2(500),
    estatus            VARCHAR2(50),
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP,
    fecha_creacion     DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion             VARCHAR2(25),
    ip                 VARCHAR2(30),
    CONSTRAINT pk_id_cita_bit PRIMARY KEY (id_cita_bit)
);

COMMENT ON TABLE cita_seguimiento_bit IS 'Tabla bitácora de citas de seguimiento';
COMMENT ON COLUMN cita_seguimiento_bit.id_cita_bit IS 'ID UUID de identificador de tabla cita_seguimiento_bit';
COMMENT ON COLUMN cita_seguimiento_bit.id_cita IS 'ID de cita para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.id_mascota IS 'ID de la mascota para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.fecha_cita IS 'Fecha de la cita para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.motivo IS 'Motivo para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.observaciones IS 'Observaciones para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.estatus IS 'Estatus para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.estado IS 'Estado lógico de la cita para auditoría';
COMMENT ON COLUMN cita_seguimiento_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN cita_seguimiento_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN cita_seguimiento_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN cita_seguimiento_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_cita_seguim_bit
    AFTER INSERT OR UPDATE ON cita_seguimiento
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

    INSERT INTO cita_seguimiento_bit (
        id_cita_bit, id_cita, id_mascota, fecha_cita, motivo,
        observaciones, estatus, estado, fecha_registro,
        fecha_creacion, fecha_modificacion, accion, ip
    )
    VALUES (
        SYS_GUID(),
        :new.id_cita,
        :new.id_mascota,
        :new.fecha_cita,
        :new.motivo,
        :new.observaciones,
        :new.estatus,
        :new.estado,
        :new.fecha_registro,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/