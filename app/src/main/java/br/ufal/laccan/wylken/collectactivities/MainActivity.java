package br.ufal.laccan.wylken.collectactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import java.util.Calendar;

import br.ufal.laccan.wylken.collectactivities.DAO.ADLDAO;
import br.ufal.laccan.wylken.collectactivities.DAO.ActivityRecordDAO;
import br.ufal.laccan.wylken.collectactivities.DAO.PersonDAO;
import br.ufal.laccan.wylken.collectactivities.model.ADL;
import br.ufal.laccan.wylken.collectactivities.model.ActivityRecord;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Vibrator vibrator;
    private Spinner spinnerActivity;
    private Spinner spinnerPerson;
    private TextView textX;
    private TextView textY;
    private TextView textZ;
    private Button startButton;
    private Button stopButton;
    private ADLDAO adlDAO;
    private PersonDAO personDAO;
    private ActivityRecordDAO activityRecordDAO;
    private ADL activitySelected;
    private Person personSelected;
    private ArrayList<ADL> adls;
    private ArrayList<Person> persons;
    private ArrayList<ActivityRecord> activityRecordList;

    //Variables to using at measurement.
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int sensorType;
    private double ax,ay,az;
    private float [] gravity;
    private float [] linear_acceleration;
    private final float thresholdToSensorRead = 0.05f;
    private final float alpha = 0.8f;
    private final short minReads = 50;
    private short numReads = 0;
    private boolean recordedActivityFlag = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        this.activityRecordList = null;

        this.adlDAO = new ADLDAO(this);
        this.personDAO = new PersonDAO(this);
        this.activityRecordDAO = new ActivityRecordDAO(this);

        //Adding function in all buttons
        //Start and Stop
        functionButtonStart();
        functionButtonStop();
        //Activity
        functionButtonAddActivity();
        functionButtonEditActivity();
        functionButtonDeletectivity();
        //Person
        functionButtonAddPerson();
        functionButtonEditPerson();
        functionButtonDeletePerson();

        startSensors();

    }

    @Override
    protected void onResume(){
        super.onResume();
        addItemSpinnerActivity();
        addItemSpinnerPerson();

    }

    private void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            this.vibrator.vibrate(500);
        }
    }

    Runnable startRecordActivity  = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.vibrate();
            MainActivity.this.recordedActivityFlag = true;
        }
    };

    private void functionButtonStart() {
        this.startButton= (Button) findViewById(R.id.btn_start);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler myHandler = new Handler();

                myHandler.postDelayed(MainActivity.this.startRecordActivity, 3000);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Activity");
                builder.setMessage("Recording data of activity....");
                builder.setPositiveButton("Save recording",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.saveRecords();
                                Toast.makeText(MainActivity.this, "Activity Recorded", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "Unsaved Activity!", Toast.LENGTH_SHORT).show();
                        MainActivity.this.cancelRecordActivity();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        this.startButton.setEnabled(false);
    }

    private void functionButtonStop() {
        this.stopButton= (Button) findViewById(R.id.btn_stop);
        this.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.stopButton.setEnabled(false);
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
                confirmDeleteActivity();
            }
        });
    }

    private void functionButtonAddPerson() {

        Button addPerson = (Button) findViewById(R.id.btn_add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddActivity = new Intent(MainActivity.this, AddPerson.class);
                startActivity(intentAddActivity);
            }
        });

    }

    private void functionButtonEditPerson() {
        Button editPerson = (Button) findViewById(R.id.btn_edit_person);
        editPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddPerson = new Intent(MainActivity.this, AddPerson.class);
                intentAddPerson.putExtra("person", MainActivity.this.personSelected);
                startActivity(intentAddPerson);
            }
        });
    }

    private void functionButtonDeletePerson() {
        Button deletePerson = (Button) findViewById(R.id.btn_delete_person);
        deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeletePerson();
            }
        });
    }


    public void confirmDeleteActivity(){
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

    public void confirmDeletePerson(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want delete \""+MainActivity.this.personSelected.getName()+"\"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.personDAO.deletePerson(MainActivity.this.personSelected);
                        addItemSpinnerPerson();
                        Toast.makeText(MainActivity.this, "Person deleted!", Toast.LENGTH_SHORT).show();
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

        //Add click on Activity list
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

        //Add click on Person list
        this.spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.personSelected = (Person) MainActivity.this.spinnerPerson.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void startSensors(){
        textX = (TextView) findViewById(R.id.x_accelerometer);
        textY = (TextView) findViewById(R.id.y_accelerometer);
        textZ = (TextView) findViewById(R.id.z_accelerometer);

        this.gravity = new float[3];
        this.gravity[0] = 0;
        this.gravity[1] = 0;
        this.gravity[2] = 0;

        this.linear_acceleration = new float[3];
        this.linear_acceleration[0] = 0;
        this.linear_acceleration[1] = 0;
        this.linear_acceleration[2] = 0;


        //Get Sensor
        this.sensorType = Sensor.TYPE_ACCELEROMETER;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(this.sensorType) != null) {
            this.accelerometer = sensorManager.getDefaultSensor(this.sensorType);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(this.sensorType), SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Calibrando acelerômetro ...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Erro ao ler sensor!", Toast.LENGTH_SHORT).show();
        }
    }

    private float [] getAcceleration(float [] values){


        this.gravity[0] = alpha * this.gravity[0] + (1 - alpha) * values[0];
        this.gravity[1] = alpha * this.gravity[1] + (1 - alpha) * values[1];
        this.gravity[2] = alpha * this.gravity[2] + (1 - alpha) * values[2];

        this.linear_acceleration[0] = values[0] - this.gravity[0];
        this.linear_acceleration[1] = values[1] - this.gravity[1];
        this.linear_acceleration[2] = values[2] - this.gravity[2];

        for(int i = 0; i < this.linear_acceleration.length; i++){
            if (this.linear_acceleration[i] < this.thresholdToSensorRead && this.linear_acceleration[i] > -this.thresholdToSensorRead){
                this.linear_acceleration[i] = 0;
            }
        }

        return this.linear_acceleration;
    }

    public void cancelRecordActivity(){
        this.recordedActivityFlag = false;
        this.activityRecordList = null;
    }

    public boolean saveRecords(){
        if(this.activityRecordList != null){
            this.activityRecordDAO.saveActivityRecord(this.activityRecordList);
            this.cancelRecordActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==this.sensorType){

            if(this.numReads > this.minReads){

                this.getAcceleration(event.values);
                ax= this.linear_acceleration[0];
                ay= this.linear_acceleration[1];
                az= this.linear_acceleration[2];

                textX.setText(String.format("%.4f", ax));
                textY.setText(String.format("%.4f", ay));
                textZ.setText(String.format("%.4f", az));

                if(this.recordedActivityFlag){
                    this.recordedActivity();
                }
            }
            else{
                numReads += 1;
            }

            if(this.numReads == this.minReads){
                this.startButton.setEnabled(true);
                Toast.makeText(this, "Sensor pronto para uso.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void recordedActivity() {
        if(this.activityRecordList == null){
            this.activityRecordList = new ArrayList<ActivityRecord>();
        }

        ActivityRecord activity = new ActivityRecord();
        activity.setActivity_tag(this.activitySelected.getTag().shortValue());
        activity.setPerson_tag(this.personSelected.getTag().shortValue());
        activity.setSensor_type(ActivityRecord.SENSOR_TYPE_ACCELEROMETER);
        activity.setX(this.linear_acceleration[0]);
        activity.setY(this.linear_acceleration[1]);
        activity.setZ(this.linear_acceleration[2]);
        activity.setTime(Calendar.getInstance().getTime());

        this.activityRecordList.add(activity);
    }
}
