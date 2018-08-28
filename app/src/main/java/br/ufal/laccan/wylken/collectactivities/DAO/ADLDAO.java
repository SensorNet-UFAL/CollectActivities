package br.ufal.laccan.wylken.collectactivities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.ufal.laccan.wylken.collectactivities.model.ADL;

public class ADLDAO extends SQLiteOpenHelper{

    public ADLDAO(Context context){
        super(context, "Activities", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Activities (id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT, tag INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Activities";
        db.execSQL(sql);
        onCreate(db);
    }

    public void saveActivity(ADL adl){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("name", adl.getName());
        dados.put("description", adl.getDescription());
        dados.put("tag", adl.getTag());

        db.insert("Activities", null, dados);
    }

    public ArrayList<ADL> getADLs(){

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Activities;";
        Cursor c = db.rawQuery(sql,null);

        ArrayList<ADL> adls = new ArrayList<ADL>();
        while(c.moveToNext()){
            ADL adl = new ADL();
            adl.setId(c.getLong(c.getColumnIndex("id")));
            adl.setName(c.getString(c.getColumnIndex("name")));
            adl.setDescription(c.getString(c.getColumnIndex("description")));
            adl.setTag(c.getInt(c.getColumnIndex("tag")));
            adls.add(adl);
        }

        c.close();

        return adls;
    }
}
