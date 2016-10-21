package edu.upc.eet.pma.notes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private static final int NEW_NOTE = 0;
    private static final int EDIT_NOTE = 1;
    private ArrayList<Note> notes;
    private ArrayAdapter<Note> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        notes = new ArrayList<>();
        notes.add(new Note("hola", "que tal"));
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                notes
        );
        ListView notes_list = (ListView) findViewById(R.id.notes_list);
        notes_list.setAdapter(adapter);

        FloatingActionButton btn_new_note = (FloatingActionButton) findViewById(R.id.btn_new_note);
        btn_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

        notes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                editNote(pos);
            }
        });
    }

    private void editNote(int pos) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        Note n = notes.get(pos);
        intent.putExtra("pos", pos);
        intent.putExtra("title", n.getTitle());
        intent.putExtra("text", n.getText());
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void newNote() {
        Intent intent = new Intent(this, EditNoteActivity.class);
        startActivityForResult(intent, NEW_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NEW_NOTE:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("title");
                    String text = data.getStringExtra("text");
                    notes.add(new Note(title, text));
                    adapter.notifyDataSetChanged();
                }
                break;

            case EDIT_NOTE:
                if (resultCode == RESULT_OK) {
                    int pos = data.getIntExtra("pos", -1);
                    String title = data.getStringExtra("title");
                    String text = data.getStringExtra("text");
                    notes.get(pos).setText(text);
                    notes.get(pos).setTitle(title);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
