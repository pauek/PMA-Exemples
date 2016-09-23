package edu.upc.eet.pma.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    // 'ids_respostes' és un membre o un atribut de la classe QuizActivity
    private int ids_respostes[] = {
        R.id.resp1, R.id.resp2, R.id.resp3, R.id.resp4
    };
    private int resposta_correcta = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Button check = (Button) findViewById(R.id.check);
        final RadioGroup respostes = (RadioGroup) findViewById(R.id.respostes);

        String[] answers = getResources().getStringArray(R.array.answers);
        for (int i = 0; i < ids_respostes.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_respostes[i]);
            String ans = answers[i];
            if (ans.charAt(0) == '*') {
                ans = ans.substring(1);
                resposta_correcta = i;
            }
            rb.setText(ans);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = respostes.getCheckedRadioButtonId();
                int index = -1;
                for (int i = 0; i < ids_respostes.length; i++) {
                    if (ids_respostes[i] == id) {
                        index = i;
                    }
                }
                if (index == resposta_correcta) {
                    Toast.makeText(QuizActivity.this, R.string.correct, Toast.LENGTH_SHORT)
                         .show();
                }

                Log.i("pauek", String.format("Resposta: %d", index));
            }
        });
    }
}
