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
    public static int company_id = 103;
    // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
            1806, // 1766, // 0 //Se encuentra Abierto el punto?
            1807, // 1767, // 1 //¿Cliente permitió tomar información?
            1808, // 1768, // 2 //¿ Cliente Aceptó Premio ?
            1809, // 1229, // 3	Existe Ventana?
            1810, // 1230, // 4	Cumple Visibilidad?
            1811, // 1231, // 5	Encontro producto?

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

