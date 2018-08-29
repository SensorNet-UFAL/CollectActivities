package br.ufal.laccan.wylken.collectactivities.helpers;

import android.widget.EditText;

import br.ufal.laccan.wylken.collectactivities.AddActivity;
import br.ufal.laccan.wylken.collectactivities.R;
import br.ufal.laccan.wylken.collectactivities.model.ADL;

public class AddActvityFormHelper {

    private EditText edActivityName;
    private EditText edActivityDescription;
    private EditText edActivityTag;
    private ADL activity;

    public AddActvityFormHelper(AddActivity activity){
        this.edActivityName = (EditText) activity.findViewById(R.id.activity_add_edit_name);
        this.edActivityDescription = (EditText) activity.findViewById(R.id.activity_add_edit_description);
        this.edActivityTag = (EditText) activity.findViewById(R.id.activity_add_edit_tag);
    }

    public ADL getActivityFromForm(){
        ADL adl = new ADL();
        adl.setName(this.edActivityName.getText().toString());
        adl.setDescription(this.edActivityDescription.getText().toString());
        adl.setTag(Integer.parseInt(this.edActivityTag.getText().toString()));
        return adl;
    }

    public void fillForm(ADL activity){
        this.activity = activity;
        this.edActivityName.setText(activity.getName());
        this.edActivityDescription.setText(activity.getDescription());
        this.edActivityTag.setText(activity.getTag().toString());
    }

    public ADL getActivityToUpdate(){
        this.activity.setName(this.edActivityName.getText().toString());
        this.activity.setDescription(this.edActivityDescription.getText().toString());
        this.activity.setTag(Integer.parseInt(this.edActivityTag.getText().toString()));
        return this.activity;
    }

    public boolean isUpdate(){
        if(this.activity == null){
            return false;
        }
        else{
            return true;
        }
    }

}
