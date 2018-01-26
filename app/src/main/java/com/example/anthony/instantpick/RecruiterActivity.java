package com.example.anthony.instantpick;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RecruiterActivity extends ActionBarActivity {
    /*private static final String languages[] = {"HTML", "CSS","JS",".net","PHP","C++","Android","XML","Java","PL/SQL","Oracle","UML"};*/
    GridView grid;
    GridView grid3;
    TextView temp;

    ArrayList<String> langHR;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> languages = new ArrayList<>();

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    Button b;
    private static final String url = "jdbc:mysql://"+LoginActivity.ip.c+"/instantpick";
    private static final String user = "bechay";
    private static final String pass = "bechay";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruiteractivity);
        b = (Button) findViewById(R.id.bshow);
        langHR = new ArrayList<>();
        temp = (TextView) findViewById(R.id.textView3);
        grid = (GridView) findViewById(R.id.gridview);
        grid.setNumColumns(3);
        MyTask objsend2 = new MyTask();
        objsend2.execute("");
        b = (Button) findViewById(R.id.bshow);
        grid3 = (GridView) findViewById(R.id.gridview2);
        grid3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object st = parent.getItemAtPosition(position);
                String selectedstd = st.toString();

                String[] sele = selectedstd.split("\n   ");
                final String maili = sele[1];
                final String mo = sele[2];
                Intent intent = null;
                final Intent[] chooser = { null };

                new AlertDialog.Builder(RecruiterActivity.this)
                        .setTitle("Selected: " + temp.getText().toString())
                        .setMessage(sele[0])
                        .setNeutralButton("Cancel", null)
                        .setNegativeButton("Call",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int which) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + mo));
                                        if (ActivityCompat.checkSelfPermission(RecruiterActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(RecruiterActivity.this, "Missing Permission !!", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        startActivity(intent);
                                    }})
                        .setPositiveButton("Send Email",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int which) {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setData(Uri.parse("mailto:"));
                                        String [] to={maili};
                                        intent.putExtra(Intent.EXTRA_EMAIL,to);
                                        intent.setType("message/rfc822");
                                        chooser[0] = Intent.createChooser(intent,"Send Email");
                                        startActivity(chooser[0]);
                                    }})
                        .show();
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object st = parent.getItemAtPosition(position);
                String selected = st.toString();
                //Toast.makeText(getApplicationContext(),"Languages Selected " + selected,Toast.LENGTH_LONG).show();
                temp.setText(selected);

            }
        });

    }


    public class MyTask1 extends AsyncTask<String ,String, String > {
        String message;
        String selected=temp.getText().toString() ;

        @Override
        protected String doInBackground(String... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con= DriverManager.getConnection(url,user,pass);


                if(con == null){
                    message="Connection goes wrong";
                }else{
                    Statement st=con.createStatement();
                    String sql="select FNAME_STUDENT,LNAME_STUDENT ,EMAIL_STUDENT,MOBILE_STUDENT FROM student,knowledge,language where ID_STUDENT=FK_STUDENT AND ID_LANG=FK_LANG AND NAME='"+selected+"'";
                    ResultSet rs = st.executeQuery(sql);
                    items.clear();

                    while(rs.next()){

                        items.add("▪️"+ rs.getString("FNAME_STUDENT") + " " + rs.getString("LNAME_STUDENT") + "\n   "+ rs.getString("EMAIL_STUDENT")+ "\n   " + rs.getString("MOBILE_STUDENT"));
                    }
                    rs.close();
                }
                con.close();
            } catch (Exception e) {
                message = e.getMessage();
                e.printStackTrace();
            }
            return message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            adapter = new ArrayAdapter<>(RecruiterActivity.this, android.R.layout.simple_list_item_1, items);
            grid3.setAdapter(adapter);
        }
    }

    public class MyTask extends AsyncTask<String ,String, String > {
        String message;

        @Override
        protected String doInBackground(String... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con= DriverManager.getConnection(url,user,pass);


                if(con == null){
                    message="Connection goes wrong";
                }else{
                    Statement st=con.createStatement();
                    String sql="Select NAME from language";
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){

                        languages.add(rs.getString(1));
                    }
                    rs.close();
                }
                con.close();
            } catch (Exception e) {
                message = e.getMessage();
                e.printStackTrace();
            }
            return message;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter1 = new ArrayAdapter<>(RecruiterActivity.this, android.R.layout.simple_list_item_single_choice, languages);
            grid.setAdapter(adapter1);
        }
    }

    public void onShow(View view) {

        MyTask1 objsend=new MyTask1();
        objsend.execute("");

    }
}
