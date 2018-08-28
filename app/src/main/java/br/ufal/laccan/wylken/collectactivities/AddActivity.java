package br.ufal.laccan.wylken.collectactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufal.laccan.wylken.collectactivities.DAO.ADLDAO;
import br.ufal.laccan.wylken.collectactivities.helpers.AddActvityFormHelper;

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

    private void loadButtonActions() {
        Button btnSave = (Button) findViewById(R.id.activity_add_btn_save);
        Button btnCancel = (Button) findViewById(R.id.activity_add_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
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
        this.adlDAO.saveActivity(activityFormHelper.getActivity());
    }


}
