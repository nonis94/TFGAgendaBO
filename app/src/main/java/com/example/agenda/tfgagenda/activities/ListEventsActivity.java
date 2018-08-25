package com.example.agenda.tfgagenda.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.helper.RecyclerViewAdapter;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.rest.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEventsActivity extends AppCompatActivity {

    //Servei per les crides a la API
    Api apiService;

    private static final String TAG = "MainActivity";

    private List<Event> events = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Log.d(TAG, "onCreate: started.");
        initImageBitmaps();
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        apiService = ((RetrofitHTTP) this.getApplication()).getAPI();

        Call<List<Event>> call = (Call<List<Event>>) apiService.getEventsByUsername("zenon");
        call.enqueue(new Callback<List<Event>>() {

            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events =response.body();

                for(int i=0;i<events.size();i++){
                    mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
                    mNames.add(events.get(i).getName());
                    adapter.notifyItemInserted(mNames.size());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("No ha funcionat la call = "+t.getMessage());
            }
        });
        /*
        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");
        */

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
