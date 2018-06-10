package com.example.alexander.mysqlexampleproject;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Context thisContext;
    ListView myListView;
    TextView progressTextView;
    Map<String, Double> fruitsMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myListView);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        thisContext = this;

        progressTextView.setText("");

        Button btn = (Button) findViewById(R.id.getDataBtn);

        btn.setOnClickListener((v) -> {

            GetData retrieveData = new GetData();
            retrieveData.execute();
        });
    }

    private class GetData extends AsyncTask<String, String, String> {

        String msg = "";
        // JDBS driver name and database URl
        static final String JDBC_DRIVER = "org.postgresql.Driver";
        static final String DB_URL = "jdbc:postgresql://" + DbStrings.DATABASE_URL + "/" + DbStrings.DATABASE_NAME;

        @Override
        protected void onPreExecute() {
            progressTextView.setText("Connecting to database...");
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName(JDBC_DRIVER);
//                set the connection
                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);
//                creating query
                stmt = conn.createStatement();
                String sql = "SELECT * FROM fruits";

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");

                    fruitsMap.put(name, price);
                }

                msg = "Process complete";

            } catch (ClassNotFoundException | SQLException ex) {
                msg = "Connection failed...";
                ex.printStackTrace();
            } finally {

                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressTextView.setText(this.msg);

            if (fruitsMap.size() > 0) {
                ItemAdapter itemAdapter = new ItemAdapter(thisContext, fruitsMap);
                myListView.setAdapter(itemAdapter);
            }
        }
    }
} // End of MainActivity
