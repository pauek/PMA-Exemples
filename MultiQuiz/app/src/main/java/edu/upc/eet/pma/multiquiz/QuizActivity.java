package edu.upc.eet.pma.multiquiz;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
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
    private RadioGroup respostes;
    private String[] all_questions;

    // Estat de QuizActivity
    private int resposta_correcta = -1;
    private int current_question;
    private boolean[] correct;
    private int[] user_answers;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_question", current_question);
        outState.putInt("resposta_correcta", resposta_correcta);
        outState.putBooleanArray("correct", correct);
        outState.putIntArray("user_answers", user_answers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle", "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "onStop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle", "onCreate");

        setContentView(R.layout.activity_quiz);

        Button check = (Button) findViewById(R.id.check);
        Button prev  = (Button) findViewById(R.id.btn_prev);
        respostes = (RadioGroup) findViewById(R.id.respostes);

        all_questions = getResources().getStringArray(R.array.all_questions);

        if (savedInstanceState == null) {
            restart();
        } else {
            current_question = savedInstanceState.getInt("current_question");
            resposta_correcta = savedInstanceState.getInt("resposta_correcta");
            correct = savedInstanceState.getBooleanArray("correct");
            user_answers = savedInstanceState.getIntArray("user_answers");
            showQuestion();
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (current_question < all_questions.length-1) {
                    current_question++;
                    showQuestion();
                } else { // estem a la última
                    showResults();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (current_question > 0) {
                    current_question--;
                    showQuestion();
                }
            }
        });
    }

    private void showResults() {
        int ngood = 0, nbad = 0, nnoanswer = 0;
        for (int i = 0; i < all_questions.length; i++) {
            if (user_answers[i] == -1) nnoanswer++;
            else if (correct[i]) ngood++;
            else nbad++;
        }

        Resources res = getResources();
        String scorrect = res.getString(R.string.scorrect);
        String sincorrect = res.getString(R.string.sincorrect);
        String sunanswered = res.getString(R.string.sunanswered);
        String result =
                String.format("%s: %d\n%s: %d\n%s: %d\n",
                        scorrect, ngood,
                        sincorrect, nbad,
                        sunanswered, nnoanswer);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.results);
        builder.setCancelable(false);
        builder.setMessage(result);
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restart();
            }
        });
        builder.create().show();
    }

    private void restart() {
        correct = new boolean[all_questions.length];
        user_answers = new int[all_questions.length];
        for (int i = 0; i < user_answers.length; i++) {
            user_answers[i] = -1;
        }
        current_question = 0;
        showQuestion();
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

    private void showQuestion() {
        String q = all_questions[current_question];
        String[] parts = q.split(";");

        TextView question_text = (TextView) findViewById(R.id.question_text);
        question_text.setText(parts[0]);

        respostes.clearCheck();

        for (int i = 0; i < ids_respostes.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_respostes[i]);
            String ans = parts[i+1];
            if (ans.charAt(0) == '*') {
                ans = ans.substring(1);
                resposta_correcta = i;
            }
            rb.setText(ans);
            if (i == user_answers[this.current_question]) {
                rb.setChecked(true);
            }
        }

        // Si estem a la última pregunta: posar "finish" al botó de baix
        // i si no estem a la última posem "next".
        Button btn_check = (Button) findViewById(R.id.check);
        if (current_question == all_questions.length-1) {
            btn_check.setText(R.string.finish);
        } else {
            btn_check.setText(R.string.next);
        }

        // Si estem a la primera pregunta el botó "anterior" no ha de sortir
        Button btn_prev = (Button) findViewById(R.id.btn_prev);
        if (current_question == 0) {
            btn_prev.setVisibility(View.GONE);
        } else {
            btn_prev.setVisibility(View.VISIBLE);
        }
    }
}
