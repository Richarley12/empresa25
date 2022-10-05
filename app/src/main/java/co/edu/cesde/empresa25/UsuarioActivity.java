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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText jetusuario, jetnombre,jetclave,jetcorreo;
    CheckBox jcbactivo;
    RequestQueue rq;
    JsonRequest jrq;
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
        sw=0;
    }

    public void Consultar(View view){
        usr= jetusuario.getText().toString();
        if (usr.isEmpty()){
            Toast.makeText(this, "El usuario es requerido para la búsqueda", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        }
        else {
            String url = "http://172.16.62.46:80/WebService/consulta.php?usr="+usr;
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


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Uusario encontrado", Toast.LENGTH_SHORT).show();
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
}