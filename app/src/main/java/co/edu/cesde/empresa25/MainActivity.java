package co.edu.cesde.empresa25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Ocultar la barra de menu por defecto

        getSupportActionBar().hide();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.Escenario,new SesionFragment()).commit();


    }


}