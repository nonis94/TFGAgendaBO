package com.example.agenda.tfgagenda.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.model.Empty;
import com.example.agenda.tfgagenda.rest.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 1/2/2018.
 */

public class GalleryActivity extends AppCompatActivity {

    //Servei per les crides a la API
    Api apiService;

    private static final String TAG = "GalleryActivity";
    private GoogleMap googleMap;
    private Button locationButton,removeButton,updateButton;
    private String imageUrl;
    private String imageName;
    private String date;
    private String latitud;
    private String longitud;
    private String nomParticipantss;
    private String datesFinal;
    private String horesInici;
    private String horesFinal;
    private Long id;
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);
        apiService = ((RetrofitHTTP) this.getApplication()).getAPI();
        locationButton = (Button) findViewById(R.id.button_location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "geo:"+latitud+","+longitud+"?z=10";
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        removeButton = (Button) findViewById(R.id.button_remove_event);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Empty> call = (Call<Empty>) apiService.deleteEvent(id);
                call.enqueue(new Callback<Empty>() {
                    @Override
                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                        Intent output = getIntent();
                        setResult(RESULT_OK, output);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Empty> call, Throwable t) {
                        Intent output = getIntent();
                        setResult(RESULT_OK, output);
                        finish();
                    }
                });
            }
        });

        updateButton = (Button) findViewById(R.id.button_update_event);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hem fet click!");
                Intent intent = new Intent(GalleryActivity.this,UpdateActivity.class);
                intent.putExtra("user",String.valueOf(user));
                intent.putExtra("date",getIntent().getStringExtra("datesInici"));
                intent.putExtra("datesFinal",getIntent().getStringExtra("datesFinal"));
                intent.putExtra("horesInici",getIntent().getStringExtra("horesInici"));
                intent.putExtra("horesFinal",getIntent().getStringExtra("horesFinal"));
                intent.putExtra("id",getIntent().getLongExtra("id",0));
                //startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            imageUrl = getIntent().getStringExtra("image_url");
            imageName = getIntent().getStringExtra("image_name");
            date = "Start: "+getIntent().getStringExtra("datesInici");
            latitud = getIntent().getStringExtra("latitud");
            longitud = getIntent().getStringExtra("longitud");
            nomParticipantss =getIntent().getStringExtra("nomParticipantss");
            datesFinal = "End: "+getIntent().getStringExtra("datesFinal");
            horesInici ="Initial hour: "+getIntent().getStringExtra("horesInici");
            horesFinal ="End hour: "+getIntent().getStringExtra("horesFinal");
            id = getIntent().getLongExtra("id",0);
            user = getIntent().getStringExtra("user");
            setImage(imageUrl, imageName,date,nomParticipantss,datesFinal,horesInici,horesFinal);
        }
    }


    private void setImage(String imageUrl, String imageName,String date,String nomParticipantss,String datesFinal,String horesInici,String horesFinal){
        Log.d(TAG, "setImage: setting te image and name to widgets.");
        String finalParticipants = "List of users : "+nomParticipantss;
        TextView name = findViewById(R.id.image_description);
        name.setText(imageName);
        TextView dateInici = findViewById(R.id.iniciDate);
        dateInici.setText(date);
        TextView horaInici = findViewById(R.id.horaInici);
        horaInici.setText(horesInici);
        TextView dataFinal = findViewById(R.id.dataFi);
        dataFinal.setText(datesFinal);
        TextView horaFi = findViewById(R.id.horaFi);
        horaFi.setText(horesFinal);
        TextView participants = findViewById(R.id.nomParticipantss);
        participants.setText(finalParticipants);

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("dins de onactivityresult galleryactivity");
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode  == RESULT_OK) {
                System.out.println("Estem a update event, onactivityresult, tanquem activitat.");
                Intent output = getIntent();
                setResult(RESULT_OK, output);
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(GalleryActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}
