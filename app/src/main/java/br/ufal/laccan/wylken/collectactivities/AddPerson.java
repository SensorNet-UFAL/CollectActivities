package br.ufal.laccan.wylken.collectactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.ufal.laccan.wylken.collectactivities.DAO.PersonDAO;
import br.ufal.laccan.wylken.collectactivities.helpers.AddActvityFormHelper;
import br.ufal.laccan.wylken.collectactivities.helpers.AddPersonFormHelper;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class AddPerson extends AppCompatActivity {

    private AddPersonFormHelper addPersonFormHelper;
    private PersonDAO personDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        this.addPersonFormHelper = new AddPersonFormHelper(this);
        this.personDAO = new PersonDAO(this);

        loadButtonActions();
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        Person person = (Person)  intent.getSerializableExtra("person");
        if(person != null) {
            this.addPersonFormHelper.fillForm(person);
        }

    }

    private void loadButtonActions() {
        Button btnSave = (Button) findViewById(R.id.person_add_btn_save);
        Button btnCancel = (Button) findViewById(R.id.person_add_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AddPerson.this.addPersonFormHelper.isUpdate()){
                    AddPerson.this.updatePerson();
                }
                else{
                    AddPerson.this.savePerson();
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

    private void savePerson(){
        Person person = this.addPersonFormHelper.getPersonFromForm();
        AddPerson.this.personDAO.savePerson(person);
        Toast.makeText(AddPerson.this, "Person: "+person.getName()+" saved!", Toast.LENGTH_SHORT).show();
    }

    private void updatePerson(){
        Person person = this.addPersonFormHelper.getPersonToUpdate();
        this.personDAO.updatePerson(person);
        Toast.makeText(AddPerson.this, "Person: "+person.getName()+" updated!", Toast.LENGTH_SHORT).show();
    }

}
