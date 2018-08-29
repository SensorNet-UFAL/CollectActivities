package br.ufal.laccan.wylken.collectactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufal.laccan.wylken.collectactivities.DAO.ADLDAO;
import br.ufal.laccan.wylken.collectactivities.DAO.PersonDAO;
import br.ufal.laccan.wylken.collectactivities.model.ADL;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Spinner spinnerActivity;
    private Spinner spinnerPerson;
    private TextView textX;
    private TextView textY;
    private TextView textZ;
    private ADLDAO adlDAO;
    private PersonDAO personDAO;
    private ADL activitySelected;
    private ArrayList<ADL> adls;
    private ArrayList<Person> persons;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int sensorType;
    private double ax,ay,az;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.adlDAO = new ADLDAO(this);
        this.personDAO = new PersonDAO(this);

        functionButtonAddActivity();
        functionButtonEditActivity();
        functionButtonDeletectivity();
        //startSensors();

    }

    @Override
    protected void onResume(){
        super.onResume();
        addItemSpinnerActivity();
        addItemSpinnerPerson();

    }

    private void functionButtonEditActivity() {
        Button editActivity = (Button) findViewById(R.id.btn_edit_activity);
        editActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddActivity = new Intent(MainActivity.this, AddActivity.class);
                intentAddActivity.putExtra("activity", MainActivity.this.activitySelected);
                startActivity(intentAddActivity);
            }
        });
    }

    private void functionButtonAddActivity() {
        Button addActivity = (Button) findViewById(R.id.btn_add_activity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddActivity = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intentAddActivity);
            }
        });
    }

    private void functionButtonDeletectivity() {
        Button deleteActivity = (Button) findViewById(R.id.btn_delete_activity);
        deleteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    public void confirmDelete(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want delete \""+MainActivity.this.activitySelected.getName()+"\" activity?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.adlDAO.deleteADL(MainActivity.this.activitySelected);
                        addItemSpinnerActivity();
                        Toast.makeText(MainActivity.this, "Activity deleted!", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void addItemSpinnerActivity(){
        this.spinnerActivity = (Spinner) findViewById(R.id.spinner_activity);

        //Get from DB
        this.adls = this.adlDAO.getADLs();
        //-------

        ArrayAdapter<ADL> activitiesAdapter = new ArrayAdapter<ADL>(this, android.R.layout.simple_spinner_item, adls);
        activitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerActivity.setAdapter(activitiesAdapter);

        //Add click on item list
        this.spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.activitySelected = (ADL) MainActivity.this.spinnerActivity.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void addItemSpinnerPerson(){

        this.spinnerPerson = (Spinner) findViewById(R.id.spinner_person);

        this.persons = personDAO.getPersons();

        ArrayAdapter<Person> personsAdapter = new ArrayAdapter<Person>(this, android.R.layout.simple_spinner_item, this.persons);
        personsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerPerson.setAdapter(personsAdapter);

    }

    private void startSensors(){
        textX = (TextView) findViewById(R.id.x_accelerometer);
        textY = (TextView) findViewById(R.id.y_accelerometer);
        textZ = (TextView) findViewById(R.id.z_accelerometer);

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
