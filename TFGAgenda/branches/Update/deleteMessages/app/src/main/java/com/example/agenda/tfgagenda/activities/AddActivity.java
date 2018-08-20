package com.example.agenda.tfgagenda.activities;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.sql.BDSQLite;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreEvento, ubicacion, fechadesde, horadesde, fechahasta, horahasta, descripcion;

    private Button guardar, cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nombreEvento =(EditText) findViewById(R.id.edtNombreEvento);
        ubicacion = (EditText) findViewById(R.id.edtUbicacion);
        fechadesde =(EditText) findViewById(R.id.edtfechaDesde);
        fechahasta = (EditText) findViewById(R.id.edtFechaHasta);
        horadesde = (EditText) findViewById(R.id.edtHoraInicio);
        horahasta = (EditText) findViewById(R.id.edtHoraHasta);
        descripcion = (EditText) findViewById(R.id.edtDescripcion);

        Bundle bundle =getIntent().getExtras();
        int dia=0, mes=0, any=0;
        bundle.getInt("dia");
        bundle.getInt("mes");
        bundle.getInt("any");
        fechadesde.setText(dia+" - "+ mes + " - " + any);
        fechahasta.setText(dia+" - "+ mes + " - " + any);
        guardar = (Button) findViewById(R.id.btnGuardar);
        cancelar = (Button) findViewById(R.id.btnCancelar);
        guardar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==guardar.getId()){
            //Guardem les dades entrades per l'usuari
            BDSQLite bd = new BDSQLite(getApplication(),"Agenda",null,1);
            SQLiteDatabase db = bd.getWritableDatabase();

            String sql = "insert into eventos" +
                    "(nombreEvento, ubicacion, fechadesde, horadesde, fechahasta, horahasta," +
                    "descripcion) values('" +
                    nombreEvento.getText()+
                    "','" + ubicacion.getText() +
                    "','" + fechadesde.getText() +
                    "','" + horadesde.getText() +
                    "','" + fechahasta.getText() +
                    "','" + horahasta.getText() +
                    "','" + descripcion.getText() +
                    "')";
            try{
                db.execSQL(sql);

                nombreEvento.setText("");
                ubicacion.setText("");
                fechahasta.setText("");
                fechadesde.setText("");
                horahasta.setText("");
                horadesde.setText("");
                descripcion.setText("");
            }
            catch(Exception e ){
                Toast.makeText(getApplication(),"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
            this.finish();

        } else {
            this.finish();
            return;
        }


    }
}
