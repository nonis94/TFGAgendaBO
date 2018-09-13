package com.example.agenda.tfgagenda.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.agenda.tfgagenda.activities.AddActivity;
import com.example.agenda.tfgagenda.activities.GalleryActivity;
import com.example.agenda.tfgagenda.activities.ListEventsActivity;
import com.example.agenda.tfgagenda.activities.ViewEventsActivity;
import com.example.agenda.tfgagenda.model.Empty;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.rest.Api;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

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

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images,ArrayList<String> latitud,ArrayList<String> longitud,ArrayList<String> datesInici ,ArrayList<String> nomParticipantss,ArrayList<String> datesFinal,ArrayList<String> horesInici,ArrayList<String> horesFinal,Api apiService,ArrayList<Long> id ,String user) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                mContext.startActivity(intent);
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


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image;
        public TextView imageName;
        public RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
