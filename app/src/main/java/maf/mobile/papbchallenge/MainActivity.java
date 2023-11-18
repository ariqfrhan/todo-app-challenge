package maf.mobile.papbchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvTask;
    private TextView tesJam;
    private TextView tesDesc;
    private RecyclerView rvTodo;
    private ArrayList<Todo> todoData;
    private TodoAdapter todoAdapter;
    private ImageButton btFilter;
    private boolean isDescend = true;
    private Button btAdd;
    private EditText etTask;
    private EditText etClock;
    private ImageButton btClock;

    int hour, minute;
    private Button btReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = (TextView) findViewById(R.id.tvName);
        tvTask = (TextView) findViewById(R.id.tvTask);
        rvTodo = (RecyclerView) findViewById(R.id.rvTodo);
        btAdd = (Button) findViewById(R.id.btAdd);
        etTask = (EditText) findViewById(R.id.etTask);
        etClock = (EditText) findViewById(R.id.etClock);
        btClock = (ImageButton) findViewById(R.id.btClock);
        btReset = (Button) findViewById(R.id.btReset);

        todoData = new ArrayList<>();
        todoAdapter = new TodoAdapter(this, todoData, this);
        rvTodo.setAdapter(todoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTodo.setLayoutManager(layoutManager);

        btFilter = (ImageButton) findViewById(R.id.btFilter);

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(todoData, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo o1, Todo o2) {
                        if (isDescend) {
                            return o2.time.compareToIgnoreCase(o1.time);
                        }else{
                            return o1.time.compareToIgnoreCase(o2.time);
                        }
                    }
                });

                isDescend = !isDescend;

                todoAdapter.notifyDataSetChanged();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTask.getText().toString().equals("") || etClock.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill the data", Toast.LENGTH_SHORT).show();
                }else{
                    String task = etTask.getText().toString();
                    String hour = etClock.getText().toString();
                    todoData.add(new Todo(hour, task));
                    Toast.makeText(MainActivity.this, "The data has been added", Toast.LENGTH_SHORT).show();
                }
                todoAdapter.notifyDataSetChanged();
                int jumlah = todoData.size();
                TaskCount(jumlah);
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoData.clear();
                getTodo();
                Toast.makeText(MainActivity.this, "Reset Data Complete", Toast.LENGTH_SHORT).show();
                todoAdapter.notifyDataSetChanged();
                int jumlah = todoData.size();
                TaskCount(jumlah);

            }
        });

        int jumlah = todoData.size();
        TaskCount(jumlah);
        todoAdapter.notifyDataSetChanged();


        getTodo();
    }

    public void TaskCount(int jumlah){
        tvTask.setText("You have " + jumlah + " task today");
    }

    private void getTodo(){
        String url = "https://mgm.ub.ac.id/todo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);

                        String time = responseObj.getString("time");
                        String what = responseObj.getString("what");

                        Todo todo = new Todo(time, what);
                        todoData.add(todo);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                int jumlah = response.length();
                TaskCount(jumlah);
                todoAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void PopupTime(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                hour = hourOfDay;
                minute = minutes;
                etClock.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour,minute,true);
        timePickerDialog.show();
    }
}