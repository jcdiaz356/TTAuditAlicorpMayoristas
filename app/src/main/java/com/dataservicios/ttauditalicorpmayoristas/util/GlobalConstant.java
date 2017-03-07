package com.dataservicios.ttauditalicorpmayoristas.util;
/**
 * Created by usuario on 11/11/2014.
 */

public final class GlobalConstant {
    public static String dominio = "http://ttaudit.com";
   // public static String dominio = "http://appfiliaibk.com";
    public static final String LOGIN_URL = dominio + "/loginUser";
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 66;
    // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
        951, // 0	Existe Ventana?
        952, // 1	Cumple Visibilidad?
        953, // 2	Encontro producto?
        954, // 3	Cliente cumplio cuota?
        955, // 4	Cliente acepto dar facturas?
        956, // 5	¿ Cliente Aceptó firmar cargo de factura ?
        957, // 6	Se encuentra Abierto el punto?
        958, // 7	¿Cliente permitió tomar información?
        959, // 8	¿ Cliente Aceptó Premio ?

} ;

    public static int[] audit_id = new int[]{
            39,	// 0 "Visibilidad Alicorp Mayoristas"
            40,	// 1 "Cumple MSL Alicorp Mayorista"
            41,	// 2 "Cumple Montos Alicorp Mayorista"
            42,	// 3 "Preguntas iniciales Alicorp Mayorista"
    } ;

    public static final String JPEG_FILE_PREFIX = "_Alicorp_M_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}

