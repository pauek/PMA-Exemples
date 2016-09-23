package edu.upc.eet.pma.kmamilles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /* onCreate és un mètode */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_a_km = (Button) findViewById(R.id.btn_a_km);
        Button btn_a_miles = (Button) findViewById(R.id.btn_a_milles);
        final EditText editKm = (EditText) findViewById(R.id.editKm);
        final EditText editMiles = (EditText) findViewById(R.id.editMiles);

        btn_a_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicks", "S'ha apretat el botó 'a km'");
                // 1. Extreure el text de la caixa de Kilòmetres
                String text = editMiles.getText().toString();
                // 2. Convertir a real.
                float valor = Float.parseFloat(text);
                // 3. Convertir a milles.
                valor = valor * 1.60934f;
                // 4. Convertir a text.
                text = String.format("%f", valor);
                // 5. Posar-lo a la caixa de milles.
                editKm.setText(text);
            }
        });

        btn_a_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editKm.getText().toString();
                float valor = Float.parseFloat(text);
                valor = valor / 1.60934f;
                text = String.format("%f", valor);
                editMiles.setText(text);
            }
        });

    }
}
