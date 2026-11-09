package com.inube.veterinaria.utils;

public class UtilConstants {

    private UtilConstants() {
    }

    // ---- Consulta ----
    public static final String MSG1 = "Consulta exitosa";

    // ---- Mascota ----
    public static final String MSG2 = "Mascota registrada correctamente";
    public static final String MSG3 = "Mascota actualizada correctamente";
    public static final String MSG4 = "Mascota adoptada correctamente";
    public static final String MSG5 = "Mascota eliminada correctamente";

    // ---- Chip de rastreo ----
    public static final String MSG6 = "Chip de rastreo registrado correctamente";
    public static final String MSG7 = "Chip de rastreo actualizado correctamente";
    public static final String MSG11 = "Chip de rastreo eliminado correctamente";

    // ---- Adoptante ----
    public static final String MSG8 = "Adoptante registrado correctamente";
    public static final String MSG9 = "Adoptante actualizado correctamente";
    public static final String MSG10 = "Adoptante eliminado correctamente";

    // ---- Cita de seguimiento ----
    public static final String MSG12 = "Cita de seguimiento registrada correctamente";
    public static final String MSG13 = "Cita de seguimiento actualizada correctamente";
    public static final String MSG14 = "Cita de seguimiento eliminada correctamente";
    public static final String MSG19 = "Cita de seguimiento completada correctamente";
    public static final String MSG20 = "Cita de seguimiento cancelada correctamente";

    // ---- No encontrado ----
    public static final String MSG15 = "Adoptante no encontrado";
    public static final String MSG16 = "Mascota no encontrada";
    public static final String MSG17 = "Chip de rastreo no encontrado";
    public static final String MSG18 = "Cita de seguimiento no encontrada";

    // ---- Validaciones de negocio ----
    public static final String MSG21 = "La mascota ya tiene un chip de rastreo registrado";
    public static final String MSG22 = "El código de chip ya está registrado";
    public static final String MSG23 = "La mascota ya fue adoptada";
    public static final String MSG24 = "La fecha de la cita debe ser una fecha futura";
    public static final String MSG25 = "Solo se pueden modificar citas programadas";
    public static final String MSG26 = "Solo se pueden completar citas programadas";
    public static final String MSG27 = "Solo se pueden cancelar citas programadas";

    // ---- Vacuna ----
    public static final String MSG28 = "Vacuna registrada correctamente";
    public static final String MSG29 = "Vacuna actualizada correctamente";
    public static final String MSG30 = "Vacuna eliminada correctamente";
    public static final String MSG31 = "Vacuna no encontrada";

    // ---- Historial de vacunación ----
    public static final String MSG32 = "Vacunación registrada correctamente";
    public static final String MSG33 = "Vacunación actualizada correctamente";
    public static final String MSG34 = "Vacunación eliminada correctamente";
    public static final String MSG35 = "Registro de vacunación no encontrado";
    public static final String MSG36 = "La fecha de aplicación es obligatoria";
    public static final String MSG37 = "La próxima aplicación debe ser posterior a la fecha de aplicación";
    public static final String MSG38 = "La mascota no tiene un adoptante activo";

    // ---- Estatus de la cita (columna "estatus") ----
    public static final String ESTATUS_PROGRAMADA = "PROGRAMADA";
    public static final String ESTATUS_COMPLETADA = "COMPLETADA";
    public static final String ESTATUS_CANCELADA = "CANCELADA";

    // ---- Estado lógico (columna "estado") ----
    public static final Integer CODENEG = 0;
    public static final Integer CODEPOS = 1;
}