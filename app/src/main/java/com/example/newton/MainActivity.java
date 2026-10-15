package com.example.newton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt = findViewById(R.id.txt);

        double a = 2.0;    // Number under square root
        double b = 10.0;   // Numerator

        // Initial estimate of 1/sqrt(a)
        double y = 0.5;

        double actual = b / Math.sqrt(a);

        StringBuilder result = new StringBuilder();

        result.append("Division by Square Root using Newton-Raphson\n\n");
        result.append("a = ").append(a).append("\n");
        result.append("b = ").append(b).append("\n\n");

        result.append("MATLAB / Actual Result = ")
                .append(String.format(Locale.US, "%.8f", actual)).append("\n\n");

        for (int i = 1; i <= 5; i++) {
            // Newton-Raphson formula for reciprocal square root: y_{k+1} = y_k * (1.5 - 0.5 * a * y_k^2)
            y = y * (1.5 - 0.5 * a * y * y);

            double approx = b * y;
            double error = Math.abs(actual - approx);

            result.append("Iteration ").append(i).append("\n");
            result.append("1/sqrt(a) = ")
                    .append(String.format(Locale.US, "%.8f", y)).append("\n");
            result.append("b/sqrt(a) = ")
                    .append(String.format(Locale.US, "%.8f", approx)).append("\n");
            result.append("Error = ")
                    .append(String.format(Locale.US, "%.8f", error)).append("\n\n");
        }

        result.append("Observation:\n");
        result.append("As the number of iterations increases,\n");
        result.append("the approximation converges rapidly\n");
        result.append("to the actual value.");

        txt.setText(result.toString());
    }
}
