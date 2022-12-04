package com.c323proj9.bradleystegbauer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewActivity extends AppCompatActivity {
    String category;
    RecyclerView recyclerView;
    RecViewAdapter recViewAdapter;
    int searchType = 0;


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
        recViewAdapter = new RecViewAdapter(this);
        recyclerView.setAdapter(recViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void searchButtonCallback(View view) {
        EditText searchInput = findViewById(R.id.searchInput_edittext_view);
        String search = searchInput.getText().toString();
        recViewAdapter.search(search, searchType, category);
//        if (search.equals("")){
//            recViewAdapter.search(search, searchType, category);
//            return;
//        }
//        if (searchType == 0){
//            recViewAdapter.search(search, searchType, category);
//        }else if(searchType == 1){
//            try{
//                Double.parseDouble(search);
//                recViewAdapter.search(search, searchType, category);
//            } catch (NumberFormatException e){
//                Toast.makeText(this, "Invalid monetary amount.", Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            if (dateFormatCheck(search)){
//                recViewAdapter.search(search, searchType, category);
//            }else{
//                Toast.makeText(this, "Invalid date.", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private boolean dateFormatCheck(String date) {
        String[] dateSplit = date.split("/");
//        first check if split was into 3 parts
        if (dateSplit.length != 3){
            return false;
        }
//        check if month, day, and year are correct length
        if (dateSplit[0].length() != 2 || dateSplit[1].length() != 2 ||dateSplit[2].length() != 4){
            return false;
        }
//        try making them into integers
        try{
            int month = Integer.parseInt(dateSplit[0]);
            int day = Integer.parseInt(dateSplit[1]);
            int year = Integer.parseInt(dateSplit[2]);

//            check if they are within bounds
//            start with month, year, and day total bounds
            if(month < 1 || month > 12 || day < 1 || day > 31 || year < 1){
                return false;
            }
//            check for leap years
            if(year % 4 != 0 && month == 2 && day > 28){
//                fail condition (common year)
                return false;
            } else if(year % 100 != 0 && month == 2 && day > 29){
//                fail condition (leap year)
                return false;
            } else if(year % 400 != 0 && month == 2 && day > 28){
//                fail condition (common year)
                return false;
            } else if(month == 2 && day > 29){
//                fail condition (leap year)
                return false;
            }
//            if it made it past this, leap year rules have been followed (based on Gregorian calendar)
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}