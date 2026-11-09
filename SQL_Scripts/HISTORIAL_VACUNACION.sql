BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE historial_vacunacion CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE historial_vacunacion_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_historial_vacunacion';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;
/

CREATE SEQUENCE seq_historial_vacunacion START WITH 1 INCREMENT BY 1;
/

CREATE TABLE historial_vacunacion
(
    id_historial       NUMBER PRIMARY KEY,
    id_mascota         NUMBER NOT NULL,
    id_vacuna          NUMBER NOT NULL,
    fecha_aplicacion   TIMESTAMP,
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_historial_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
    CONSTRAINT fk_historial_vacuna FOREIGN KEY (id_vacuna) REFERENCES vacuna(id_vacuna)
);

COMMENT ON TABLE historial_vacunacion IS 'Registro de aplicación de vacunas a las mascotas';
COMMENT ON COLUMN historial_vacunacion.id_historial IS 'ID principal del evento de vacunación';
COMMENT ON COLUMN historial_vacunacion.id_mascota IS 'Llave foránea hacia la tabla mascota';
COMMENT ON COLUMN historial_vacunacion.id_vacuna IS 'Llave foránea hacia el catálogo de vacunas';
COMMENT ON COLUMN historial_vacunacion.fecha_aplicacion IS 'Fecha en la que se aplicó la vacuna a la mascota';
COMMENT ON COLUMN historial_vacunacion.estado IS 'Estado lógico del registro';
COMMENT ON COLUMN historial_vacunacion.fecha_registro IS 'Fecha de registro en la db';

CREATE TABLE historial_vacunacion_bit
(
    id_historial_bit   VARCHAR2(36) NOT NULL,
    id_historial       NUMBER,
    id_mascota         NUMBER,
    id_vacuna          NUMBER,
    fecha_aplicacion   TIMESTAMP,
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP,
    fecha_creacion     DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion             VARCHAR2(25),
    ip                 VARCHAR2(30),
    CONSTRAINT pk_id_historial_bit PRIMARY KEY (id_historial_bit)
);

COMMENT ON TABLE historial_vacunacion_bit IS 'Tabla bitácora del historial de vacunación';
COMMENT ON COLUMN historial_vacunacion_bit.id_historial_bit IS 'ID UUID de identificador de tabla historial_vacunacion_bit';
COMMENT ON COLUMN historial_vacunacion_bit.id_historial IS 'ID del historial para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.id_mascota IS 'ID de la mascota para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.id_vacuna IS 'ID de la vacuna para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.fecha_aplicacion IS 'Fecha de aplicación para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.estado IS 'Estado lógico para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.fecha_registro IS 'Fecha de registro para auditoría';
COMMENT ON COLUMN historial_vacunacion_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN historial_vacunacion_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN historial_vacunacion_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN historial_vacunacion_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_hist_vacunacion_bit
    AFTER INSERT OR UPDATE ON historial_vacunacion
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

    INSERT INTO historial_vacunacion_bit (
        id_historial_bit, id_historial, id_mascota, id_vacuna,
        fecha_aplicacion, estado, fecha_registro,
        fecha_creacion, fecha_modificacion, accion, ip
    ) VALUES (
        SYS_GUID(),
        :new.id_historial,
        :new.id_mascota,
        :new.id_vacuna,
        :new.fecha_aplicacion,
        :new.estado,
        :new.fecha_registro,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/