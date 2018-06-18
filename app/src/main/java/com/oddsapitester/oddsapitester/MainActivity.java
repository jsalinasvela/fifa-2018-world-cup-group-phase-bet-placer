package com.oddsapitester.oddsapitester;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Spinner matchSpinner;
    ProgressDialog dialog;
    Button cargarBtn;
    String data_result;
    JSONArray matches_array = new JSONArray();
    Button apostarbtn_0, apostarbtn_draw, apostarbtn_1;
    double monto;
    String bet_event, betEvent_country;
    TextView textEventTv;
    TextView textOddsTV;
    TextView countryTv_0;
    TextView countryTv_1;
    double bet_odds;
    LinearLayout home_container, draw_container, away_container;
    TextView yourBetCountrTv;
    Button colocar_apuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarBtn = (Button)findViewById(R.id.cargarBtn);

        apostarbtn_0 = (Button)findViewById(R.id.apostar_0);
        apostarbtn_draw = (Button)findViewById(R.id.apostar_draw);
        apostarbtn_1 = (Button)findViewById(R.id.apostar_1);
        home_container = findViewById(R.id.event_container_0);
        away_container = findViewById(R.id.event_container_1);
        draw_container = findViewById(R.id.event_container_draw);

        colocar_apuesta = findViewById(R.id.colocar_apuesta_btn);

        loadEvents();

    }

    public void loadEvents(){

        cargarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strURL= "https://my-json-server.typicode.com/jsalinasvela/sports-odds-api/events";

                wsAsyncTask ws = new wsAsyncTask();
                ws.execute(strURL);
            }
        });

        apostarbtn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bet_event = "0";
                textEventTv = findViewById(R.id.country0);
                betEvent_country= String.valueOf(textEventTv.getText());
                textOddsTV = findViewById(R.id.odds_0);
                bet_odds = Double.parseDouble(String.valueOf(textOddsTV.getText()));
                yourBetCountrTv = findViewById(R.id.apuesta_evento);
                yourBetCountrTv.setText(betEvent_country);

                home_container.setBackgroundResource(R.color.selectedEventBackground);
                draw_container.setBackgroundResource(R.color.unselectedEventBackground);
                away_container.setBackgroundResource(R.color.unselectedEventBackground);


                Log.v("Result:", betEvent_country);
                Log.v("Odds: ", String.valueOf(bet_odds));

            }
        });

        apostarbtn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bet_event="Draw";
                textEventTv = findViewById(R.id.countryDraw);
                betEvent_country= String.valueOf(textEventTv.getText());
                textOddsTV = findViewById(R.id.odds_draw);
                bet_odds = Double.parseDouble(String.valueOf(textOddsTV.getText()));
                yourBetCountrTv = findViewById(R.id.apuesta_evento);
                yourBetCountrTv.setText(betEvent_country);

                home_container.setBackgroundResource(R.color.unselectedEventBackground);
                draw_container.setBackgroundResource(R.color.selectedEventBackground);
                away_container.setBackgroundResource(R.color.unselectedEventBackground);


                Log.v("Result:", betEvent_country);
                Log.v("Odds: ", String.valueOf(bet_odds));
            }
        });

        apostarbtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bet_event="1";
                textEventTv = findViewById(R.id.country1);
                betEvent_country= String.valueOf(textEventTv.getText());
                textOddsTV = findViewById(R.id.odds_1);
                bet_odds = Double.parseDouble(String.valueOf(textOddsTV.getText()));
                yourBetCountrTv = findViewById(R.id.apuesta_evento);
                yourBetCountrTv.setText(betEvent_country);

                home_container.setBackgroundResource(R.color.unselectedEventBackground);
                draw_container.setBackgroundResource(R.color.unselectedEventBackground);
                away_container.setBackgroundResource(R.color.selectedEventBackground);


                Log.v("Result:", betEvent_country);
                Log.v("Odds: ", String.valueOf(bet_odds));
            }
        });

        colocar_apuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String latitude="";
                String longitude="";

                Toast.makeText(getApplicationContext(),
                        "Tu apuesta fue colocada con éxito", Toast.LENGTH_LONG).show();

                //preparing the parameters...we will send the values of latitude and longitude of the matches

                try {
                    //Log.v("REsult 1: ", matches_array.getJSONObject(0).getString("participants"));
                    Log.v("AQUI:", "ESTOY");
                    //looking for the latitute and longitude values that correspond to the selected match
                    for (int j=0; j<matches_array.length();j++){
                        //Log.v("Locations :", matches_array.getJSONObject(j).getString("location"));

                        String home_country = matches_array.getJSONObject(j).getJSONObject("participants").getString("0");
                        String away_country = matches_array.getJSONObject(j).getJSONObject("participants").getString("1");
                        if (home_country.equals(countryTv_0.getText()) && away_country.equals(countryTv_1.getText())){
                            latitude = matches_array.getJSONObject(j).getJSONObject("location").getString("latitude");
                            longitude = matches_array.getJSONObject(j).getJSONObject("location").getString("longitude");
                            break;
                        }

                    }
                } catch (JSONException e) {
                    //matches_array =new JSONArray("[{\"status\":\"failure\"}]");
                    e.printStackTrace();
                }

                Log.v("latitude:", latitude);
                Log.v("longitude", longitude);

                //preparing the view to be loaded
                Intent i = new Intent(getApplicationContext(), SuccessMapActivity.class);

                Bundle b = new Bundle();
                b.putString("latitude", latitude);
                b.putString("longitude", longitude);
                i.putExtras(b);
                //inicio
                startActivity(i);
            }
        });


    }

    public void fillOutMatchSpinner(){

        matchSpinner = (Spinner) findViewById(R.id.match_spinner);
        ArrayList<String> matches = new ArrayList<String>();
        matches.add("Selecciona el partido");

        try {
            //Log.v("REsult 1: ", matches_array.getJSONObject(0).getString("participants"));
            for (int i=0; i<matches_array.length();i++){
                Log.v("Match :", matches_array.getJSONObject(i).getString("participants"));
                String match_name = matches_array.getJSONObject(i).getJSONObject("participants").getString("0") + " - " + matches_array.getJSONObject(i).getJSONObject("participants").getString("1");
                matches.add(match_name);
            }
        } catch (JSONException e) {
            //matches_array =new JSONArray("[{\"status\":\"failure\"}]");
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matches){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        matchSpinner.setAdapter(dataAdapter);

    }

    public void addListenerOnSpinnerItemSelection(){

        matchSpinner = (Spinner) findViewById(R.id.match_spinner);
        matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Log.v("OnItemSelected",adapterView.getItemAtPosition(pos).toString());
                try {
                    Log.v("Country 1", matches_array.getJSONObject(pos-1).getJSONObject("participants").getString("0"));
                    Log.v("Country 1", matches_array.getJSONObject(pos-1).getJSONObject("participants").getString("1"));
                    setBetOdds(pos-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setBetOdds(int index){
        String country_0 = "";
        String country_1 = "";
        String odds_0="";
        String odds_1="";
        String odds_draw="";
        countryTv_0= (TextView)findViewById(R.id.country0);
        countryTv_1= (TextView)findViewById(R.id.country1);
        TextView oddsTv_0= (TextView)findViewById(R.id.odds_0);
        TextView oddsTv_1= (TextView)findViewById(R.id.odds_1);
        TextView oddsTv_draw =(TextView)findViewById(R.id.odds_draw);

        try {
            country_0 = matches_array.getJSONObject(index).getJSONObject("participants").getString("0");
            country_1 = matches_array.getJSONObject(index).getJSONObject("participants").getString("1");
            odds_0=matches_array.getJSONObject(index).getJSONObject("sites").getJSONObject("inkabet").getJSONObject("odds").getJSONObject("h2h").getString("0");
            odds_1=matches_array.getJSONObject(index).getJSONObject("sites").getJSONObject("inkabet").getJSONObject("odds").getJSONObject("h2h").getString("1");
            odds_draw=matches_array.getJSONObject(index).getJSONObject("sites").getJSONObject("inkabet").getJSONObject("odds").getJSONObject("h2h").getString("draw");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        countryTv_0.setText(country_0);
        countryTv_1.setText(country_1);
        oddsTv_0.setText(odds_0);
        oddsTv_1.setText(odds_1);
        oddsTv_draw.setText(odds_draw);

    }

    public class wsAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String[] strURL) {

            return requestWebService(strURL[0]);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Cargando ...");
            dialog.setMessage("Obteniendo Partidos ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected  void onPostExecute(String result){

            //load matches into Spinner
            //ArrayList<String> matches = new ArrayList<String>();
            //Log.v("RESULT:",result);
            data_result = result;
            try {
                matches_array = new JSONArray(data_result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            fillOutMatchSpinner();
            addListenerOnSpinnerItemSelection();

            dialog.dismiss();

        }

        public String requestWebService(String serviceURL){
            HttpURLConnection urlConnection = null;

            try{
                URL urlToRequest = new URL(serviceURL);
                urlConnection = (HttpURLConnection)urlToRequest.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
                //Get JSON data
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner scanner = new Scanner(in);
                String strJSON = scanner.useDelimiter("\\A").next();
                scanner.close();
                return strJSON;
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(SocketTimeoutException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }



}
