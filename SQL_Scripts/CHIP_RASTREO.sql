BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE chip_rastreo CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE chip_rastreo_bit CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_chip_rastreo';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora errores si las tablas o secuencia no existen en la primera ejecución
END;

CREATE SEQUENCE seq_chip_rastreo START WITH 1 INCREMENT BY 1;
/

CREATE TABLE chip_rastreo
(
    id_chip            NUMBER PRIMARY KEY,
    codigo_chip        VARCHAR2(100),
    fecha_implantacion TIMESTAMP,
    fabricante         VARCHAR2(150),
    id_mascota         NUMBER,
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chip_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota)
);

COMMENT ON TABLE chip_rastreo IS 'Tabla entidad de chip de rastreo';
COMMENT ON COLUMN chip_rastreo.id_chip IS 'ID number de identificador de tabla chip_rastreo';
COMMENT ON COLUMN chip_rastreo.codigo_chip IS 'Código identificador del chip';
COMMENT ON COLUMN chip_rastreo.fecha_implantacion IS 'Fecha en la que se implantó el chip';
COMMENT ON COLUMN chip_rastreo.fabricante IS 'Fabricante del chip de rastreo';
COMMENT ON COLUMN chip_rastreo.id_mascota IS 'ID de la mascota (llave foránea)';
COMMENT ON COLUMN chip_rastreo.estado IS 'Estado lógico del chip';
COMMENT ON COLUMN chip_rastreo.fecha_registro IS 'Fecha de registro en db';

CREATE TABLE chip_rastreo_bit
(
    id_chip_bit        VARCHAR2(36) NOT NULL,
    id_chip            NUMBER,
    codigo_chip        VARCHAR2(100),
    fecha_implantacion TIMESTAMP,
    fabricante         VARCHAR2(150),
    id_mascota         NUMBER,
    estado             NUMBER DEFAULT 1,
    fecha_registro     TIMESTAMP,
    fecha_creacion     DATE DEFAULT SYSDATE,
    fecha_modificacion DATE,
    accion             VARCHAR2(25),
    ip                 VARCHAR2(30),
    CONSTRAINT pk_id_chip_bit PRIMARY KEY (id_chip_bit)
);

COMMENT ON TABLE chip_rastreo_bit IS 'Tabla bitácora de chips de rastreo';
COMMENT ON COLUMN chip_rastreo_bit.id_chip_bit IS 'ID UUID de identificador de tabla chip_rastreo_bit';
COMMENT ON COLUMN chip_rastreo_bit.id_chip IS 'ID de chip para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.codigo_chip IS 'Código del chip para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.fecha_implantacion IS 'Fecha de implantación para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.fabricante IS 'Fabricante del chip para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.id_mascota IS 'ID de la mascota para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.estado IS 'Estado lógico del chip para auditoría';
COMMENT ON COLUMN chip_rastreo_bit.fecha_creacion IS 'Campo de acción de creación o inserción';
COMMENT ON COLUMN chip_rastreo_bit.fecha_modificacion IS 'Fecha de modificación del registro';
COMMENT ON COLUMN chip_rastreo_bit.accion IS 'Acción de auditoría y/o modificación';
COMMENT ON COLUMN chip_rastreo_bit.ip IS 'Registro de IP del solicitante';

CREATE OR REPLACE TRIGGER trg_chip_rastreo_bit
    AFTER INSERT OR UPDATE ON chip_rastreo
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

    INSERT INTO chip_rastreo_bit (
        id_chip_bit, id_chip, codigo_chip, fecha_implantacion, fabricante,
        id_mascota, estado, fecha_registro,
        fecha_creacion, fecha_modificacion, accion, ip
    )
    VALUES (
        SYS_GUID(),
        :new.id_chip,
        :new.codigo_chip,
        :new.fecha_implantacion,
        :new.fabricante,
        :new.id_mascota,
        :new.estado,
        :new.fecha_registro,
        v_fecha_creacion,
        v_fecha_modificacion,
        v_accion,
        SYS_CONTEXT('USERENV', 'IP_ADDRESS')
    );
END;
/