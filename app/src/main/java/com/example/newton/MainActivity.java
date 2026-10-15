package com.example.newton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt = findViewById(R.id.txt);
        double x = 1.5;     // Initial guess
        double n = 2;       // Find sqrt(2)
        String result = "";

        for(int i = 1; i <= 5; i++) {
            x = (x + n / x) / 2;
            result += "Iteration " + i + " = " + x + "\n";
        }

        txt.setText(result);
    }
}
