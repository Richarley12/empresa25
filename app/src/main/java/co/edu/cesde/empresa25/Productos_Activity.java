package co.edu.cesde.empresa25;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Productos_Activity extends AppCompatActivity  implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText jetreferencia, jetnombre, jetvalor, jetmarca, jetmodelo;
    CheckBox jbcactivo;
    RequestQueue rq,au;
    JsonRequest jrq,aux;
    String ref,nombre,valor,marca,modelo;
    Byte sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        getSupportActionBar().hide();
        jetreferencia=findViewById(R.id.etreferencia);
        jetnombre=findViewById(R.id.etenombre);
        jetvalor=findViewById(R.id.etvalor);
        jetmarca=findViewById(R.id.etmarcas);
        jetmodelo=findViewById(R.id.etmodelo);
        jbcactivo=findViewById(R.id.cbactivo);
        rq= Volley.newRequestQueue(this);
        au=Volley.newRequestQueue(this);
        sw=0;
    }
    public void Consulta(View view){
        ref=jetreferencia.getText().toString();
        if (ref.isEmpty()){
            Toast.makeText(this, "Referencia es requerido para la busqueda", Toast.LENGTH_SHORT).show();
            jetreferencia.requestFocus();
        }
        else{
            String url = "http://172.18.70.0:80/WebService/consultaproducto.php?ref="+ref;
            jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            rq.add(jrq);
            sw=0;
        }

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Producto no registrado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "producto registrado", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);//posicion 0 del arreglo....
            jetnombre.setText(jsonObject.optString("nombre"));
            jetvalor.setText(jsonObject.optString("valor"));
            jetmarca.setText(jsonObject.optString("marca"));
            jetmodelo.setText(jsonObject.optString("modelo"));
            if (jsonObject.optString("activo").equals("si")) {
                jbcactivo.setChecked(true);
            }
            else {
                jbcactivo.setChecked(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Anularp(View view) {
        ref = jetreferencia.getText().toString();
        if (ref.isEmpty()) {
            Toast.makeText(this, "La referencia es requerida", Toast.LENGTH_SHORT).show();
            jetreferencia.requestFocus();
        } else {
            String url = "http://172.18.70.0:80/WebService/anulaProducto.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_Campos();
                            Toast.makeText(getApplicationContext(), "Producto anulado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Producto no anulado!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ref", jetreferencia.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public void Limpiar(View view) {
        Limpiar_Campos();
    }

    private void Limpiar_Campos(){
        jetreferencia.setText("");
        jetnombre.setText("");
        jetvalor.setText("");
        jetmarca.setText("");
        jetmodelo.setText("");
        jbcactivo.setChecked(false);
        jetreferencia.requestFocus();
    }

    public void Regresarp(View view){
        Intent intmain= new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    public void GuardarP (View view){
        ref=jetreferencia.getText().toString();
        nombre=jetnombre.getText().toString();
        valor=jetvalor.getText().toString();
        marca=jetmarca.getText().toString();
        modelo=jetmodelo.getText().toString();

        if (ref.isEmpty()||nombre.isEmpty()||valor.isEmpty()||marca.isEmpty()||modelo.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetreferencia.requestFocus();
        } else {
            String url = "http://172.18.70.0:80/WebService/guardarproducto.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_Campos();
                            Toast.makeText(Productos_Activity.this, "Registro de producto realizado", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Productos_Activity.this, "Registro incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ref",jetreferencia.getText().toString().trim());
                    params.put("nombre",jetnombre.getText().toString().trim());
                    params.put("valor",jetvalor.getText().toString().trim());
                    params.put("marca",jetmarca.getText().toString().trim());
                    params.put("modelo",jetmodelo.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }
    public void Actualizap(View view){
        ref=jetreferencia.getText().toString();
        nombre=jetnombre.getText().toString();
        valor=jetvalor.getText().toString();
        marca=jetmarca.getText().toString();
        modelo=jetmodelo.getText().toString();

        if (ref.isEmpty()||nombre.isEmpty()||valor.isEmpty()||marca.isEmpty()||modelo.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetreferencia.requestFocus();}

        else{
            String url = "http://172.18.70.0:80/WebService/actualizaProducto.php?ref="+ref+"&nombre="+nombre+"&valor="+valor+"&marca="+marca+"&modelo="+modelo+"";
            aux = new JsonObjectRequest(Request.Method.PUT,url, null, null, null);
            au.add(aux);
            Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
            Limpiar_Campos();
        }
    }
}