package com.example.agenda.tfgagenda.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda.tfgagenda.R;

import io.paperdb.Paper;

public class WidgetActivity extends AppCompatActivity {


    Button btnSave;
    EditText edtTitle,edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        //Inicialitzem el paper
        Paper.init(this);


        btnSave = (Button)findViewById(R.id.edt_save);
        edtTitle = (EditText)findViewById(R.id.edt_title);
        edtContent = (EditText)findViewById(R.id.edt_content);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("title",edtTitle.getText().toString());
                Paper.book().write("content",edtContent.getText().toString());

                Toast.makeText(WidgetActivity.this,"Saved",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
