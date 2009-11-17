package net.johnpwood.android.standuptimer;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StandupTimer extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeGUIElements();
    }

    public void onClick(View v) {
        Intent i = new Intent(this, Timer.class);

        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        i.putExtra("meetingLength", s.getSelectedItem().toString());

        Object o = findViewById(R.id.num_participants);
        Class<? extends Object> c = o.getClass();
        int numParticipants = 0;
        try {
            Method m = c.getMethod("getCurrent");
            numParticipants = (Integer) m.invoke(o, null);
        } catch (Exception e) {
            Log.e("Failed to get the number of partipipants: ", e.getMessage());
            throw new RuntimeException(e);
        }
        i.putExtra("numParticipants", numParticipants);

        startActivity(i);
    }

    private void initializeGUIElements() {
        initializeNumberOfParticipantsPicker();
        initializeMeetingLengthSpinner();
        initializeStartButton();
    }

    private void initializeStartButton() {
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    private void initializeMeetingLengthSpinner() {
        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.meeting_lengths,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private void initializeNumberOfParticipantsPicker() {
        Object o = findViewById(R.id.num_participants);
        Class<? extends Object> c = o.getClass();
        try {
            Method m = c.getMethod("setRange", int.class, int.class);
            m.invoke(o, 0, 20);
        } catch (Exception e) {
            Log.e("Failed to set the range of the participants number picker: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
