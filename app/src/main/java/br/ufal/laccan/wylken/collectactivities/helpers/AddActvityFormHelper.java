package br.ufal.laccan.wylken.collectactivities.helpers;

import android.widget.EditText;

import br.ufal.laccan.wylken.collectactivities.AddActivity;
import br.ufal.laccan.wylken.collectactivities.R;
import br.ufal.laccan.wylken.collectactivities.model.ADL;

public class AddActvityFormHelper {

    private EditText edActivityName;
    private EditText edActivityDescription;
    private EditText edActivityTag;

    public AddActvityFormHelper(AddActivity activity){
        this.edActivityName = (EditText) activity.findViewById(R.id.activity_add_edit_name);
        this.edActivityDescription = (EditText) activity.findViewById(R.id.activity_add_edit_description);
        this.edActivityTag = (EditText) activity.findViewById(R.id.activity_add_edit_tag);
    }

    public ADL getActivity(){
        ADL adl = new ADL();
        adl.setName(this.edActivityName.getText().toString());
        adl.setDescription(this.edActivityDescription.getText().toString());
        adl.setTag(Integer.parseInt(this.edActivityTag.getText().toString()));
        return adl;
    }
}
