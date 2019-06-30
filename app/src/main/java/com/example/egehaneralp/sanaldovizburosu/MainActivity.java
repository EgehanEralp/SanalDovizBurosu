package com.example.egehaneralp.sanaldovizburosu;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    EditText editText;
    TextView tlT,dolarT,euroT,poundT;
    Button button;
    RadioButton tlrb,dolarrb,eurorb;
    RadioGroup bgrup;
    RadioButton rB;
    int girilensayi,a;
    String stext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tlT=findViewById(R.id.tlT);
        dolarT=findViewById(R.id.dolarT);
        euroT=findViewById(R.id.euroT);
        poundT=findViewById(R.id.poundT);

        bgrup=findViewById(R.id.bgrup);
        tlrb=findViewById(R.id.tlrb);
        dolarrb=findViewById(R.id.dolarrb);
        eurorb=findViewById(R.id.eurorb);

        editText=findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new ArkaPlan().execute("https://api.exchangeratesapi.io/latest?base=TRY");
            }
        });



        /*button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ArkaPlan().execute("https://api.exchangeratesapi.io/latest?base=TRY");

            }
        });*/

    }

    class ArkaPlan extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader buf;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                buf = new BufferedReader(new InputStreamReader(is));

                String satir, dosya ="";

                while ((satir = buf.readLine()) != null) {
                    //Log.d("satir", satir);
                    dosya += satir;  // WHİLE BİTTİGİNDE SUNUCUDAKİ TÜM SATİRLARI ELDE ETMİŞ OLUCAĞIM

                }
                return dosya;

            } catch (Exception e) {
                e.printStackTrace();

            }

            return "sorun";
        }

        @Override
        protected void onPostExecute(String s) {
            stext=editText.getText().toString();
            girilensayi=Integer.parseInt(stext);
            tlT.setText(stext);
            int selectedId = bgrup.getCheckedRadioButtonId();
            rB= findViewById(selectedId);

            try{
                JSONObject json=new JSONObject(s);
                String rates =json.getString("rates");

                JSONObject json2=new JSONObject(rates);


                if(rB==tlrb){
                    tlT.setText("TL = "+ girilensayi);

                    double a=json2.getDouble("USD");
                    dolarT.setText("USD = "+(a*girilensayi));

                    double b=json2.getDouble("EUR");
                    euroT.setText("EUR = "+(b*girilensayi));

                    double c=json2.getDouble("GBP");
                    poundT.setText("GBP = "+(c*girilensayi));
                }

                else if(rB==dolarrb){     //KURDAKİ SAYILARI DOLAR KURUNA BÖL SONRA *girilensayi

                    double kur=json2.getDouble("USD");

                    tlT.setText("TL = "+((1/kur) * girilensayi));

                    dolarT.setText("USD = "+girilensayi);

                    double b=json2.getDouble("EUR");
                    euroT.setText("EUR = "+((b/kur)*girilensayi));

                    double c=json2.getDouble("GBP");
                    poundT.setText("GBP = "+((c/kur)*girilensayi));
                }

                else if(rB==eurorb){

                    double kur=json2.getDouble("EUR");

                    tlT.setText("TL      ="+ ((1/kur)*girilensayi));

                    double a=json2.getDouble("USD");
                    dolarT.setText("USD    ="+((a/kur)*girilensayi));

                    euroT.setText("EURO ="+girilensayi);

                    double c=json2.getDouble("GBP");
                    poundT.setText("GBP   ="+((c/kur)*girilensayi));
                }


            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

}
