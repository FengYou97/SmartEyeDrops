package com.alvarezaaronai.sed.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class httprequest {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static physician requestPhysician (int physician_id) throws ParseException {

        try {

            String base_url = "https://od2h1ov7je.execute-api.us-west-1.amazonaws.com/prod/physician";

            URL url = new URL (base_url);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"physician\":" + physician_id +  "}";

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                Log.d("REQUESTOUTPUT", response.toString());

                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String physicianID = jsonResponse.getString("physician_id");
                    String patients = jsonResponse.getString("patients");
                    String lastname = jsonResponse.getString("last_name");
                    String image = jsonResponse.getString("physician_image");
                    String firstname = jsonResponse.getString("first_name");

                    List<patient> patientList = new ArrayList<>();

                    JSONArray arr = new JSONArray(patients);

                    for(int i=0; i<arr.length(); i++){
                        patient reqeustedPatient = requestPatient(Integer.parseInt(arr.getJSONObject(i).getString("patient_id")));
                        patientList.add(reqeustedPatient);
                    }

                    physician newPhysician = new physician(physicianID, patientList, lastname, image, firstname);

                    return newPhysician;


                } catch (JSONException e){
                    e.printStackTrace();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

//        List<record> records = new ArrayList<>();
//
//        String d = "yyyy-MM-dd";
//
//        String times = "10:52";
//
//        record temporaryRecords = new record(d, times);
//
//        records.add(temporaryRecords);
//
//        patient errorPatient = new patient("ERROR", "ERROR", "ERROR", "ERROR", "ERROR", "ERROR", records);
//        List<patient> errorList = new ArrayList<>();
//        errorList.add(errorPatient);

        return null;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static patient requestPatient(int patient_id) throws ParseException {
        try {

            String base_url = "https://od2h1ov7je.execute-api.us-west-1.amazonaws.com/prod/getpatientinfo";

            URL url = new URL(base_url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"patient\":" + patient_id + "}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                Log.d("REQUESTOUTPUT", response.toString());

                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String patientID = jsonResponse.getString("patient_id");
                    String email = jsonResponse.getString("email");
                    String first_name = jsonResponse.getString("first_name");
                    String image = jsonResponse.getString("image");
                    String last_name = jsonResponse.getString("last_name");
                    String physician_id = jsonResponse.getString("physician_id");

                    List<record> records = new ArrayList<>();

//                    System.out.println(jsonResponse.getJSONObject("records").getJSONObject("2020-03-23").getJSONArray("time_applied").getString(0));

                    JSONObject arr = jsonResponse.getJSONObject("records");
                    Iterator<String> keys = arr.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        if (arr.get(key) instanceof JSONObject) {
                            JSONArray arr2 = arr.getJSONObject(key).getJSONArray("time_applied");
                            for(int i = 0; i < arr2.length(); i++){
                                String time = arr2.getString(i);
                                record newRecord = new record(key, time);
                                records.add(newRecord);
                            }
                        }
                    }

                    patient newPatient = new patient(last_name, image, first_name, patientID, patientID, physician_id, records);

                    return newPatient;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String d = "yyyy-MM-dd";
//
//        List<record> records = new ArrayList<>();
//
//        String times = "10:52";
//
//        record temporaryRecords = new record(d, times);
//
//        records.add(temporaryRecords);
//
//        return(new patient("ERROR","ERROR","ERROR","ERROR","ERROR","ERROR",records));

        return null;

    }
}
