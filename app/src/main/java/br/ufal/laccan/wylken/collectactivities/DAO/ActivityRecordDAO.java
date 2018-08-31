package br.ufal.laccan.wylken.collectactivities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.ufal.laccan.wylken.collectactivities.model.ActivityRecord;


public class ActivityRecordDAO extends SQLiteOpenHelper{

    static String TABLE = "ActivityRecords";
    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

    public ActivityRecordDAO(Context context){
        super(context, ActivityRecordDAO.TABLE, null, 1);
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

        return false;
    }

}
