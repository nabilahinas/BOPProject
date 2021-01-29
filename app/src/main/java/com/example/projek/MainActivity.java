package com.example.projek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
DatabaseHelper dbcenter;
Button logout;
String[] daftar;
ListView ListView01;
protected Cursor cursor;
public static MainActivity ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button2);
        logout = (Button)findViewById(R.id.btn_logout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent inte = new Intent(MainActivity.this, BuatBiodata.class);
                startActivity(inte);
            }
        });
        ma = this;
        dbcenter = new DatabaseHelper(this);
        RefreshList();
    }
    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata", null);
        daftar = new String[cursor.getCount()];
//        cursor.moveToFirst();

        for (int cc=0; cc< cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1);
        }
        ListView01 = (ListView)findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                                                  final String selection = daftar[arg2]; //.getItemAtPosition(arg2).toString();
                                                  final CharSequence[] dialogitem = {"Lihat Biodata", "Update Biodata", "Hapus Biodata"};

                                                  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                  builder.setTitle("Pilihan");
                                                  builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                                      public void onClick(DialogInterface dialog, int item) {
                                                          switch(item){
                                                              case 0 :
                                                                  Intent i = new Intent(getApplicationContext(), LihatBiodata.class);
                                                                  i.putExtra("nama", selection);
                                                                  startActivity(i);
                                                                  break;
                                                              case 1 :
                                                                  Intent in = new Intent(getApplicationContext(), UpdateBiodata.class);
                                                                  in.putExtra("nama", selection);
                                                                  startActivity(in);
                                                                  break;
                                                              case 2 :
                                                                  SQLiteDatabase db = dbcenter.getWritableDatabase();
                                                                  db.execSQL("delete from biodata where nama = '"+selection+"'");
                                                                  RefreshList();
                                                                  break;
                                                          }
                                                      }
                                                  });
                                                  builder.create().show();

}});
((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();


        Boolean checkSession = dbcenter.checkSession("ada");
        if (checkSession == false) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        // logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean updtSession = dbcenter.upgradeSession("kosong", 1);
                if (updtSession == true) {
                    Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }

        });
    }
}