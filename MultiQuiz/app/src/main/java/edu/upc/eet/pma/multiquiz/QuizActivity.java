package edu.upc.eet.pma.multiquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    // 'ids_respostes' és un membre o un atribut de la classe QuizActivity
    private int ids_respostes[] = {
        R.id.resp1, R.id.resp2, R.id.resp3, R.id.resp4
    };
    private int resposta_correcta = -1;
    private String[] all_questions;
    private int current_question = 0;
    private boolean[] correct;
    private int[] user_answers;
    private RadioGroup respostes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Button check = (Button) findViewById(R.id.check);
        Button prev  = (Button) findViewById(R.id.btn_prev);
        respostes = (RadioGroup) findViewById(R.id.respostes);

        all_questions = getResources().getStringArray(R.array.all_questions);
        correct = new boolean[all_questions.length];
        user_answers = new int[all_questions.length];
        for (int i = 0; i < user_answers.length; i++) {
            user_answers[i] = -1;
        }

        showQuestion(current_question);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (current_question < all_questions.length-1) {
                    current_question++;
                    showQuestion(current_question);
                } else { // estem a la última
                    for (int i = 0; i < correct.length; i++) {
                        String resp = "bé";
                        if (!correct[i]) {
                            resp = "malament";
                        }
                        Log.i("pauek", String.format("Reposta %d: %s", i, resp));
                    }
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (current_question > 0) {
                    current_question--;
                    showQuestion(current_question);
                }
            }
        });
    }

    private void checkAnswer() {
        int id = respostes.getCheckedRadioButtonId();
        int index = -1;
        for (int i = 0; i < ids_respostes.length; i++) {
            if (ids_respostes[i] == id) {
                index = i;
            }
        }
        correct[current_question] = (index == resposta_correcta);
        user_answers[current_question] = index;
    }

    private void showQuestion(int index) {
        String q = all_questions[index];
        String[] parts = q.split(";");

        TextView question_text = (TextView) findViewById(R.id.question_text);
        question_text.setText(parts[0]);

        for (int i = 0; i < ids_respostes.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_respostes[i]);
            String ans = parts[i+1];
            if (ans.charAt(0) == '*') {
                ans = ans.substring(1);
                resposta_correcta = i;
            }
            rb.setText(ans);
            rb.setChecked(i == user_answers[current_question]);
        }

        // Si estem a la última pregunta: posar "finish" al botó de baix
        // i si no estem a la última posem "next".
        Button btn_check = (Button) findViewById(R.id.check);
        if (index == all_questions.length-1) {
            btn_check.setText(R.string.finish);
        } else {
            btn_check.setText(R.string.next);
        }

        // Si estem a la primera pregunta el botó "anterior" no ha de sortir
        Button btn_prev = (Button) findViewById(R.id.btn_prev);
        if (index == 0) {
            btn_prev.setVisibility(View.GONE);
        } else {
            btn_prev.setVisibility(View.VISIBLE);
        }
    }
}
