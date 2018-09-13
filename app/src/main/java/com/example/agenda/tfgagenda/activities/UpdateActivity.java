package com.example.agenda.tfgagenda.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.helper.InputValidation;
import com.example.agenda.tfgagenda.model.Empty;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.rest.Api;
import com.example.agenda.tfgagenda.sql.BDSQLite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreEvento, ubicacion, fechadesde, horadesde, fechahasta, horahasta, descripcion;

    private String dateInici,datesFinal,horesInici,horesFinal;

    //Servei per les crides a la API
    Api apiService;

    private static final int REQUEST_LOCATION = 1;

    private Button guardar, cancelar, button,addUserButton,bfechaInici,bhoraInici,bfechaFinal,bhoraFinal;

    private  int diaI,mesI,anoI,horaI,minutosI,diaF,mesF,anoF,horaF,minutosF;

    private int REQUEST_FORM = 1;

    private LocationManager locationManager;
    private LocationListener listener;
    private TextView textView;
    private String lattitude="",longitude="",user;
    private ArrayList<String> nomParticipants = new ArrayList<>();
    private TextView mItemSelected;
    private String nomParticipantss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        nombreEvento = (EditText) findViewById(R.id.edtNombreEvento);
        fechadesde = (EditText) findViewById(R.id.edtfechaDesde);
        fechahasta = (EditText) findViewById(R.id.edtFechaHasta);
        horadesde = (EditText) findViewById(R.id.edtHoraInicio);
        horahasta = (EditText) findViewById(R.id.edtHoraHasta);
        descripcion = (EditText) findViewById(R.id.edtDescripcion);
        button = findViewById(R.id.button);
        addUserButton = findViewById(R.id.buttonAddUser);
        textView = (TextView) findViewById(R.id.edtLocation);
        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        bfechaInici=(Button)findViewById(R.id.bfechaInicio);
        bhoraInici=(Button)findViewById(R.id.bhoraInicio);
        bfechaFinal=(Button)findViewById(R.id.bfechaFinal);
        bhoraFinal=(Button)findViewById(R.id.bhoraFinal);


        user = String.valueOf(getIntent().getStringExtra("user")); //Obtenim usuari
        dateInici = String.valueOf(getIntent().getStringExtra("date")); //Obtenim dia Inici
        datesFinal = String.valueOf(getIntent().getStringExtra("datesFinal")); //Obtenim dia Fi
        horesInici = String.valueOf(getIntent().getStringExtra("horesInici")); //Obtenim hora Inici
        horesFinal = String.valueOf(getIntent().getStringExtra("horesFinal")); //Obtenim hora Fi

        fechadesde.setText(dateInici);
        fechahasta.setText(datesFinal);
        guardar = (Button) findViewById(R.id.btnUpdate);
        cancelar = (Button) findViewById(R.id.btnCancelar);
        guardar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        button.setOnClickListener(this);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this,AddParticipantsEventActivity.class);
                intent.putStringArrayListExtra("nomParticipants",nomParticipants);
                startActivityForResult(intent,REQUEST_FORM);
            }
        });

        bfechaInici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                diaI=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                mesI=Calendar.getInstance().get(Calendar.MONTH);
                anoI=Calendar.getInstance().get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fechadesde.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }
                        ,diaI,mesI,anoI);
                datePickerDialog.show();
            }
        });

        bhoraInici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                horaI=c.get(Calendar.HOUR_OF_DAY);
                minutosI=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horadesde.setText(hourOfDay+":"+minute);
                    }
                },horaI,minutosI,false);
                timePickerDialog.show();
            }
        });
        bfechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                diaF=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                mesF=Calendar.getInstance().get(Calendar.MONTH);
                anoF=Calendar.getInstance().get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fechahasta.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }
                        ,diaF,mesF,anoF);
                datePickerDialog.show();
            }
        });

        bhoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                horaF=c.get(Calendar.HOUR_OF_DAY);
                minutosF=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horahasta.setText(hourOfDay+":"+minute);
                    }
                },horaF,minutosF,false);
                timePickerDialog.show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==guardar.getId()){
            if(nombreEvento.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter an event name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(fechadesde.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if(horadesde.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter a start hour", Toast.LENGTH_SHORT).show();
                return;
            }
            if(fechahasta.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter a finish date", Toast.LENGTH_SHORT).show();
                return;
            }
            if(horahasta.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter an end date", Toast.LENGTH_SHORT).show();
                return;
            }
            if(descripcion.getText().toString().equals("")){
                Toast.makeText(this, "You need to enter an event name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(lattitude.equals("")){
                Toast.makeText(this, "You need to set the location", Toast.LENGTH_SHORT).show();
                return;
            }
            if(longitude.equals("")){
                Toast.makeText(this, "You need to set the location", Toast.LENGTH_SHORT).show();
                return;
            }
            String horaFinal, horaInicial,diaInicial,diaFinal;
            diaFinal = fechahasta.getText().toString();
            horaInicial = horadesde.getText().toString();
            horaFinal = horahasta.getText().toString();

            Event nouEvent = new Event();
            nouEvent.setEventDate(fechadesde.getText().toString().trim());
            nouEvent.setOwner(user);
            nouEvent.setLatitud(lattitude);
            nouEvent.setLongitud(longitude);
            nouEvent.setName(nombreEvento.getText().toString());
            nouEvent.setNomParticipantss(nomParticipantss);
            nouEvent.setEventDateFinish(diaFinal);
            nouEvent.setHourEnd(horaFinal);
            nouEvent.setHourStart(horaInicial);
            nouEvent.setId((int) getIntent().getLongExtra("id",0));
            if(mItemSelected.getText().toString().equals("")) nouEvent.setNomParticipantss(user);
            else nouEvent.setNomParticipantss(mItemSelected.getText().toString());

            Long idid= (long) getIntent().getLongExtra("id",0);
            System.out.println("La id es= "+idid);

            apiService = ((RetrofitHTTP) this.getApplication()).getAPI();

            Call<Empty> call = apiService.updateEvent((long) getIntent().getLongExtra("id",0),nouEvent);
            call.enqueue(new Callback<Empty>() {

                @Override
                public void onResponse(Call<Empty> call, Response<Empty> response) {
                    System.out.println("Update event ok");
                    Intent output = getIntent();
                    setResult(RESULT_OK, output);
                    finish();
                }

                @Override
                public void onFailure(Call<Empty> call, Throwable t) {
                    System.out.println("Update event not ok");
                    Intent output = getIntent();
                    setResult(RESULT_OK, output);
                    finish();
                }
            });

            Intent output = getIntent();
            setResult(RESULT_OK, output);
            finish();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode  == RESULT_OK) {
                nomParticipants = data.getStringArrayListExtra("nomParticipants");
                String[] listItems=new String[nomParticipants.size()];;
                String item = "Users added : ";
                nomParticipantss="";
                for (int i = 0; i < nomParticipants.size(); i++) {
                    item = item + nomParticipants.get(i);
                    nomParticipantss= nomParticipantss + nomParticipants.get(i);
                    if (i != nomParticipants.size() - 1) {
                        item = item + ", ";
                        nomParticipantss = nomParticipantss + ", ";
                    }
                }
                mItemSelected.setText(item);
            }
        } catch (Exception ex) {
            Toast.makeText(UpdateActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
