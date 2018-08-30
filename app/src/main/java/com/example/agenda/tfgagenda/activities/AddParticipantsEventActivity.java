package com.example.agenda.tfgagenda.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.Retrofit.RetrofitHTTP;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.model.User;
import com.example.agenda.tfgagenda.rest.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddParticipantsEventActivity extends AppCompatActivity {

    //Servei per les crides a la API
    Api apiService;

    private List<User> users = new ArrayList<>();

    Button mOrder;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private ArrayList<String> nomParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants_event);

        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        nomParticipants = getIntent().getStringArrayListExtra("nomParticipants");
        //listItems = getResources().getStringArray(R.array.shopping_item);

        apiService = ((RetrofitHTTP) this.getApplication()).getAPI();

        Call<List<User>> call = (Call<List<User>>) apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println("Crida noms usuaris correcte :)");
                users =response.body();
                listItems = new String[users.size()];
                for(int i=0;i<users.size();i++){
                    listItems[i]=users.get(i).getName();
                }
                checkedItems = new boolean[listItems.length];

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddParticipantsEventActivity.this);
                mBuilder.setTitle("List of users");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            nomParticipants.add(listItems[mUserItems.get(i)]);
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        //mItemSelected.setText(item);
                        Intent output = getIntent();
                        output.putStringArrayListExtra("nomParticipants",nomParticipants);
                        setResult(RESULT_OK, output);
                        finish();
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("No ha funcionat la call = "+t.getMessage());
            }
        });
    }
}
