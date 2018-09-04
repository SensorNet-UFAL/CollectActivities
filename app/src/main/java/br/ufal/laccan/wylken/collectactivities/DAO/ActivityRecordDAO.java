package br.ufal.laccan.wylken.collectactivities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.ufal.laccan.wylken.collectactivities.MainActivity;
import br.ufal.laccan.wylken.collectactivities.model.ActivityRecord;


public class ActivityRecordDAO extends SQLiteOpenHelper{

    static public String TABLE = "ActivityRecords";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:MM:SS.SSS");
    private Context context;

    public ActivityRecordDAO(Context context){
        super(context, ActivityRecordDAO.TABLE, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ActivityRecordDAO.TABLE+" (id INTEGER PRIMARY KEY, x FLOAT, y FLOAT, z FLOAT, activity_tag TINYINT, person_tag TINYINT, sensor_type TINYINT, time DATETIME);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+ActivityRecordDAO.TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    private ContentValues getValues (ActivityRecord activityRecord){
        ContentValues dados = new ContentValues();
        dados.put("x", activityRecord.getX());
        dados.put("y", activityRecord.getY());
        dados.put("z", activityRecord.getZ());
        dados.put("activity_tag", activityRecord.getActivity_tag());
        dados.put("person_tag", activityRecord.getPerson_tag());
        dados.put("sensor_type", activityRecord.getPerson_tag());
        dados.put("time", sdf.format(activityRecord.getTime()));
        return dados;
    }

    public boolean saveActivityRecord(ArrayList<ActivityRecord> activitiRecordList){

        SQLiteDatabase db = getWritableDatabase();

        for(ActivityRecord activityRecord : activitiRecordList){
            ContentValues dados = this.getValues(activityRecord);
            db.insert(ActivityRecordDAO.TABLE, null, dados);
        }

        return true;
    }



}
