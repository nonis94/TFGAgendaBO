package com.example.agenda.tfgagenda.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.rest.Api;
import com.example.agenda.tfgagenda.util.Global;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {

    GridLayout mainGrid;
    String user,password;

    //Servei per les crides a la API
    Api apiService;
    private List<Object> mAdapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = getIntent().getStringExtra("user"); //Obtenim el nom(correu)
        password = getIntent().getStringExtra("password"); //Obtenim el nom(correu)

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);



        apiService = ((RetrofitHTTP) this.getApplication()).getAPI();

        Call<List<Event>> call = (Call<List<Event>>) apiService.getEventsByUsername("zenon");
        call.enqueue(new Callback<List<Event>>() {

            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                List<Event> eventss =response.body();

                System.out.println("El nombre d'elements que tenim es =");
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("No ha funcionat la call = "+t.getMessage());
            }
        });
        //Event pel click
        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {

        //Bucle per recorrer tots els elements del grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //Bucle pels cardViews, perque son els fills del mainGrid
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==0){ //Calendarii
                        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    if(finalI==1){ //Chat Privat o Public
                        Intent intent = new Intent(MenuActivity.this,ChatDialogActivity.class);
                        intent.putExtra("user",user);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    }

                }
            });
        }
    }
}
