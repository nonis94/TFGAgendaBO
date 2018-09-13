package com.example.agenda.tfgagenda.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.helper.RecyclerViewAdapter;
import com.example.agenda.tfgagenda.model.Empty;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.rest.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEventsActivity extends AppCompatActivity {

    //Servei per les crides a la API
    Api apiService;

    private static final String TAG = "MainActivity";

    private List<Event> events = new ArrayList<>();
    private RecyclerViewAdapterr adapter;

    private String user;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> latitud = new ArrayList<>();
    private ArrayList<String> longitud = new ArrayList<>();
    private ArrayList<String> datesInici = new ArrayList<>();
    private ArrayList<String> nomParticipantss = new ArrayList<>();
    private ArrayList<String> datesFinal = new ArrayList<>();
    private ArrayList<String> horesInici = new ArrayList<>();
    private ArrayList<String> horesFinal = new ArrayList<>();
    private ArrayList<Long> id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        user = getIntent().getStringExtra("user"); //Obtenim el nom(correu)

        Log.d(TAG, "onCreate: started.");
        initImageBitmaps();
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        apiService = ((RetrofitHTTP) this.getApplication()).getAPI();

        Call<List<Event>> call = (Call<List<Event>>) apiService.getEventsByUsername(user);
        call.enqueue(new Callback<List<Event>>() {

            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events =response.body();

                for(int i=0;i<events.size();i++){
                    mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
                    mNames.add(events.get(i).getName());
                    latitud.add(events.get(i).getLatitud());
                    longitud.add(events.get(i).getLongitud());
                    datesInici.add(events.get(i).getEventDate());
                    nomParticipantss.add(events.get(i).getNomParticipantss());
                    datesFinal.add(events.get(i).getEventDateFinish());
                    horesInici.add(events.get(i).getHourStart());
                    horesFinal.add(events.get(i).getHourEnd());
                    id.add((long) events.get(i).getId());
                    System.out.println("El nom del participant es= "+events.get(i).getNomParticipantss());
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
        adapter = new RecyclerViewAdapterr(this, mNames, mImageUrls,latitud,longitud,datesInici,nomParticipantss,datesFinal,horesInici,horesFinal,apiService,id,user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode  == RESULT_OK) {
                System.out.println("Entrem a activityresult de ListEventsActivity");
                finish();
                startActivity(getIntent());
            }
        } catch (Exception ex) {
            Toast.makeText(ListEventsActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private class RecyclerViewAdapterr extends RecyclerView.Adapter<com.example.agenda.tfgagenda.helper.RecyclerViewAdapter.ViewHolder> {

        //Servei per les crides a la API
        Api apiService;

        private static final String TAG = "RecyclerViewAdapter";

        private ArrayList<String> mImageNames = new ArrayList<>();
        private ArrayList<String> mImages = new ArrayList<>();
        private ArrayList<String> latitud = new ArrayList<>();
        private ArrayList<String> longitud = new ArrayList<>();
        private ArrayList<String> datesInici = new ArrayList<>();
        private ArrayList<String> nomParticipantss = new ArrayList<>();
        private ArrayList<String> datesFinal = new ArrayList<>();
        private ArrayList<String> horesInici = new ArrayList<>();
        private ArrayList<String> horesFinal = new ArrayList<>();
        private ArrayList<Long> id = new ArrayList<>();
        private Context mContext;
        private String user;

        public RecyclerViewAdapterr(Context context, ArrayList<String> imageNames, ArrayList<String> images,ArrayList<String> latitud,ArrayList<String> longitud,ArrayList<String> datesInici ,ArrayList<String> nomParticipantss,ArrayList<String> datesFinal,ArrayList<String> horesInici,ArrayList<String> horesFinal,Api apiService,ArrayList<Long> id ,String user) {
            mImageNames = imageNames;
            mImages = images;
            this.latitud = latitud;
            this.longitud = longitud;
            this.datesInici = datesInici;
            this.nomParticipantss = nomParticipantss;
            this.datesFinal = datesFinal;
            this.horesInici = horesInici;
            this.horesFinal = horesFinal;
            this.apiService = apiService;
            this.id=id;
            this.user = user;
            mContext = context;
        }

        @Override
        public com.example.agenda.tfgagenda.helper.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
            com.example.agenda.tfgagenda.helper.RecyclerViewAdapter.ViewHolder holder = new com.example.agenda.tfgagenda.helper.RecyclerViewAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final com.example.agenda.tfgagenda.helper.RecyclerViewAdapter.ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder: called.");

            Glide.with(mContext)
                    .asBitmap()
                    .load(mImages.get(position))
                    .into(holder.image);

            holder.imageName.setText(mImageNames.get(position));

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));

                    Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, GalleryActivity.class);
                    intent.putExtra("image_url", mImages.get(position));
                    intent.putExtra("image_name", mImageNames.get(position));
                    intent.putExtra("datesInici", datesInici.get(position));
                    intent.putExtra("latitud", latitud.get(position));
                    intent.putExtra("longitud", longitud.get(position));
                    intent.putExtra("nomParticipantss", nomParticipantss.get(position));
                    intent.putExtra("datesFinal", datesFinal.get(position));
                    intent.putExtra("horesInici", horesInici.get(position));
                    intent.putExtra("horesFinal", horesFinal.get(position));
                    intent.putExtra("id", id.get(position));
                    intent.putExtra("user", user);
                    //mContext.startActivity(intent);
                    startActivityForResult(intent,1);
                }
            });
            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    CharSequence []items = new CharSequence[2];
                    items[0]= "Delete event";
                    items[1]= "Cancel";

                    builder.setTitle("Selecct an option").setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i==0){ //Delete event
                                Call<Empty> call = (Call<Empty>) apiService.deleteEvent(id.get(position));
                                call.enqueue(new Callback<Empty>() {
                                    @Override
                                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                                        System.out.println("Hem esborrat esdeveniment :)");
                                    }

                                    @Override
                                    public void onFailure(Call<Empty> call, Throwable t) {
                                        System.out.println("Error en borrar esdeveniment "+t.getMessage());
                                    }
                                });
                            }
                            else if(i==1){ //Cancelar
                                return;
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mImageNames.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            CircleImageView image;
            TextView imageName;
            RelativeLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                imageName = itemView.findViewById(R.id.image_name);
                parentLayout = itemView.findViewById(R.id.parent_layout);
            }
        }
    }

}
