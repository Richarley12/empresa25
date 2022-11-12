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

public class UsuarioActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText jetusuario, jetnombre,jetclave,jetcorreo;
    CheckBox jcbactivo;
    RequestQueue rq,au;
    JsonRequest jrq,aux;
    String usr, nombre, correo, clave;
    Byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        //Ocultar la barra, asociar objetos Java con objetos Xml
        //Inicializar la cola de consultas
        getSupportActionBar().hide();
        jetusuario= findViewById(R.id.etusuario);
        jetnombre= findViewById(R.id.etnombre);
        jetcorreo= findViewById(R.id.etcorreo);
        jetclave=findViewById(R.id.etclave);
        jcbactivo=findViewById(R.id.cbactivo);
        rq= Volley.newRequestQueue(this);
        au=Volley.newRequestQueue(this);
        sw=0;
    }

    public void Consultar(View view){
        usr= jetusuario.getText().toString();
        if (usr.isEmpty()){
            Toast.makeText(this, "El usuario es requerido para la búsqueda", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else {
            String url = "http://172.18.70.0:80/WebService/consulta.php?usr="+usr;
            jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            rq.add(jrq);
        }
    }

    public void Limpiar (View view){
        Limpiar_campos();
    }

    public void Regresar(View view){
        Intent intmain= new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos (){
        jetnombre.setText("");
        jetusuario.setText("");
        jetcorreo.setText("");
        jetclave.setText("");
        jcbactivo.setChecked(false);
        jetusuario.requestFocus();
    }
    public void Guardar(View view){
        usr=jetusuario.getText().toString();
        nombre=jetnombre.getText().toString();
        correo=jetcorreo.getText().toString();
        clave=jetclave.getText().toString();

        if (usr.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else{
            String url = "http://172.18.70.0:80/WebService/registrocorreo.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_campos();
                            Toast.makeText(getApplicationContext(), "Registro de usuario realizado!", Toast.LENGTH_LONG).show();
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetusuario.getText().toString().trim());
                    params.put("nombre", jetnombre.getText().toString().trim());
                    params.put("correo",jetcorreo.getText().toString().trim());
                    params.put("clave",jetclave.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Usario encontrado", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray= response.optJSONArray("datos");
        JSONObject jsonObject= null;
        try {
            jsonObject= jsonArray.getJSONObject(0);//posición 0 del arreglo...
            jetnombre.setText(jsonObject.optString("nombre"));
            jetclave.setText(jsonObject.optString("clave"));
            jetcorreo.setText(jsonObject.optString("correo"));
            if (jsonObject.optString("activo").equals("si")){
                jcbactivo.setChecked(true);
            }
            else {
                jcbactivo.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Actualizar (View view){
        usr=jetusuario.getText().toString();
        nombre=jetnombre.getText().toString();
        correo=jetcorreo.getText().toString();
        clave=jetclave.getText().toString();
        if (usr.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        /*else {
            String url = "http://192.168.0.14:80/WebService/actualiza.php";
                    StringRequest putRequest= new StringRequest(Request.Method.PUT, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Limpiar_campos();
                                    Toast.makeText(getApplicationContext(), "Usuario actualizado!", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Actualización incorrecta!", Toast.LENGTH_LONG).show();
                        }
                    }
                    ){
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("usr",jetusuario.getText().toString().trim());
                            params.put("nombre", jetnombre.getText().toString().trim());
                            params.put("correo",jetcorreo.getText().toString().trim());
                            params.put("clave",jetclave.getText().toString().trim());
                            return params;
                        }
                    };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(putRequest);
        }*/

        else{
            String url = "http://172.18.70.0:80/WebService/actualiza.php?usr="+usr+"&nombre="+nombre+"&correo="+correo+"&clave="+clave+"";
            aux = new JsonObjectRequest(Request.Method.PUT, url, null, null, null);
            au.add(aux);
            Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }
    }

    public void Eliminar(View view){
        usr=jetusuario.getText().toString();
        if (usr.isEmpty()){
            Toast.makeText(this, "El usuario es requerido", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else{
           String url = "http://172.18.70.0:80/WebService/elimina.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_campos();
                            Toast.makeText(getApplicationContext(), "Registro eliminado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro no eliminado!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetusuario.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public void Anular(View view){
        usr=jetusuario.getText().toString();
        if (usr.isEmpty()){
            Toast.makeText(this, "El usuario es requerido", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else{
            String url = "http://172.18.70.0:80/WebService/anula.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_campos();
                            Toast.makeText(getApplicationContext(), "Registro anulado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro no anulado!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetusuario.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

}