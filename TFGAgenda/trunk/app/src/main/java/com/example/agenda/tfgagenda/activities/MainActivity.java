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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bd = new BDSQLite(activity);
        calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[3];
        items[0]= "Añadir evento";
        items[1]= "Ver evento";
        items[2]= "Cancelar";

        final int dia;
        int mes;
        final int any;
        dia = i;
        mes = i1;
        any = i2;
        mes++;
        final int finalMes = mes;
        builder.setTitle("Selecciona una opcion").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){ //Añadir evento
                    Intent intent = new Intent(getApplication(), AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("dia",dia);
                    bundle.putInt("mes", finalMes);
                    bundle.putInt("any",any);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(i==1){ //Ver eventos
                    Intent intent = new Intent(getApplication(), ViewEventsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("dia",dia);
                    bundle.putInt("mes", finalMes);
                    bundle.putInt("any",any);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(i==2){ //Cancelar
                    return;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();;
    }
}
