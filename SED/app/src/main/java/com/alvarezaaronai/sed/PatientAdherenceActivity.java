package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alvarezaaronai.sed.utils.AdherenceXAxisFormatter;
import com.alvarezaaronai.sed.utils.AdherenceYAxisFormatter;
import com.alvarezaaronai.sed.utils.httprequest;
import com.alvarezaaronai.sed.utils.patient;
import com.alvarezaaronai.sed.utils.record;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientAdherenceActivity extends AppCompatActivity {

    private static final String TAG = "PatientAdherenceActivit";

    private List<Entry> mScheduledEntries;
    private List<Entry> mPatientEntries;
    private ScatterChart mScatterChart;
    private TextView mYearMonthHeader;

    /**
     * Going to be changing these values often.
     * Initializing them with the user's current year and month,
     * and changing them as the user clicks "Next" or "Previous"
     */
    private int currentMonth;
    private int currentYear;

    private String[] months = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};


    Map<String, Map<String, List<String>>> chartRecords =
            new HashMap<String, Map<String, List<String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_adherence);

        // Check if we got extra
        if(getIntent().hasExtra("patient_id")) {
            // If we get -1, something is obviously wrong.
            int patient_id = getIntent().getIntExtra("patient_id" , -1);
            Log.d(TAG, "onCreate: Patient Id Extra: " + patient_id);

            currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;;
            currentYear = Calendar.getInstance().get(Calendar.YEAR);;

            getPatientRecords(patient_id);
        }


        mYearMonthHeader = findViewById(R.id.year_month_header);
        String header = months[currentMonth - 1] + ", " + currentYear;
        mYearMonthHeader.setText(header);

        mScatterChart = findViewById(R.id.adherence_scatter_chart);

        // Start off empty
        mPatientEntries = new ArrayList<Entry>();

        createScatterChart();

    }

    private void createScatterChart() {
        mScheduledEntries = generateScheduledEntries();

        // Create ScatterDataSet using list of Entries
        ScatterDataSet scheduledDataSet = new ScatterDataSet(mScheduledEntries, "Scheduled");
        scheduledDataSet.setColor(R.color.grey);

        ScatterDataSet patientDataSet = new ScatterDataSet(mPatientEntries, "Actual");

        // Using the previous ScatterDataSet objects, add them to the list of IScatterDataSet objects
        List<IScatterDataSet> dataSetList = new ArrayList<IScatterDataSet>();

        dataSetList.add(scheduledDataSet);
        dataSetList.add(patientDataSet);

        // Finally, we can create our ScatterData
        ScatterData scatterData = new ScatterData(dataSetList);
        // Removes Text from data values (Ex: 18 for 7am)
        scatterData.setDrawValues(false);

        mScatterChart.setData(scatterData);

        configureScatterChart();

        // IMPORTANT!! Don't forget!
        mScatterChart.invalidate(); // Refresh
    }

    // Chart Configuration
    private void configureScatterChart() {
        // Configure ScatterChart
        mScatterChart.setTouchEnabled(false); // Disables interaction with graph
        mScatterChart.getDescription().setEnabled(false);

        // YAxis
        mScatterChart.getAxisRight().setEnabled(false);// Remove right labels
        YAxis yLeftAxis = mScatterChart.getAxisLeft();
        yLeftAxis.setAxisMaximum(25.0f);
        yLeftAxis.setAxisMinimum(0.0f);
        // Will allow us to see 24 labels. Which we'll use to display all 24 hours of a day
        yLeftAxis.setLabelCount(25);
        yLeftAxis.setValueFormatter(new AdherenceYAxisFormatter());

        // XAxis
        mScatterChart.getXAxis().setAxisMaximum(32.0f);
        mScatterChart.getXAxis().setAxisMinimum(0.0f);
        mScatterChart.getXAxis().setLabelCount(32);
        // Use the formatter to remove all 33 labels
        mScatterChart.getXAxis().setValueFormatter(new AdherenceXAxisFormatter());
    }

    private void getPatientRecords(int patient_id) {
        RecordRequestRunnable requestRunnable = new RecordRequestRunnable(patient_id);
        new Thread(requestRunnable).start();
    }


    /**
     * Returns a String representation of the month number.
     * Ex: 4 will return "04", 10 will return "10", etc.
     */
    private String getMonthNumberString(int monthNumber) {
        if(monthNumber < 10) {
            return "0" + monthNumber;
        }

        return "" + monthNumber;
    }

    // Generates the grey data point
    private List<Entry> generateScheduledEntries() {
        // Entry(float x, float y)
        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 1; i < 32; i++) {
            entries.add(new Entry((float) i, 18f));
        }

        return entries;
    }


    public void addRecordToMap(String date, String time) {
        String yearMonthKey = date.substring(0, 7);
        String dayKey = date.substring(8);

        if(chartRecords.containsKey(yearMonthKey)) {
            Map<String, List<String>> dayMap = chartRecords.get(yearMonthKey);

            if(dayMap.containsKey(dayKey)) {
                dayMap.get(dayKey).add(time);

            } else {
                List<String> times = new ArrayList<>();
                times.add(time);
                dayMap.put(dayKey, times);
            }

        } else {
            HashMap<String, List<String>> value = new HashMap<>();
            List<String> times = new ArrayList<>();
            times.add(time);

            value.put(dayKey, times);

            chartRecords.put(yearMonthKey, value);
        }

    }

//    private String getCurrentMonth() {
//        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//
//        // Adding a leading 0
//        if(month < 10) {
//            return "0" + month;
//        }
//
//        return "" + month;
//    }

//    private String getCurrentYear() {
//        return "" + Calendar.getInstance().get(Calendar.YEAR);
//    }

    private float convertTimeToFloat(String timeString) {
        // Change format from 10:30 to 10.30
        String newTimeString = timeString.replace(":", ".");
        // Convert String to Float
        float time = Float.parseFloat(newTimeString);
        // Not sure why adding .75 places it in the correct spot.
        float convertedTime = (float) 24.0f - time + .75f;

        return convertedTime;
    }

    /**
     * We're going to be re-using this function a lot.
     * 1) When user first visits PatientAdherenceActivity
     * 2) When user clicks "Next" or "Previous"
     *
     * yearMonthKey must be in the format "year-month"
     * Ex: 2020-04
     */
    public List<Entry> generatePatientEntries(String yearMonthKey) {
        Log.d(TAG, "generatePatientEntries:");
        List<Entry> entries = new ArrayList<Entry>();

        // Log.d(TAG, "generatePatientEntries: yearMonthKey " + yearMonthKey);
        // Check if there are any entries for this particular month
        if(chartRecords.containsKey(yearMonthKey)) {
            // Iterate through all keys in nest Map and use those
            // keys to get the time values.
            Map<String, List<String>> dayAndTimeRecords = chartRecords.get(yearMonthKey);
            for(String key : dayAndTimeRecords.keySet()) {
                List<String> times = dayAndTimeRecords.get(key);

                for(int i = 0; i < times.size(); i++) {
                    String timeString = times.get(i);
                    float hourOfDay = convertTimeToFloat(timeString);

                    /**
                     * key should be x-axis (day of month). But going to
                     * double check just in case
                     */
                    float dayOfMonth = Float.parseFloat(key);
                    Log.d(TAG, "generatePatientEntries: dayOfMonth: " + dayOfMonth);
                    entries.add(new Entry(dayOfMonth, hourOfDay));
                }
            }

        }

        return entries;
    }


    class RecordRequestRunnable implements Runnable {

        int patient_id;

        RecordRequestRunnable(int patient_id) {
            this.patient_id = patient_id;
        }

        @Override
        public void run() {
            try {
                patient patient = httprequest.requestPatient(patient_id);
                // Android Studio says this may be Null...
                List<record> records = patient.getRecords();

                Log.d(TAG, "getPatientRecords: records: ");
                for(int i = 0; i < records.size(); i++) {
                    Log.d(TAG, "run: " + records.get(i));
                    String date = records.get(i).getDate();
                    String time = records.get(i).getTime();
                    addRecordToMap(date, time);

                    /**
                     * Now we gotta create a String using currentYear and currentMonth
                     * (Ex: 2020-04) and using this String we iterate through our map and
                     * create a bunch of Entry objects and initialize mPatientRecords
                     */
                    String monthString = getMonthNumberString(currentMonth);
                     mPatientEntries = generatePatientEntries(currentYear + "-" + monthString);

                     createScatterChart();

                }

            } catch(ParseException ex) {
                System.out.println("ParseException");
                System.out.println(ex);
            }
        }
    }

}
