package com.example.agenda.tfgagenda.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.agenda.tfgagenda.Manifest;
import com.example.agenda.tfgagenda.R;
import com.example.agenda.tfgagenda.helper.InputValidation;
import com.example.agenda.tfgagenda.rest.Api;
import com.example.agenda.tfgagenda.sql.BDSQLite;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //QuickBlox
    private final String APP_ID = "71317";
    private final String AUTH_KEY = "BrEdQXQR7jT3nF3";
    private final String AUTH_SECRET = "MRpnbUuEc8E2YU5";
    private final String ACCOUNT_KEY = "tiEKB5_Lw9FJ5zrAfM2Z";

    static final int REQUEST_CODE = 1000;
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout  textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextFullName;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private BDSQLite databaseHelper;
    private Api mTodoService;
    private String user;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        //mTodoService = new RetrofitHTTP().setup(getApplication());

        requestRuntimePermission();

        initializeFramework();
        initViews();
        initListeners();
        initObjects();
    }

    private void requestRuntimePermission() {
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case REQUEST_CODE:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getBaseContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeFramework() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    private void initListeners(){
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects(){
        databaseHelper = new BDSQLite(activity);
        inputValidation = new InputValidation(activity);

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }
    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        //if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
               // , textInputEditTextPassword.getText().toString().trim())) {
            //Agafem les dades per QuickBlox
             user = textInputEditTextEmail.getText().toString();
             password = textInputEditTextPassword.getText().toString();


            QBUser qbUser = new QBUser(user,password);
            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    Intent intent = new Intent(LoginActivity.this, ChatDialogActivity.class);//MainActivity
                    //intent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
                    intent.putExtra("user",user);
                    intent.putExtra("password",password);
                    emptyInputEditText();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(QBResponseException e) {
                    Toast.makeText(getBaseContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        //}
        /*
        else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
        */
    }
    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
