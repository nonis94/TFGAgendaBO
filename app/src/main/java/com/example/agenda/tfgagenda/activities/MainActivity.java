package com.example.agenda.tfgagenda.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.sql.BDSQLite;

public class MainActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener{

    private final AppCompatActivity activity = MainActivity.this;
    private CalendarView calendarView;
    private BDSQLite bd;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("user"); //Obtenim el nom(correu)
        bd = new BDSQLite(activity);
        calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[2];
        items[0]= "Add event";
        items[1]= "Cancel";

        final int dia;
        int mes;
        final int any;
        dia = i;
        mes = i1;
        any = i2;
        mes++;
        final int finalMes = mes;
        builder.setTitle("Selecct an option").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){ //Añadir evento
                    Intent intent = new Intent(getApplication(), AddActivity.class);
                    intent.putExtra("dia",String.valueOf(dia));
                    intent.putExtra("mes",String.valueOf(finalMes));
                    intent.putExtra("any",String.valueOf(any));
                    intent.putExtra("user",String.valueOf(user));
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putInt("dia",dia);
                    bundle.putInt("mes", finalMes);
                    bundle.putInt("any",any);
                    intent.putExtras(bundle);
                    */

                    startActivity(intent);
                }
                else if(i==1){ //Cancelar
                    return;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();;
    }
}
