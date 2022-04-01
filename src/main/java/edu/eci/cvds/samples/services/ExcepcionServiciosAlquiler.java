package edu.eci.cvds.samples.services;

public class ExcepcionServiciosAlquiler extends Exception {
    public static final String NO_CLIENT_REGISTRED = "Este cliente no se encuentra registrado.";
    public static final String NO_ITEM = "Este item no existe.";
    public static final String NO_ITEM_RENTED = "Este item no esta siendo rentado actualmente.";
    public static final String ITEM_RENTED = "El item se encuentra rentado.";
    public static final String INVALID_DAYS = "El numero de dias debe ser mayor a 0";
    public static final String INVALID_RATE = "El valor de la tarifa deberia ser mayor a 0";
    public static final String NO_ITEM_TYPE = "El tipo de item no existe.";
    public static final String INVALID_DATE = "La fecha de entrega debe ser luego de la fecha de inicio.";


    /**
     * Genera una excepcion con el mensaje dado
     * @param message mensaje de la excepción lanzada
     */
    public ExcepcionServiciosAlquiler(String message){
        super(message);
    }

    /**
     * Genera una excepcion con el mensaje dado y una excepcion
     * @param message mensaje de la excepción lanzada
     * @param e excepción
     */
    public ExcepcionServiciosAlquiler(String message, Exception e){
        super(message, e);
    }
}
