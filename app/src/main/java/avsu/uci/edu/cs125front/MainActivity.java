package avsu.uci.edu.cs125front;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        EditText timeText = findViewById(R.id.Time_editText);
        timeText.setText(currentDateTimeString);
        Button localSearchButton = findViewById(R.id.LocalSearchButton);



    }
}
