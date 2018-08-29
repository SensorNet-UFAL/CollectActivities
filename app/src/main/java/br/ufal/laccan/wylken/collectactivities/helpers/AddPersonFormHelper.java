package br.ufal.laccan.wylken.collectactivities.helpers;


import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import br.ufal.laccan.wylken.collectactivities.AddPerson;
import br.ufal.laccan.wylken.collectactivities.R;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class AddPersonFormHelper {

    private EditText edPersonName;
    private EditText edPersonAge;
    private EditText edPersonTag;
    private RadioGroup rdPersonGender;
    private RadioButton rdPersonGenderMale;
    private RadioButton rdPersonGenderFemale;
    private AddPerson context;
    private Person person;

    public AddPersonFormHelper(AddPerson context){
        this.context = context;
        this.edPersonName = context.findViewById(R.id.person_add_edit_name);
        this.edPersonAge = context.findViewById(R.id.person_add_edit_age);
        this.edPersonTag = context.findViewById(R.id.person_add_edit_tag);
        this.rdPersonGender = context.findViewById(R.id.person_add_radiogroup_gender);
        this.rdPersonGenderFemale = context.findViewById(R.id.person_add_radiobutton_female);
        this.rdPersonGenderMale = context.findViewById(R.id.person_add_radiobutton_male);
    }

    public Person getPersonFromForm(){
        Person person = new Person();
        RadioButton genderSelected = (RadioButton) this.context.findViewById(this.rdPersonGender.getCheckedRadioButtonId());

        person.setName(this.edPersonName.getText().toString());
        person.setAge(Integer.parseInt(this.edPersonAge.getText().toString()));
        person.setTag(Integer.parseInt(this.edPersonTag.getText().toString()));
        person.setGender(genderSelected.getText().toString());

        return person;
    }

    private void setRadioButtonGender(Person person){

        if(this.rdPersonGenderFemale.getText().toString().equals(person.getGender())){
            this.rdPersonGenderFemale.setChecked(true);
            this.rdPersonGenderMale.setChecked(false);
        }
        if(this.rdPersonGenderMale.getText().toString().equals(person.getGender())){
            this.rdPersonGenderFemale.setChecked(false);
            this.rdPersonGenderMale.setChecked(true);
        }

    }

    public void fillForm(Person person){
        this.person = person;
        this.edPersonName.setText(person.getName());
        this.edPersonAge.setText(person.getAge().toString());
        this.edPersonTag.setText(person.getTag().toString());
        this.setRadioButtonGender(person);
    }

    public Person getPersonToUpdate(){

        RadioButton genderSelected = (RadioButton) this.context.findViewById(this.rdPersonGender.getCheckedRadioButtonId());

        this.person.setName(this.edPersonName.getText().toString());
        this.person.setAge(Integer.parseInt(this.edPersonAge.getText().toString()));
        this.person.setTag(Integer.parseInt(this.edPersonTag.getText().toString()));
        this.person.setGender(genderSelected.getText().toString());

        return this.person;
    }

    public boolean isUpdate(){
        if(this.person == null){
            return false;
        }
        else{
            return true;
        }
    }

}
