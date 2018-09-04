package br.ufal.laccan.wylken.collectactivities.commons;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.ufal.laccan.wylken.collectactivities.DAO.ADLDAO;
import br.ufal.laccan.wylken.collectactivities.DAO.ActivityRecordDAO;
import br.ufal.laccan.wylken.collectactivities.DAO.PersonDAO;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class Files {

    private Context context;

    public Files(Context context){
        this.context = context;
    }

    public boolean exportDB(){

        File externalStorageDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+"ActivityRecords/");
        File dataDirectory = Environment.getDataDirectory();
        String currentDBPath = "/data/" + this.context.getApplicationContext().getApplicationInfo().packageName + "/databases/";

        FileChannel source = null;
        FileChannel destination = null;

        File currentDBActivityRecord = new File(dataDirectory, currentDBPath + ActivityRecordDAO.TABLE);
        File currentDBADL = new File(dataDirectory, currentDBPath + ADLDAO.TABLE);
        File currentDBPerson = new File(dataDirectory, currentDBPath + PersonDAO.TABLE);


        File backupDBActivityRecord = new File(externalStorageDirectory, ActivityRecordDAO.TABLE+".sqlite");
        File backupDBADL = new File(externalStorageDirectory, ADLDAO.TABLE+".sqlite");
        File backupDBPerson = new File(externalStorageDirectory, PersonDAO.TABLE+".sqlite");

        if (!externalStorageDirectory.exists()) {
            externalStorageDirectory.mkdirs();
        }


        try {

            //Save ActivityRecords
            source = new FileInputStream(currentDBActivityRecord).getChannel();
            destination = new FileOutputStream(backupDBActivityRecord).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            //Save ADL
            source = new FileInputStream(currentDBADL).getChannel();
            destination = new FileOutputStream(backupDBADL).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            //Save Person
            source = new FileInputStream(currentDBPerson).getChannel();
            destination = new FileOutputStream(backupDBPerson).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();

            Toast.makeText(this.context, "Data exported in: "+externalStorageDirectory.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error: can't save the data.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
