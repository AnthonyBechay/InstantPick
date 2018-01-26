package com.example.anthony.instantpick;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentActivity extends Activity {
    View redLine;
    GridView list;
    ArrayList<String> companies = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    // private static final String languages[] = {"HTML", "CSS","JS",".net","PHP","C++","Android","XML","Java","HTML", "CSS","JS",".net","PHP","C++","Android","XML","Java","HTML", "CSS","JS",".net","PHP","C++","Android","XML","Java","HTML", "CSS","JS",".net","PHP","C++","Android","XML","Java","PL/SQL","Oracle","UML"};
    private static final String url = "jdbc:mysql://"+LoginActivity.ip.c+"/instantpick";
    private static final String user = "bechay";
    private static final String pass = "bechay";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentactivity);

        MyTask3 showlist=new MyTask3();
        showlist.execute("");

        list = (GridView) findViewById(R.id.grid5);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object st = parent.getItemAtPosition(position);
                String selectedstd = st.toString();

                String[] sele = selectedstd.split("\n    ");
                final String maili = sele[1];
                final String comp = sele[2];

                Intent intent = null;
                final Intent[] chooser = { null };
                new AlertDialog.Builder(StudentActivity.this)
                        .setTitle("Company info")
                        .setMessage(comp)
                        .setNeutralButton("Cancel", null)
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
    }


    public class MyTask3 extends AsyncTask<String ,String, String > {
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
                    String sql="select FNAME_HR,LNAME_HR,EMAIL_HR,COMPANY FROM hr";
                    ResultSet rs = st.executeQuery(sql);

                    while(rs.next()){

                        companies.add("◾"+rs.getString("FNAME_HR") + " " + rs.getString("LNAME_HR") + "\n    "+ rs.getString("EMAIL_HR")+ "\n    " + rs.getString("COMPANY"));
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
            adapter = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_list_item_1, companies);
            list.setAdapter(adapter);
        }
    }




}
