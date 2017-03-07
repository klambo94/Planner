package kendraslaptop.example.com.planner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_EVENT = 100;
    private static final int REQUEST_SETTINGS = 200;
    private List<Integer> textViewIds = new ArrayList<Integer>();
    RelativeLayout layout;
    private int eventsAdded = 0;
    private int prevTextViewId = 0;
    //event keys
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String LOCATION = "LOCATION";

    //String constant
    private static final String EVENTID = "event_id_";

    // Font chooser keys
    private static final String SIZE = "SIZE";
    private static final String FONT_FACE = "FONT FACE";
    private static final String FONT_STYLE = "FONT STYLE";
    private static final String COLOR_RED = "COLOR RED";
    private static final String COLOR_GREEN = "COLOR GREEN";
    private static final String COLOR_BLUE = "COLOR BLUE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBarId);
        setSupportActionBar(myToolbar);
        layout = (RelativeLayout)findViewById(R.id.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_buttons, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent("RETRIVE_FONT");
                startActivityForResult(settingsIntent, REQUEST_SETTINGS);
                return true;
            case R.id.addEventBtn:
                Intent eventIntent = new Intent(this, AddEventActivity.class);
                startActivityForResult(eventIntent, REQUEST_EVENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_EVENT:
                    addEventToDateSpecified(data);
                    break;
                case REQUEST_SETTINGS:
                    changeWidgetText(data);
                    break;
                default:
                    break;
            }
        }
    }
    FontCharacteristics characteristics;
    private void changeWidgetText(Intent data) {
        String[] fontKeys = {SIZE, COLOR_BLUE, COLOR_GREEN, COLOR_RED, FONT_FACE, FONT_STYLE};
        if(data != null) {
            Bundle extras = data.getExtras();
            if(extras != null) {
                if(extrasContainsKeys(extras, fontKeys)){
                    characteristics = new FontCharacteristics(extras);
                    int size = characteristics.getSize();
                    int colorBlue = characteristics.getColorBlue();
                    int colorGreen = characteristics.getColorGreen();
                    int colorRed = characteristics.getColorRed();
                    Typeface face = characteristics.getTypeFace();
                    int typeStyle = characteristics.getTypeStyle();
                    for(Integer id : textViewIds) {
                        TextView text = (TextView) findViewById(id);
                        text.setTextColor(Color.rgb(colorRed, colorGreen, colorBlue));
                        text.setTextSize(size);
                        text.setTypeface(face, typeStyle);
                    }
                }
            }
        }
    }

    private boolean extrasContainsKeys(Bundle extras, String[] keys) {
        for(String key : keys){
            if(!extras.containsKey(key)){
                return false;
            }
        }
        return true;
    }

    private void addEventToDateSpecified(Intent data) {
        String eventLocation = "";
        String eventDate = "";
        String eventTime = "";
        String eventName = "";
        if(data != null) {
            Bundle extras = data.getExtras();

            if(extras != null) {
                if(extras.containsKey(NAME)){
                    eventName = extras.get(NAME).toString();
                }

                if(extras.containsKey(TIME)){
                    eventTime = extras.get(TIME).toString();
                }

                if(extras.containsKey(DATE)){
                    eventDate = extras.get(DATE).toString();
                }

                if(extras.containsKey(LOCATION)){
                    eventLocation= extras.get(LOCATION).toString();
                }

                if(!eventName.isEmpty() && !eventDate.isEmpty() && !eventTime.isEmpty()){
                    final TextView textView = new TextView(this);
                    String eventText = getEventText(eventName, eventDate, eventTime, eventLocation);
                    textView.setText(eventText);
                    textView.setId(++eventsAdded);
                    if(characteristics != null){
                        int size = characteristics.getSize();
                        int colorBlue = characteristics.getColorBlue();
                        int colorGreen = characteristics.getColorGreen();
                        int colorRed = characteristics.getColorRed();
                        Typeface face = characteristics.getTypeFace();
                        int typeStyle = characteristics.getTypeStyle();
                        textView.setTextColor(Color.rgb(colorRed, colorGreen, colorBlue));
                        textView.setTextSize(size);
                        textView.setTypeface(face, typeStyle);
                    }

                    final RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if(prevTextViewId == 0){
                        prevTextViewId = findViewById(R.id.calendarView).getId();
                    }
                    params.addRule(RelativeLayout.BELOW, prevTextViewId);
                    textView.setLayoutParams(params);


                    layout.addView(textView, params);
                    textViewIds.add(eventsAdded);
                    prevTextViewId = eventsAdded;
                }
            }
        }
    }

    private String getEventText(String eventName, String eventDate, String eventTime, String eventLocation) {
        String eventText = eventName + " " + eventDate + " " + eventTime;

        eventText = (!eventLocation.isEmpty() && eventLocation != null)
                ? eventText + " " + eventLocation : eventText;

        return eventText;
    }


}
