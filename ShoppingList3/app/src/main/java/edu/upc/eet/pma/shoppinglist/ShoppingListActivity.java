package edu.upc.eet.pma.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private final static String FILENAME = "shopping_list.txt";
    private final static int MAX_BYTES = 6000;

    private ArrayList<ShoppingItem> itemList;
    private ShoppingListAdapter adapter;

    private ListView list;
    private EditText edit_item;

    @Override
    protected void onStop() {
        super.onStop();
        writeList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_checked:
                clearChecked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearChecked() {
        int i = 0;
        while (i < itemList.size()) {
            if (itemList.get(i).isChecked()) {
                itemList.remove(i);
            } else {
                i++;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void writeList() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < itemList.size(); i++) {
                ShoppingItem item = itemList.get(i);
                String item_txt = String.format("%s;%b\n", item.getText(), item.isChecked());
                fos.write(item_txt.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("pauek", "writeList: FileNotFoundException");
        } catch (IOException e) {
            Log.e("pauek", "writeList: IOException");
        }
    }

    private ArrayList<ShoppingItem> readList() {
        ArrayList<ShoppingItem> list = new ArrayList<ShoppingItem>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nbytes = fis.read(buffer);
            String content = new String(buffer, 0, nbytes);
            String[] lines = content.split("\n");
            for (String line : lines) {
                String[] parts = line.split(";");
                list.add(new ShoppingItem(parts[0], parts[1].equals("true")));
            }
            fis.close();
            return list;
        } catch (FileNotFoundException e) {
            return list;
        } catch (IOException e) {
            Toast.makeText(this, R.string.read_error, Toast.LENGTH_SHORT).show();
            return list;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        itemList = readList();
        adapter = new ShoppingListAdapter(this, itemList);

        edit_item = (EditText) findViewById(R.id.edit_item);
        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem();
                return true;
            }
        });

        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                itemList.get(pos).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });
    }

    public void onButtonAddClick(View v) {
        addItem();
    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String msg = getResources().getString(R.string.confirm_remove);
        builder.setMessage(String.format(msg, itemList.get(pos).getText()));
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty()) {
            itemList.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
            list.smoothScrollToPosition(itemList.size()-1);
        }
    }
}
