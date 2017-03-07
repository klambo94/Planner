package kendraslaptop.example.com.planner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Kendra's Laptop on 3/4/2017.
 */

public class AddEventActivity extends AppCompatActivity{
    private Intent incomingIntent;
    private Intent outgoingIntent;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private TimePicker timePicker1;
    private TextView time;
    private String format = "";
    private int hour, min;


    Editable nameText;
    Editable locationText;
    String dateText;
    String timeText;

    //event keys
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String LOCATION = "LOCATION";

    //Settings keys

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.addEventToolBar);
        setSupportActionBar(myToolbar);
        incomingIntent = getIntent();
        outgoingIntent = new Intent();

        dateView = (Button) findViewById(R.id.dateBtn);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        time = (TextView) findViewById(R.id.timeBtn);
        calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
    }

    public void setTime(View view) {
        showDialog(1000);
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ")
                .append(min).append(" ").append(format));
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } else if(id == 1000) {
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_button_event, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //TODO: This is where we would call font chooser
                return true;
            case R.id.action_save:
                setEventDetails();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearNameValues(View view) {
        EditText name = (EditText) findViewById(R.id.nameId);
        name.setText("");
    }

    public void clearLocationValues(View view) {
        EditText location = (EditText) findViewById(R.id.locationId);
        location.setText("");
    }

    private void setEventDetails() {
        EditText name = (EditText) findViewById(R.id.nameId);
        nameText = name.getText();

        Button time = (Button) findViewById(R.id.timeBtn);
        timeText = time.getText().toString();

        Button date = (Button) findViewById(R.id.dateBtn);
        dateText = date.getText().toString();

        EditText location = (EditText) findViewById(R.id.locationId);
        locationText = location.getText();
    }

    @Override
    public void finish() {
        outgoingIntent.putExtra(NAME, nameText);
        outgoingIntent.putExtra(TIME, timeText);
        outgoingIntent.putExtra(DATE, dateText);
        outgoingIntent.putExtra(LOCATION, locationText);
        setResult(RESULT_OK, outgoingIntent);
        super.finish();
    }
}
