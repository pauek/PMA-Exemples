package edu.upc.eet.pma.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    private int pos = -1;
    private EditText edit_title, edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text  = (EditText) findViewById(R.id.edit_text);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String text = extras.getString(Intent.EXTRA_TEXT);
            if (text == null) {
                String title = extras.getString("title");
                text = extras.getString("text");
                edit_title.setText(title);
                edit_text.setText(text);
                pos = extras.getInt("pos");
            } else {
                edit_text.setText(text);
                edit_title.setText(R.string.new_note);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_save:
                Intent data = new Intent();
                if (pos != -1) {
                    data.putExtra("pos", pos);
                }
                data.putExtra("title", edit_title.getText().toString());
                data.putExtra("text",  edit_text.getText().toString());
                setResult(RESULT_OK, data);
                finish();
                return true;

            case R.id.option_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, edit_text.getText().toString());
                intent.setType("text/plain");
                String chooser_title = getResources().getString(R.string.chooser_title);
                Intent chooser = Intent.createChooser(intent, chooser_title);
                startActivity(chooser);
                return true;

        default:
            return super.onOptionsItemSelected(item);
        }

    }
}
