package es.esy.AlarmApp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ListView listview;

    DigitalClock nowTime;
    TextView lastAlarm;
    TextView totalActif;
    TextView totalSave;
    Button stopAlarm;

    AdapterAlarme alarmArrayAdapter;
    ArrayList<Alarme> alarmArray = new ArrayList<Alarme>();

    Calendar c;
    int hour;
    int minute;
    int tSave=0;

    String hZero;
    String mZero;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleHeureFormat;
    String formatDate;
    String formatHeure;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private static final int Time_id = 1;
    int nAlarm;
    FloatingActionButton fab;
    Toolbar toolbar;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Parcelable state = listAlarm.onSaveInstanceState();
        savedInstanceState.putInt ("click_count", tSave);
//save array list
//savedInstanceState.putStringArrayList("ITEM_ID_LIST", alarmArray);
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//Get The Time Instance in , use to set TimePicker Dialog by Time Now
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

//Get All View in XML
        totalActif = (TextView) findViewById(R.id.totalActif);
        totalSave = (TextView) findViewById(R.id.totalSave);
        lastAlarm = (TextView) findViewById(R.id.lastAlarm);
        stopAlarm = (Button) findViewById(R.id.stopAlarm);
        stopAlarm.setVisibility(View.GONE);
        fab = (FloatingActionButton) findViewById(R.id.fab);

// add default Alarm Time with Info in array, default date add is set.
        alarmArray.add(new Alarme("06:00", "11-02-2017", "ON"));
        alarmArray.size();
        totalActif.setText(alarmArray.size()+"Alarmes Actifs");
        totalSave.setText(alarmArray.size()+"Alarmes Enregistrer");

// set the array adapter to use the above array list and tell the listview to set as the adapter
// our custom adapter
        alarmArrayAdapter = new AdapterAlarme(MainActivity.this, R.layout.list_alarm, alarmArray);
        listview= (ListView) findViewById(R.id.listAlarm);
        listview.setItemsCanFocus(false);
        listview.setAdapter(alarmArrayAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                Toast.makeText(MainActivity.this,
                        "List Item Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Arret en cour.....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                stopAlarm.setVisibility(View.GONE);
            }
        });

// Set onClick on Floatting Button to open Dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Time_id);

            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timerMethod();

    }
//Create Dialog for TimePicker or DatePicker
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Time_id:
// Open the timepicker dialog
                return new TimePickerDialog(MainActivity.this, time_listener, hour,
                        minute, false);
        }
        return null;
    }
//THe TimePicker Dialog Action ,
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            if(hour<10){
                hZero= 0+String.valueOf(hour);
            }else{
                hZero= String.valueOf(hour);
            }
            if(minute<10){
                mZero= 0+String.valueOf(minute);
            }else{
                mZero= String.valueOf(minute);
            }
// store the data in one string and set it to text
            String time1 = hZero + ":" + mZero;
//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            formatDate = simpleDateFormat.format(new Date());
            alarmArray.add(new Alarme(time1,formatDate,"ON"));
            alarmArray.size();
            lastAlarm.setText(time1);
            totalActif.setText(alarmArray.size()+"Alarmes Actifs");
            totalSave.setText(alarmArray.size()+"Alarmes Enregistrer");
            alarmArrayAdapter.notifyDataSetChanged();
            //Snackbar.make(view, "Alarme Enregistrer: "+ time1, Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();
        }
    };

//Use Timer to reload app every 30 milliseconde
//
    Timer reload = new Timer();
    void timerMethod()
    {
        reload.schedule(new TimerTask() {
            public void run() {
                Log.d("timer", "timer ");
// Get the calander
//Boolean t=alarmArray.contains("itemsValueInArray);

//Get HHours and Minutes
                simpleHeureFormat = new SimpleDateFormat("HH:mm");
                formatHeure = simpleHeureFormat.format(new Date());
//use Date Instance to get Hours , Minutes and seconds
                int heu =new Date().getHours();
                int min =new Date().getMinutes();
                int sec =new Date().getSeconds();
                int result=1;

//Conversion en millisegonde
                if(heu!=0){
                    heu=heu*60*60*1000;
                }else{
                    heu=0;
                }
                if(min!=0){
                    min = min*60*1000;
                }else{
                    min=0;
                }
                if(sec!=0){
                    sec = sec * 1000;
                }else{
                    sec=0;
                }
                result = heu+min+sec;
if(stopAlarm.getVisibility() == View.GONE){
// Loop to compare is Time Now is equals to Time in Array (Time Alarm Set by user)
    nAlarm =0;
    for(int i = 0; i < alarmArray.size(); i++) {
        if (alarmArray.get(i).getTime().equals(formatHeure) && alarmArray.get(i).getEtatAdd().equals("ON")){
//alarm found
            //System.out.println( "Touver youpi: "+reload);
            alarmArray.get(i).getTime();
            alarmArray.get(i).getEtatAdd();
            //System.out.println( "Touver youpi: "+formatHeure+"\n Time Alarm: "+alarmArray.get(i).getTime()+"\n Time Etat: "+alarmArray.get(i).getEtatAdd());
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//can use to repeat Alarm every instance set
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10000, pendingIntent);
//service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
            //or
//use to set alarm one time
// alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + result , pendingIntent);
            nAlarm=1;
        }else{
//If not Found
            System.out.println( "Not found: "+formatHeure+"\n Time Alarm: "+alarmArray.get(i).getTime()+"\n Time Etat: "+alarmArray.get(i).getEtatAdd());

            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    if(nAlarm==1){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopAlarm.setVisibility(View.VISIBLE);
            }
        });
    }
}
            }
        }, 10000, 10000);
//1minutes = 60000 milliseconde
//0,0005minutes = 30 milliseconde
    }
}

