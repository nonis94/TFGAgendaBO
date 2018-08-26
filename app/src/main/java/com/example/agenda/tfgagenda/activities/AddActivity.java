package com.example.agenda.tfgagenda.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.sql.BDSQLite;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreEvento, ubicacion, fechadesde, horadesde, fechahasta, horahasta, descripcion;

    private static final int REQUEST_LOCATION = 1;

    private Button guardar, cancelar, button;

    private LocationManager locationManager;
    private LocationListener listener;
    private TextView textView;
    String lattitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nombreEvento = (EditText) findViewById(R.id.edtNombreEvento);
        fechadesde = (EditText) findViewById(R.id.edtfechaDesde);
        fechahasta = (EditText) findViewById(R.id.edtFechaHasta);
        horadesde = (EditText) findViewById(R.id.edtHoraInicio);
        horahasta = (EditText) findViewById(R.id.edtHoraHasta);
        descripcion = (EditText) findViewById(R.id.edtDescripcion);
        button = findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.edtLocation);

        String dia, mes, any;
        dia = String.valueOf(getIntent().getStringExtra("dia")); //Obtenim el dia
        mes = String.valueOf(getIntent().getStringExtra("mes")); //Obtenim el mes
        any = String.valueOf(getIntent().getStringExtra("any")); //Obtenim l'any
        System.out.println("A dia hi tenim= " + dia);
        System.out.println("A mes hi tenim= " + mes);
        System.out.println("A any hi tenim= " + any);

        fechadesde.setText(dia + " - " + mes + " - " + any);
        fechahasta.setText(dia + " - " + mes + " - " + any);
        guardar = (Button) findViewById(R.id.btnGuardar);
        cancelar = (Button) findViewById(R.id.btnCancelar);
        guardar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        button.setOnClickListener(this);

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

        } else if(view.getId()==cancelar.getId()) {
            this.finish();
            return;
        }
        else if(view.getId()==button.getId()){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
