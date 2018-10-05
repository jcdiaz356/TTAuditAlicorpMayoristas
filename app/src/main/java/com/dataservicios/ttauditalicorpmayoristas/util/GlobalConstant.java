package com.dataservicios.ttauditalicorpmayoristas.util;
/**
 * Created by usuario on 11/11/2014.
 */

public final class GlobalConstant {
    public static String dominio = "http://ttaudit.com";
//    public static String dominio = "http://192.168.1.73";
    public static final String LOGIN_URL = dominio + "/loginUser";
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
   // public static int company_id = 106; // Alicorp Helena
    public static int company_id = 212; //205; //191; //182; //159; // 151; // Triada
//    public static int company_id = 138; // Triada
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
           3516, //3412, // 3118, //3022,  // 0 //Se encuentra Abierto el punto?
           3517, //3413, // 3119, //3023,  // 1 //¿Cliente permitió tomar información?
           3518, //3414, // 3120, //3024,  // 2 //¿ Cliente Aceptó Premio ?
           3519, //3415, // 3121, //3025,  // 3	Existe Ventana?
           3520, //3416, // 3122, //3026,  // 4	Cumple Visibilidad?
           3521, //3417, // 3123, //3027,  // 5	Encontro producto?

            //2347, // 2244,  //2122, // 2006, // 1836,//1806, // 1766, // 0 //Se encuentra Abierto el punto?
            //2348, // 2245,  //2123, // 2007, // 1837,//1807, // 1767, // 1 //¿Cliente permitió tomar información?
            //2349, // 2246,  //2124, // 2008, // 1838,//1808, // 1768, // 2 //¿ Cliente Aceptó Premio ?
            //2350, // 2247,  //2125, // 2009, // 0,   //1809, // 1229, // 3	Existe Ventana?
            //2351, // 2248,  //2126, // 2010, // 0,   //1810, // 1230, // 4	Cumple Visibilidad?
            //2352, // 2249,  //2127, // 2011, // 0,   //1811, // 1231, // 5	Encontro producto?
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

