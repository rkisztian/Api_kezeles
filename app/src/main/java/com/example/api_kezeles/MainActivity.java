package com.example.api_kezeles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String data = "";
    TextView textViewEredmeny;
    Button ButtonFrissit, ButtonFelvesz;
    EditText editTextGyarto, editTextModell, editTextUzembe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        textViewEredmeny.setMovementMethod(new ScrollingMovementMethod());
        listaz();
        ButtonFrissit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaz();
            }
        });

        ButtonFelvesz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gyarto = editTextGyarto.getText().toString().trim();
                String modell = editTextModell.getText().toString().trim();
                String uzembe = editTextUzembe.getText().toString().trim();
                String json = "{ \"gyarto\" : \""+gyarto+"\", \"modell\" : \""+modell+
                        "\", \"uzembehelyezes\" : \""+uzembe+"\" }";

                PerformNetworkRequest request = new PerformNetworkRequest("POST", json);
                request.execute();
            }
        });
    }

    private void listaz(){
        PerformNetworkRequest request = new PerformNetworkRequest("GET");
        request.execute();
    }

    private void frissitMuvelet(){
        this.runOnUiThread(frissites);
    }

    private Runnable frissites = new Runnable() {
        @Override
        public void run() {
            textViewEredmeny.setText(data);
        }
    };

    public void init(){
        textViewEredmeny = findViewById(R.id.TextViewEredmeny);
        ButtonFrissit = findViewById(R.id.ButtonFrissit);
        ButtonFelvesz = findViewById(R.id.ButtonFelvesz);
        editTextGyarto = findViewById(R.id.editTextGyarto);
        editTextModell = findViewById(R.id.editTextModell);
        editTextUzembe = findViewById(R.id.editTextUzembe);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String>{
        String method;
        String content;

        public PerformNetworkRequest(String method){
            this.method = method;
            this.content ="";
        }

        public PerformNetworkRequest(String method, String content){
            this.method = method;
            this.content = content;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if(method.equals("GET")){
                    data = Request.getData();
                }else{
                    data = Request.postData(content);
                }
            } catch (IOException e){
                data = e.getMessage();
            }

            frissitMuvelet();
            return data;
        }
    }

}