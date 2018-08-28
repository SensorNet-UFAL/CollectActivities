package br.ufal.laccan.wylken.collectactivities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Spinner spinnerActivity;
    private Spinner spinnerPerson;
    private TextView textX;
    private TextView textY;
    private TextView textZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int sensorType;
    private double ax,ay,az;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textX = (TextView) findViewById(R.id.x_accelerometer);
        textY = (TextView) findViewById(R.id.y_accelerometer);
        textZ = (TextView) findViewById(R.id.z_accelerometer);

        addItemSpinnerActivity();
        addItemSpinnerPerson();

        //Get Sensor
        this.sensorType = Sensor.TYPE_MAGNETIC_FIELD;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(this.sensorType) != null) {
            this.accelerometer = sensorManager.getDefaultSensor(this.sensorType);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(this.sensorType), SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Sensor Iniciado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao ler sensor!", Toast.LENGTH_SHORT).show();
        }

    }

    private void addItemSpinnerActivity(){
        this.spinnerActivity = (Spinner) findViewById(R.id.spinner_activity);

        List<String> activitiesList = new ArrayList<String>();
        activitiesList.add("Walking");
        activitiesList.add("Sitdown");
        activitiesList.add("Laydown");

        ArrayAdapter<String> activitiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activitiesList);
        activitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerActivity.setAdapter(activitiesAdapter);
    }

    private void addItemSpinnerPerson(){

        this.spinnerPerson = (Spinner) findViewById(R.id.spinner_person);

        List<String> personsList = new ArrayList<String>();
        personsList.add("Wylken");
        personsList.add("Rafaela");
        personsList.add("Maria");

        ArrayAdapter<String> personsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, personsList);
        personsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerPerson.setAdapter(personsAdapter);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==this.sensorType){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

            textX.setText(String.format("%.4f", ax));
            textY.setText(String.format("%.4f", ay));
            textZ.setText(String.format("%.4f", az));
        }
    }
}
