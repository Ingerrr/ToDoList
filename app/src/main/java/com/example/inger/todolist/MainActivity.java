/*
* Inger Bij de Vaate: 10624562
 */

package com.example.inger.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
* Activity in which user can store their to does in a list and also update this list
 */
public class MainActivity extends AppCompatActivity {

    // Create variables
    ArrayList<String> toDoes;
    ListView listToDo;
    ArrayAdapter<String> listAdapter;

    /*
    * Creates empty list if app is opened for the first time or reads existing list from file
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create link to ListView on screen
        listToDo = (ListView)findViewById(R.id.list);

        // Create empty toDoes
        toDoes = new ArrayList<String>();

        // Attempts to input list from file if it exists
        try {
            // Read existing toDoes from file
            Scanner scan = new Scanner(openFileInput("Todolist.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                toDoes.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Set Adapter to ListView
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoes);
        listToDo.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        // Setup listener for clicks on ListView
        setupListener();
    }

    /*
    * Adds item to toDoes after user input
     */
    public void addItem(View view) throws FileNotFoundException {

        // Read input from user
        String input = ((EditText) findViewById(R.id.input)).getText().toString();

        // Add input to toDoes
        toDoes.add(input);

        // Update ListView
        listAdapter.notifyDataSetChanged();

        // Clear EditText
        ((EditText)findViewById(R.id.input)).setText("");

        // Store/update list in text file
        writeToFile(toDoes);
    }

    /*
    * Setup a listener for clicks on ListView
     */
    public void setupListener() {
        listToDo.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    /*
                    * Deletes an item from the list when it is long-clicked
                     */
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        // Removes item from list
                        toDoes.remove(position);

                        // Update ListView
                        listAdapter.notifyDataSetChanged();

                        // Update text file
                        try {
                            writeToFile(toDoes);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                });
    }

    /*
    * Writes list to text file so that ListView doesn't reset after reboot
     */
    public void writeToFile(ArrayList<String> toDoes) throws FileNotFoundException {

        // Create link to file
        PrintStream out = new PrintStream(openFileOutput("Todolist.txt", MODE_PRIVATE));

        // Write toDoes to file one by one
        for (int i = 0; i < toDoes.size(); i++) {
            out.println(toDoes.get(i));
        }

        // Close file
        out.close();
    }
}

