package br.ufal.laccan.wylken.collectactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.ufal.laccan.wylken.collectactivities.DAO.ADLDAO;
import br.ufal.laccan.wylken.collectactivities.helpers.AddActvityFormHelper;
import br.ufal.laccan.wylken.collectactivities.model.ADL;

public class AddActivity extends AppCompatActivity {
    AddActvityFormHelper activityFormHelper;
    ADLDAO adlDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.activityFormHelper = new AddActvityFormHelper(this);
        this.adlDAO = new ADLDAO(this);
        loadButtonActions();



    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        ADL activity = (ADL)  intent.getSerializableExtra("activity");
        if(activity != null) {
            this.activityFormHelper.fillForm(activity);
        }

    }

    private void loadButtonActions() {
        Button btnSave = (Button) findViewById(R.id.activity_add_btn_save);
        Button btnCancel = (Button) findViewById(R.id.activity_add_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AddActivity.this.activityFormHelper.isUpdate()){
                    updateActivity();
                }else{
                    saveActivity();
                }

                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveActivity(){
        ADL adl = activityFormHelper.getActivityFromForm();
        this.adlDAO.saveActivity(adl);
        Toast.makeText(AddActivity.this, "Activity: "+adl.getName()+" saved." , Toast.LENGTH_SHORT).show();
    }

    private void updateActivity(){
        ADL adl = activityFormHelper.getActivityToUpdate();
        this.adlDAO.updateADL(adl);
        Toast.makeText(AddActivity.this, "Activity: "+adl.getName()+" updated." , Toast.LENGTH_SHORT).show();
    }


}
