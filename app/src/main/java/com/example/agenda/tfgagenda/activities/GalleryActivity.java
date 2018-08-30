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

import com.bumptech.glide.Glide;
import com.example.agenda.tfgagenda.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by User on 1/2/2018.
 */

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    private GoogleMap googleMap;
    private Button locationButton;
    private String imageUrl;
    private String imageName;
    private String date;
    private String latitud;
    private String longitud;
    private String nomParticipantss;
    private String datesFinal;
    private String horesInici;
    private String horesFinal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);
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



}
