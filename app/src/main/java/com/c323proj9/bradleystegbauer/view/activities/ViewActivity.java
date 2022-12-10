package com.c323proj9.bradleystegbauer.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c323proj9.bradleystegbauer.R;
import com.c323proj9.bradleystegbauer.view.recviewadapter.RecViewAdapter;

public class ViewActivity extends AppCompatActivity {
    private String category;
    private RecyclerView recyclerView;
    private RecViewAdapter recViewAdapter;
    private int searchType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Spinner spinner = findViewById(R.id.searchChoice_spinner_view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        RadioGroup radioGroup = findViewById(R.id.typeButtons_radioGroup_view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.nameRadio_radiobutton_view:
                        searchType = 0;
                        break;
                    case R.id.moneyRadio_radiobutton_view:
                        searchType = 1;
                        break;
                    case R.id.dateRadio_radiobutton_view:
                        searchType = 2;
                        break;
                }
            }
        });
        recyclerView = findViewById(R.id.expensesList_recyclerview_view);
        recViewAdapter = new RecViewAdapter(this, this);
        recyclerView.setAdapter(recViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void searchButtonCallback(View view) {
        EditText searchInput = findViewById(R.id.searchInput_edittext_view);
        String search = searchInput.getText().toString();
        recViewAdapter.search(search, searchType, category);
    }

}