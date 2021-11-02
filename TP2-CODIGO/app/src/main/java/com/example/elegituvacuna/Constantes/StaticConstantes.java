package com.example.elegituvacuna.Constantes;

public final class StaticConstantes {

    public final static String COLOR_CON_LUZ = "#d9411d";
    public final static String COLOR_POCA_LUZ = "#8657c5";
    public final static Integer VALOR_LUZ_CAMBIO_FONDO = 1;


    // Para realizar Registro
    public final static String URL_REGISTRAR = "http://so-unlam.net.ar/api/api/register";

    public final static String ENTORNO = "env";
    public final static String ENTORNO_TEST = "TEST";
    public final static String ENTORNO_PROD = "PROD";
    public final static String NOMBRE = "name";
    public final static String APELLIDO = "lastname";

    public final static String DNI = "dni";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String COMISION = "commission";
    public final static String GRUPO = "group";
////////////////////////////////////////////////////////////////
    public final static String COMISION_OTRA_2900 = "2900";
    public final static String COMISION_OTRA_3900 = "3900";
    public final static Integer CANTIDAD_MINIMA_PASSWORD = 8;
    public final static Integer ESTADO_INICIAL_CONTEO = 0;

    public final static String MSJ_PASSWORD_CORTO ="La password debe tener al menos 8 caracteres";
    public final static String MSJ_COMISION_INVALIDA ="Por favor ingrese una comision valida";
    public final static String MSJ_REGISTRO_CORRECTO ="Registro correcto";
    public final static String MSJ_REGISTRO_INCORRECTO ="Error de registro";
    public final static String REQUEST_POST="POST";
    //////////////////////////////////////////////// asyntask
    public final static int TIMEOUT_SOLICITUD = 5000;


    ////////////////////////////////////////////////
    //Para Centros Presenter
    ////////////////////////////////////////////////
    public final static String CAPITAL_FEDERAL = "Capital Federal";
    public final static String PROVINCIA_BUENOS_AIRES = "Buenos Aires";
    public final static String PALABRA_CLAVE_VACUNA = "Vacuna";
    public final static String PALABRA_CLAVE_PARTIDO = "Partido";
    public final static String PALABRA_CLAVE_PROVINCIA = "Provincia";



    public final static String URL_LOGIN = "http://so-unlam.net.ar/api/api/login";
    public final static String MSJ_LOGIN_CORRECTO ="Login correcto";
    public final static String MSJ_LOGIN_INCORRECTO ="Error de logueo";
    public final static String PATTERN = "^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
    public final static String CAMPOS_VACIOS="Los campos no pueden estar vacios";
    public final static String MAIL_INVALIDO="Debe introducir un mail valido";




    public final static String REQUEST_PUT="PUT";
    public final static String URL_EVENTOS="http://so-unlam.net.ar/api/api/event";
    public final static String URL_ACTUALIZAR_TOKEN="http://so-unlam.net.ar/api/api/refresh";
    public final static String MSJ_TOKEN_ACTUALIZADO="actualizo el token";
    public final static String MSJ_ERROR_TOKEN="Error al actualizar el token";

    //14 minutos = 1000 mili * 60s * 14m = 840.000 mili
    public final static long VENCIMIENTO_TOKEN = 840000;


    public final static String ACTUALIZAR_TOKEN_Y_REGISTRAR_EVENTO="actualizar";
    public final static String VOLVER_A_LOGUEARSE="Logueo";
    public final static String MSJ_NO_INTERNET="No hay conexion a internet";

}
