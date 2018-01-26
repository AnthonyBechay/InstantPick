package com.example.anthony.instantpick;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Signup extends Activity {

    TextView text;
    GridView grid2;
    LinearLayout lrecruiter, lstudent, llang;
    RadioButton rstudent;
    RadioButton rrecruiter;
    Button bsubmit;
    EditText efname, elname, eemail, emobile, ecompany, epassword;
    TextView test;
    ArrayList<String> langS;
    View view2;

    private static final String url="jdbc:mysql://"+LoginActivity.ip.c+"/instantpick";
    private static final String user="bechay";
    private static final String pass="bechay";

    ArrayAdapter<String> adapter;
    ArrayList<String> languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        lstudent = (LinearLayout) findViewById(R.id.lstudent);
        llang = (LinearLayout) findViewById(R.id.llang);
        lrecruiter = (LinearLayout) findViewById(R.id.lrecruiter);
        rstudent = (RadioButton) findViewById(R.id.rstudent);
        rrecruiter = (RadioButton) findViewById(R.id.rrecruiter);
        bsubmit = (Button) findViewById(R.id.bsubmit);
        efname = (EditText) findViewById(R.id.efname);
        elname = (EditText) findViewById(R.id.elname);
        eemail = (EditText) findViewById(R.id.eemail);
        emobile = (EditText) findViewById(R.id.emobile);
        grid2 = (GridView) findViewById(R.id.gridview2);
        test = (TextView) findViewById(R.id.tlname);
        ecompany = (EditText) findViewById(R.id.ecompany);
        epassword = (EditText) findViewById(R.id.epassword);
        text = (TextView) findViewById(R.id.text);
        view2 = (View) findViewById(R.id.view2);

        languages= new ArrayList<>();
        langS= new ArrayList<>();

        LV_languages objlv = new LV_languages();
        objlv.execute("");

        grid2.setNumColumns(2);
        grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             Object st = parent.getItemAtPosition(position);
                                             String selected = st.toString();
                                             if(langS.contains(selected)){
                                                 langS.remove(selected);
                                             }else{
                                                 langS.add(selected);
                                             }
                                             // Toast.makeText(getApplicationContext(),"Languages Selected " + langS,Toast.LENGTH_LONG).show();
                                         }
                                     }
        );
    }

    public class LV_languages extends AsyncTask<String ,String, String >{
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
                    String sql="Select * from language";
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        languages.add(rs.getString(2));
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
            adapter = new ArrayAdapter<>(Signup.this, android.R.layout.simple_list_item_multiple_choice, languages);
            grid2.setAdapter(adapter);
        }
    }

    public void onRecruiter(View v) {

        float s = bsubmit.getTranslationY();
        if (s == 2) {
            bsubmit.setY(1400);
            llang.setVisibility(LinearLayout.INVISIBLE);
            lstudent.setVisibility(LinearLayout.INVISIBLE);
            lrecruiter.setVisibility(LinearLayout.VISIBLE);
        } else {
            llang.setVisibility(LinearLayout.INVISIBLE);
            lstudent.setVisibility(LinearLayout.INVISIBLE);
            lrecruiter.setVisibility(LinearLayout.VISIBLE);
        }

    }

    public void onStudent(View V) {
        bsubmit.setTranslationY(2);
        lrecruiter.setVisibility(LinearLayout.INVISIBLE);
        llang.setVisibility(LinearLayout.VISIBLE);
        lstudent.setVisibility(LinearLayout.VISIBLE);
        view2.setVisibility(View.VISIBLE);
    }

    public void onSubmit(View V) throws InterruptedException {

        if(efname.length()==0 || elname.length()==0 || eemail.length()==0 || emobile.length()==0 || (rrecruiter.isChecked()==false && rstudent.isChecked()==false)){

            Toast.makeText(this,"Incomplete form!",Toast.LENGTH_LONG).show();

        }
        else
        if (rstudent.isChecked()) {
            class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
                JSONParser j = new JSONParser();
                JSONObject obj;
                String TempName = efname.getText().toString();
                String TempLname = elname.getText().toString();
                String TempEmail = eemail.getText().toString();
                String TempMobile = emobile.getText().toString();
                String TempPassword = epassword.getText().toString();

                @Override
                protected String doInBackground(String... params) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("efname", TempName));
                    nameValuePairs.add(new BasicNameValuePair("elname", TempLname));
                    nameValuePairs.add(new BasicNameValuePair("eemail", TempEmail));
                    nameValuePairs.add(new BasicNameValuePair("emobile", TempMobile));
                    nameValuePairs.add(new BasicNameValuePair("epassword", TempPassword));

                    obj = j.makeHttpRequest("http://"+LoginActivity.ip.c+"/instantpick/insertdata.php", "POST", nameValuePairs);
                    return obj.toString();
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.getBoolean("success")) {
                            Intent intent = new Intent(Signup.this, StudentActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                            efname.setText("");
                            elname.setText("");
                            eemail.setText("");
                            emobile.setText("");
                            epassword.setText("");
                        }
                    } catch (Exception e) {

                    }
                }
            }
            SendPostReqAsyncTask spr = new SendPostReqAsyncTask();
            spr.execute();

            for(int i=0;i<langS.size();i++){
                final String curr=langS.get(i);
                text.setText(curr);

                class SendPostReqAsyncTask1 extends AsyncTask<String, Void, String> {
                    JSONParser j1 = new JSONParser();
                    JSONObject obj1;

                    String temp= (String) text.getText();
                    String TempEmail = eemail.getText().toString();

                    @Override
                    protected String doInBackground(String... params) {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("text", temp));
                        nameValuePairs.add(new BasicNameValuePair("eemail", TempEmail));

                        obj1 = j1.makeHttpRequest("http://"+LoginActivity.ip.c+"/instantpick/insertlang.php", "POST", nameValuePairs);
                        return obj1.toString();
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        try {
                            JSONObject obj1 = new JSONObject(result);
                            if (obj1.getBoolean("success")) {

                            } else {
                                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                                efname.setText("");
                                elname.setText("");
                                eemail.setText("");
                                emobile.setText("");
                                epassword.setText("");
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                SendPostReqAsyncTask1 spr1 = new SendPostReqAsyncTask1();
                spr1.execute();
            }

        } else if (rrecruiter.isChecked()) {
            if(ecompany.length() == 0){
                Toast.makeText(getApplicationContext(), "Company name is missing!", Toast.LENGTH_LONG).show();

            }else {
                class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
                    JSONParser j = new JSONParser();
                    JSONObject obj;
                    String TempName = efname.getText().toString();
                    String TempLname = elname.getText().toString();
                    String TempEmail = eemail.getText().toString();
                    String TempMobile = emobile.getText().toString();
                    String TempCompany = ecompany.getText().toString();
                    String TempPassword = epassword.getText().toString();

                    @Override
                    protected String doInBackground(String... params) {
                        List<NameValuePair> nameValuePairs = new ArrayList<>();
                        nameValuePairs.add(new BasicNameValuePair("efname", TempName));
                        nameValuePairs.add(new BasicNameValuePair("elname", TempLname));
                        nameValuePairs.add(new BasicNameValuePair("eemail", TempEmail));
                        nameValuePairs.add(new BasicNameValuePair("emobile", TempMobile));
                        nameValuePairs.add(new BasicNameValuePair("ecompany", TempCompany));
                        nameValuePairs.add(new BasicNameValuePair("epassword", TempPassword));

                        obj = j.makeHttpRequest("http://" + LoginActivity.ip.c + "/instantpick/insertdatahr.php", "POST", nameValuePairs);
                        return obj.toString();
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getBoolean("success")) {
                                Intent intent = new Intent(Signup.this, RecruiterActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                                efname.setText("");
                                elname.setText("");
                                eemail.setText("");
                                emobile.setText("");
                                ecompany.setText("");
                                epassword.setText("");
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                SendPostReqAsyncTask spr = new SendPostReqAsyncTask();
                spr.execute();
            }
        }
    }
}
