package br.ufal.laccan.wylken.collectactivities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.ufal.laccan.wylken.collectactivities.model.ADL;
import br.ufal.laccan.wylken.collectactivities.model.Person;

public class PersonDAO extends SQLiteOpenHelper{

    static String TABLE = "Persons";

    public PersonDAO(Context context){
        super(context, PersonDAO.TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+PersonDAO.TABLE+" (id INTEGER PRIMARY KEY, name TEXT NOT NULL, age INTEGER, gender TEXT,tag INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+PersonDAO.TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    private ContentValues getValues (Person person){
        ContentValues dados = new ContentValues();
        dados.put("name", person.getName());
        dados.put("age", person.getAge());
        dados.put("gender", person.getTag());
        dados.put("tag", person.getTag());

        return dados;
    }

    public ArrayList<Person> getPersons(){

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM "+PersonDAO.TABLE+";";
        Cursor c = db.rawQuery(sql,null);

        ArrayList<Person> persons = new ArrayList<Person>();
        while(c.moveToNext()){
            Person person = new Person();
            person.setId(c.getLong(c.getColumnIndex("id")));
            person.setName(c.getString(c.getColumnIndex("name")));
            person.setAge(c.getInt(c.getColumnIndex("age")));
            person.setGender(c.getString(c.getColumnIndex("gender")));
            person.setTag(c.getInt(c.getColumnIndex("tag")));
            persons.add(person);
        }

        c.close();

        return persons;
    }
}
