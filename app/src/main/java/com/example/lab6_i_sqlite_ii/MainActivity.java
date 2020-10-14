package com.example.lab6_i_sqlite_ii;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    EditText rollEditText, nameEditText, marksEditText;
    Button modifyBtn, addBtn, deleteBtn, viewBtn, viewAllBtn, showBtn;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollEditText = (EditText)findViewById(R.id.rollEditText);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        marksEditText = (EditText)findViewById(R.id.marksEditText);

        modifyBtn = (Button)findViewById(R.id.modifyBtn);
        addBtn = (Button)findViewById(R.id.addBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        viewAllBtn = (Button)findViewById(R.id.viewAllBtn);
        viewBtn = (Button)findViewById(R.id.viewBtn);
        showBtn = (Button)findViewById(R.id.showBtn);

        modifyBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        viewAllBtn.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
        showBtn.setOnClickListener(this);

        db=openOrCreateDatabase("student", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR, name VARCHAR, marks VARCHAR);");

    }


    public void onClick(View view){
        if (view == addBtn){
            if (rollEditText.getText().toString().trim().length()==0 ||
                    nameEditText.getText().toString().trim().length()==0 ||
                    marksEditText.getText().toString().trim().length()==0){
                showMessage("Error", "Please enter all values");
                return;
            }
            Log.d("error", ""+ rollEditText.getText());
            db.execSQL("INSERT INTO student VALUES ('"+rollEditText.getText() +"','"+nameEditText.getText()+"','"+marksEditText.getText()+"');");
            showMessage("Success", "Record added");
            clearText();

        }

        if (view == deleteBtn){
            if (rollEditText.getText().toString().trim().length()==0){
                showMessage("Error", "Please select roll number to be deleted.");
                return;
            }

            Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno="+rollEditText.getText()+"", null);

            if (c.moveToFirst()){
                db.execSQL("DELETE FROM student WHERE rollno="+rollEditText.getText()+"");
                showMessage("Success", "Record Deleted");
            }
            else {
                showMessage("Error", "Invalid Roll Num");
            }
            clearText();
        }

        if (view == modifyBtn){
            if (rollEditText.getText().toString().trim().length()==0){
                showMessage("Error", "Please select roll number to be modified.");
                return;
            }

            Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno="+rollEditText.getText()+"", null);
            if (c.moveToFirst()){
                db.execSQL("UPDATE student SET name="+nameEditText.getText()+", marks="+marksEditText.getText()+"WHERE rollno="+rollEditText.getText()+"");
                showMessage("Success", "Record Modified");
            }
            else {
                showMessage("Error", "Invalid Roll Num");
            }
            clearText();
        }

        if (view == viewAllBtn){
            Cursor c = db.rawQuery("SELECT * FROM student", null);
            if (c.getCount()==0){
                showMessage("Error", "No Record Found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()){
                buffer.append("Rollno: "+ c.getString(0)+"\n");
                buffer.append("Name: " + c.getString(1) +"\n");
                buffer.append("Marks: "+ c.getString(2) +"\n");
            }
            showMessage("Student Details", buffer.toString());
        }

        if (view == viewBtn){
            if (rollEditText.getText().toString().trim().length()==0){
                showMessage("Error", "Please select roll number to be viewed.");
                return;
            }

            Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno="+rollEditText.getText()+"", null);
            if (c.moveToFirst()){
                nameEditText.setText(c.getString(1));
                marksEditText.setText(c.getString(2));
            }
            else {
                showMessage("Error", "Invalid Roll Num");
                clearText();
            }
        }

        if (view == showBtn){
            showMessage("Student MGMT APP", "BCS");

        }
    }

    public void clearText(){
        rollEditText.setText("");
        nameEditText.setText("");
        marksEditText.setText("");
        rollEditText.requestFocus();
    }

    public void showMessage(String title, String msg){
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
}
