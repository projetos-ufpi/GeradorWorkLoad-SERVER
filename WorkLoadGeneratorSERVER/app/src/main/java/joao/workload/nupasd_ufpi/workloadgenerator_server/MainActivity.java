package joao.workload.nupasd_ufpi.workloadgenerator_server;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Handler handler = new Handler();

    private Button buttonStartReceiving;
    private Button buttonStopReceiving;
    private TextView textViewDataFromClient;
    private boolean end = false;


    private String stringData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartReceiving = (Button) findViewById(R.id.btn_start_receiving);
        buttonStopReceiving = (Button) findViewById(R.id.btn_stop_receiving);
        textViewDataFromClient = (TextView) findViewById(R.id.tv_data_from_client);

        buttonStartReceiving.setOnClickListener(this);
        buttonStopReceiving.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= 23 && (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    android.Manifest.permission.WAKE_LOCK
            }, 0);
        }

    }

    private void startServerSocket() {

        Thread thread = new Thread(new Runnable() {



            @Override
            public void run() {

                try {

                    ServerSocket ss = new ServerSocket(9002);

                    while (true) {
                        //Server is waiting for client here, if needed
                        Socket s = ss.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter output = new PrintWriter(s.getOutputStream());

                        stringData = input.readLine();
                        output.println("FROM SERVER - " + stringData.toUpperCase());
                        output.flush();

                        //if (stringData.equalsIgnoreCase("a")) {
                            geraCarga(stringData);
                        //}


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateUI(stringData);
                        if (stringData.equalsIgnoreCase("STOP")) {
                            end = true;
                            output.close();
                            s.close();
                            break;
                        }

                        output.close();
                        s.close();
                    }
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }


    private void geraCarga(final String scan){//A variável scan serve para transporta o valor enviado pelo cliente
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Teste");
                System.out.println("\nTestando saída no resultado!");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("teste")
                        .setMessage("Testando possibilidades")
                        .setPositiveButton("aaaa", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }).start();
        */
        handler.post(new Runnable() {
            @Override
            public void run() {
//
                try {
                    AssetManager assetManager = getResources().getAssets();
                    InputStream inputStream = null;

                    if (scan.equalsIgnoreCase("100")){
                        inputStream = assetManager.open("100_words.txt");
                    }else if (scan.equalsIgnoreCase("1000")){
                        inputStream = assetManager.open("1000_words.txt");
                    }else if (scan.equalsIgnoreCase("10k")){
                        inputStream = assetManager.open("10k_words.txt");
                    }else if (scan.equalsIgnoreCase("100k")){
                        inputStream = assetManager.open("100k_words.txt");
                    }else if (scan.equalsIgnoreCase("500k")){
                        inputStream = assetManager.open("500k_words.txt");
                    }else if (scan.equalsIgnoreCase("1m")) {
                        inputStream = assetManager.open("1M_words.txt");
                    }else if (scan.equalsIgnoreCase("2m")) {
                        inputStream = assetManager.open("2M_words.txt");
                    }else if (scan.equalsIgnoreCase("4m")) {
                        inputStream = assetManager.open("4M_words.txt");
                    }else if (scan.equalsIgnoreCase("5m")) {
                        inputStream = assetManager.open("5M_words.txt");
                    }else if (scan.equalsIgnoreCase("8m")) {
                        inputStream = assetManager.open("8M_words.txt");
                    }else if (scan.equalsIgnoreCase("10m")) {
                        inputStream = assetManager.open("10M_words.txt");
                    }else if (scan.equalsIgnoreCase("15m")) {
                        inputStream = assetManager.open("15M_words.txt");
                    }else if (scan.equalsIgnoreCase("20m")) {
                        inputStream = assetManager.open("20M_words.txt");
                    }



                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String linha;
                    int quant1 = 0;
                    double tempo1 = 0, tempo2 = 0;
                    //LinkedList<String> linhas = new LinkedList<String>();
                    tempo1 = System.currentTimeMillis();
                    while((linha = bufferedReader.readLine()) !=null){
                        //aqui com o valor da linha vc pode testar o que quiser, por exemplo: linha.equals("123")
                        quant1 += linha.length();
                    }
                    tempo2 = System.currentTimeMillis();

                    android.app.AlertDialog alert;
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Quantidade de Caracteres - Carga "+scan);
                    builder.setMessage("A quantidade de caracteres: "+quant1+"\nTempo total de leitura: "+ ((tempo2/1000) - (tempo1/1000)) +"segundos");
                    builder.setPositiveButton("Ok", null);
                    alert = builder.create();
                    alert.show();
                    System.out.println("Quantidade: "+quant1);
                    System.out.println("\nTempo de leitura: "+ ((tempo2/1000) - (tempo1/1000))+" segundos");


                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void updateUI(final String stringData) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                String s = textViewDataFromClient.getText().toString();
                if (stringData.trim().length() != 0)
                    textViewDataFromClient.setText(s + "\n" + "Enviado por Client: " + stringData);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_start_receiving:

                startServerSocket();
                buttonStartReceiving.setEnabled(false);
                buttonStopReceiving.setEnabled(true);
                break;

            case R.id.btn_stop_receiving:

                //stopping server socket logic you can add yourself
                buttonStartReceiving.setEnabled(true);
                buttonStopReceiving.setEnabled(false);
                break;
        }
    }
}