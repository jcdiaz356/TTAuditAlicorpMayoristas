package com.dataservicios.ttauditalicorpmayoristas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorpmayoristas.AditoriaAlicorp.StoreOpenClose;
import com.dataservicios.ttauditalicorpmayoristas.Model.Categoria;
import com.dataservicios.ttauditalicorpmayoristas.Model.Pdv;
import com.dataservicios.ttauditalicorpmayoristas.Model.Product;
import com.dataservicios.ttauditalicorpmayoristas.Model.Publicity;
import com.dataservicios.ttauditalicorpmayoristas.SQLite.DatabaseHelper;
import com.dataservicios.ttauditalicorpmayoristas.adapter.PdvsAdapter;
import com.dataservicios.ttauditalicorpmayoristas.util.AuditAlicorp;
import com.dataservicios.ttauditalicorpmayoristas.util.GPSTracker;
import com.dataservicios.ttauditalicorpmayoristas.util.GlobalConstant;
import com.dataservicios.ttauditalicorpmayoristas.util.JSONParserX;
import com.dataservicios.ttauditalicorpmayoristas.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by usuario on 06/01/2015.
 */
public class PuntosVenta extends Activity {
    private static final String LOG_TAG = PuntosVenta.class.getSimpleName();
    private static final String TAG = PuntosVenta.class.getSimpleName();
    private Activity MyActivity = this ;
    EditText pdvs1,pdvsAuditados1,porcentajeAvance1;
    private TextView tvPDVSdelDía;
    // Movies json url
    private ProgressDialog pDialog;
    private List<Pdv> pdvList = new ArrayList<Pdv>();
    private ListView listView;
    private PdvsAdapter adapter;
    private int IdRuta ;
    private String fechaRuta;
   // private Button bt_MapaRuta;
    private DatabaseHelper db;
   // Activity MyActivity ;
    private JSONObject params;
    private SessionManager session;
    private String email_user, id_user, name_user, region , typeBodega,type ,typeBodega_id;
    private int store_id,level;
    private Button bt_MapaRuta , btMapaRutasAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntos_venta);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("PDVs");
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        pdvs1 = (EditText) findViewById(R.id.etPDVS);
        pdvsAuditados1 = (EditText) findViewById(R.id.etPDVSAuditados);
        porcentajeAvance1 = (EditText) findViewById(R.id.etPorcentajeAvance);
        tvPDVSdelDía = (TextView) findViewById(R.id.tvPDVSdelDia);

        Bundle bundle = getIntent().getExtras();
        IdRuta = bundle.getInt("idRuta");
        level = bundle.getInt("level");
        fechaRuta = bundle.getString("fechaRuta");
        tvPDVSdelDía.setText(fechaRuta);


        session = new SessionManager(MyActivity);
        HashMap<String, String> user = session.getUserDetails();
        name_user = user.get(SessionManager.KEY_NAME);
        email_user = user.get(SessionManager.KEY_EMAIL);
        id_user = user.get(SessionManager.KEY_ID_USER);

        db = new DatabaseHelper(getApplicationContext());

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        //Añadiendo parametros para pasar al Json por metodo POST
        params = new JSONObject();
        try {
            params.put("id", IdRuta);
            params.put("company_id", GlobalConstant.company_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }





        bt_MapaRuta = (Button) findViewById(R.id.btMapaRuta);
        btMapaRutasAll = (Button) findViewById(R.id.btMapaRutaAll);
        if (level == 1) {

            bt_MapaRuta.setVisibility(View.VISIBLE);
            btMapaRutasAll.setVisibility(View.VISIBLE);
        } else if (level == 0) {

            bt_MapaRuta.setVisibility(View.INVISIBLE);
            btMapaRutasAll.setVisibility(View.INVISIBLE);
        }
        bt_MapaRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle argRuta = new Bundle();
                argRuta.putInt("id", IdRuta);
                //Intent intent = new Intent("com.dataservicios.ttauditcolgate.MAPARUTAS");
                Intent intent = new Intent(MyActivity,MapaRuta.class);
                intent.putExtras(argRuta);
                startActivity(intent);
            }
        });

        btMapaRutasAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle argRuta = new Bundle();
                argRuta.putInt("id", IdRuta);

//
//                Intent intent = new Intent(MyActivity, MapaRuta.class);
//                intent.putExtras(argRuta);
//                startActivity(intent);
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.dataservicios.ttauditrutas", "com.dataservicios.ttauditrutas.MapaRuta"));
                    intent.putExtras(argRuta);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(MyActivity,"No se encuentra instalada la aplicación", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.dataservicios.ttauditrutas"));
                    startActivity(intent);
                }finally {

                }
            }
        });

        listView = (ListView) findViewById(R.id.list);
        adapter = new PdvsAdapter(this, pdvList,IdRuta);

        listView.setAdapter(adapter);
        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Obteniendo fecha y hora
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                GlobalConstant.inicio = strDate;
                Log.i("FECHA", strDate);

                //Obteniendo Ubicacion
                GPSTracker gps = new GPSTracker(MyActivity);

                // Verificar si GPS esta habilitado
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    GlobalConstant.latitude_open = latitude;
                    GlobalConstant.longitude_open = longitude;
                    //Toast toast = Toast.makeText(getApplicationContext(), "Lat: " + String.valueOf(latitude) + "Long: " + String.valueOf(longitude), Toast.LENGTH_SHORT);
                    //toast.show();
                } else {
                    // Indicar al Usuario que Habilite su GPS
                    gps.showSettingsAlert();
                }

                // selected item
                String selected = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                store_id = Integer.valueOf(selected);
                type = ((TextView) view.findViewById(R.id.tvType)).getText().toString();
                region = ((TextView) view.findViewById(R.id.tvRegion)).getText().toString();
                typeBodega = ((TextView) view.findViewById(R.id.tvTypeBodega)).getText().toString();
                type = ((TextView) view.findViewById(R.id.tvType)).getText().toString();

//                if (typeBodega.equals("Mini Market")) typeBodega_id = "1";
//                if (typeBodega.equals("Bodega Clásica")) typeBodega_id = "2";
//                if (typeBodega.equals("Bodega Alto Tráfico")) typeBodega_id = "3";

               // typeBodega_id = "4";
                db.deleteAllPublicity();
                db.deleteAllProducts();
                db.deleteAllCategorias();
                db.deleteAllSODVentanas();
                db.deleteAllPresensePublicity();

                new loadPoll().execute();

            }
        });
        listView.setAdapter(adapter);

        new loadStores().execute();


    }


        class loadPoll extends AsyncTask<Void, Integer, Boolean> {
            /**
             * Antes de comenzar en el hilo determinado, Mostrar progresión
             */
            boolean failure = false;
            @Override
            protected void onPreExecute() {
                //tvCargando.setText("Cargando Product...");
                pDialog = new ProgressDialog(MyActivity);
                pDialog.setMessage("Cargando...");
                pDialog.setCancelable(false);
                pDialog.show();
                super.onPreExecute();
            }
            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO Auto-generated method stub
                //cargaTipoPedido();
                // readJsonProducts();
                if (level == 0 ) {

                    if (!readJsonPublicity()) return false;
                    if (!readJsonProducts()) return false;
                    createCategorias();
                }

                //AuditAlicorp.getLisStores(IdRuta,GlobalConstant.company_id);
                return true;
            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute(Boolean result) {
                // dismiss the dialog once product deleted
                hidepDialog();
                if (result) {
                    // loadLoginActivity();
                    Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(store_id), Toast.LENGTH_SHORT);
                    toast.show();
                    Bundle argPDV = new Bundle();
                    argPDV.putInt("idPDV", Integer.valueOf(store_id));
                    argPDV.putInt("idRuta", Integer.valueOf(IdRuta));
                    argPDV.putString("fechaRuta", fechaRuta);
                    argPDV.putString("region", region);
                    argPDV.putString("typeBodega", typeBodega);
                    //argPDV.putInt("level", level);

                    //Intent intent = new Intent("com.dataservicios.ttauditalicorpmayoristas.DETALLEPDV");
                    //Intent intent = new Intent(MyActivity, TipoDex.class);
//                    if (type.equals("Mercado")) {
//                        Intent intent = new Intent(MyActivity, MercadoDetalle.class);
//                        intent.putExtras(argPDV);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(MyActivity, StoreOpenClose.class);
//                        intent.putExtras(argPDV);
//                        startActivity(intent);
//                    }
                    if (level == 1) {
                        Intent intent = new Intent(MyActivity, MercadoDetalle.class);
                        intent.putExtras(argPDV);
                        startActivity(intent);
                        finish();
                    } else if (level == 0) {
                        Intent intent = new Intent(MyActivity, StoreOpenClose.class);
                        intent.putExtras(argPDV);
                        startActivity(intent);
                        //finish();
                    }
                }
            }
        }




    class loadStores extends AsyncTask<Void, Integer, ArrayList<Pdv>> {
            /**
             * Antes de comenzar en el hilo determinado, Mostrar progresión
             */
            boolean failure = false;

            @Override
            protected void onPreExecute() {
                //tvCargando.setText("Cargando Product...");

                pDialog = new ProgressDialog(MyActivity);
                pDialog.setMessage("Cargando...");
                pDialog.setCancelable(false);
                pDialog.show();
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Pdv> doInBackground(Void... params) {
                // TODO Auto-generated method stub

                ArrayList<Pdv> listPdv = new ArrayList<Pdv>();
                listPdv = AuditAlicorp.getLisStores(IdRuta, GlobalConstant.company_id,level);
                return listPdv;
            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute(ArrayList<Pdv> pdvs) {
                // dismiss the dialog once product deleted
                hidepDialog();

                if (pdvs.isEmpty()) {
//                Toast.makeText(TimelineActivity.this, getResources().getString(R.string.label_tweets_not_found),
//                        Toast.LENGTH_SHORT).show();

                    //pdvList.addAll(pdvs);
                } else {
//                updateListView(tweets);
//                Toast.makeText(TimelineActivity.this, getResources().getString(R.string.label_tweets_downloaded),
//                        Toast.LENGTH_SHORT).show();


                    float contadorStore = pdvs.size();
                    float auditadosStore = 0;


                    for (int i = 0; i < pdvs.size(); i++) {

                        if(pdvs.get(i).getStatus() == 1) {
                            auditadosStore ++;
                        }
                    }


                    pdvs1.setText(String.valueOf(contadorStore)) ;
                    pdvsAuditados1.setText(String.valueOf(auditadosStore));

                    float porcentajeAvance=(auditadosStore / contadorStore) *100;
                    BigDecimal big = new BigDecimal(porcentajeAvance);
                    big = big.setScale(2, RoundingMode.HALF_UP);
                    porcentajeAvance1.setText( String.valueOf(big) + " % ");

                    pdvList.addAll(pdvs);
                    adapter.notifyDataSetChanged();
                }

            }
        }

            private boolean readJsonPublicity() {
                int success;
                try {

                    HashMap<String, String> params = new HashMap<>();

                    params.put("company_id", String.valueOf(GlobalConstant.company_id));
                    params.put("tipo_bodega", String.valueOf("4"));


                    JSONParserX jsonParser = new JSONParserX();
                    // getting product details by making HTTP request
                    JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonListPublicitiesAlicorp", "POST", params);
                    // check your log for json response
                    Log.d("Login attempt", json.toString());
                    // json success, tag que retorna el json
                    //
                    if (json == null) {
                        return false;
                    } else {

                        success = json.getInt("success");
                        if (success == 1) {
                            JSONArray ObjJson;
                            ObjJson = json.getJSONArray("publicities");

                            if (ObjJson.length() > 0) {
                                for (int i = 0; i < ObjJson.length(); i++) {
                                    try {
                                        JSONObject obj = ObjJson.getJSONObject(i);
                                        Publicity publicity = new Publicity();
                                        publicity.setId(Integer.valueOf(obj.getString("id")));
                                        publicity.setName(obj.getString("fullname"));
                                        publicity.setActive(1);
                                        publicity.setCategory_id(Integer.valueOf(obj.getString("category_id")));
                                        publicity.setCategory_name(obj.getString("categoria"));
                                        publicity.setImage(obj.getString("imagen"));
                                        publicity.setCompany_id(Integer.valueOf(obj.getString("company_id")));
                                        db.createPublicity(publicity);
                                        //pedido.setDescripcion(obj.getString("descripcion"));
                                        // adding movie to movies array
                                        // tipoPedidoList.add(pedido);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //poblandoSpinnerTipoPedido();
                                Log.d(LOG_TAG, String.valueOf(db.getAllPublicity()));
                            }
                        } else {
                            Log.d(LOG_TAG, "No hay datos de Gancheras");
                            // return json.getString("message");
                            //return false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (Exception e) {
                    return false;
                }

                return true;

            }


    private boolean readJsonProducts() {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("company_id", String.valueOf(GlobalConstant.company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonListProductsCompany" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json

            if (json == null) {
                return false;
            } else {

                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("products");

                    if(ObjJson.length() > 0) {
                        for (int i = 0; i < ObjJson.length(); i++) {
                            try {
                                JSONObject obj = ObjJson.getJSONObject(i);
                                Product product = new Product();
                                product.setId(Integer.valueOf(obj.getString("id")));
                                product.setName(obj.getString("fullname"));
                                product.setCode(obj.getString("eam"));
                                product.setStatus(0);
                                product.setActive(1);
                                product.setCategory_id(Integer.valueOf(obj.getString("category_id")));
                                product.setCategory_name(obj.getString("categoria"));
                                product.setImage(GlobalConstant.dominio + "/media/images/colgate/products/" + obj.getString("imagen"));
                                product.setPrecio(obj.getString("precio"));
                                //product.setCompany_id(Integer.valueOf(obj.getString("company_id")));
                                product.setCompany_id(Integer.valueOf(GlobalConstant.company_id));
                                db.createProduct(product);
                                //pedido.setDescripcion(obj.getString("descripcion"));
                                // adding movie to movies array
                                // tipoPedidoList.add(pedido);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //poblandoSpinnerTipoPedido();
                        Log.d(LOG_TAG, String.valueOf(db.getAllProducts()));
                    }
                }else{
                    Log.d(LOG_TAG, json.getString("message"));
                    // return json.getString("message");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            return false;
        }


        return true;
    }


    private void createCategorias() {
                // 59 QUITAMANCHAS
                // 60 SUAVIZANTES
                // 61 DETERGENTES

        Categoria categoria = new Categoria();
        categoria.setId(Integer.valueOf(59));
        categoria.setNombre("QUITAMANCHAS");
        categoria.setStatus(0);
        categoria.setActive(1);
        db.createCategoria(categoria);


        categoria.setId(Integer.valueOf(60));
        categoria.setNombre("SUAVIZANTES");
        categoria.setStatus(0);
        categoria.setActive(1);
        db.createCategoria(categoria);


        categoria.setId(Integer.valueOf(61));
        categoria.setNombre("DETERGENTES");
        categoria.setStatus(0);
        categoria.setActive(1);
        db.createCategoria(categoria);
    }

    @Override
            public void onDestroy() {
                super.onDestroy();
                hidepDialog();
            }


            @Override
            public void onBackPressed() {
                super.onBackPressed();
                this.finish();
//        Intent a = new Intent(this,PanelAdmin.class);
//        //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(a);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                        onBackPressed();
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
                //return super.onOptionsItemSelected(item);
            }


            private void showpDialog() {
                if (!pDialog.isShowing())
                    pDialog.show();
            }

            private void hidepDialog() {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResume() {
                super.onResume();


            }

            @Override
            public void onRestart() {
                super.onRestart();
                //When BACK BUTTON is pressed, the activity on the stack is restarted
                //Do what you want on the refresh procedure here
                finish();
                startActivity(getIntent());
            }
        }

