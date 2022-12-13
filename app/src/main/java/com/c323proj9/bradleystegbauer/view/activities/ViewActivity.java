package com.c323proj9.bradleystegbauer.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c323proj9.bradleystegbauer.R;
import com.c323proj9.bradleystegbauer.view.activities.datareceivers.TotalExpensesReceiver;
import com.c323proj9.bradleystegbauer.view.recviewadapter.RecViewAdapter;

import java.math.BigDecimal;

public class ViewActivity extends AppCompatActivity implements TotalExpensesReceiver {
    private String category;
    private RecViewAdapter recViewAdapter;
    private BigDecimal totalExpenses;
    private int searchType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        this.totalExpenses = new BigDecimal("0");
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
        RecyclerView recyclerView = findViewById(R.id.expensesList_recyclerview_view);
        recViewAdapter = new RecViewAdapter(this, this, this);
        recyclerView.setAdapter(recViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.recViewAdapter.loadAllItems();
    }

    public void searchButtonCallback(View view) {
        EditText searchInput = findViewById(R.id.searchInput_edittext_view);
        String search = searchInput.getText().toString();
        recViewAdapter.search(search, searchType, category);
    }

    public void addExpenseButtonCallback(View view) {
        startActivity(new Intent(ViewActivity.this, MainActivity.class));
    }

    @Override
    public void getTotalExpenses(@NonNull BigDecimal total) {
        TextView totalExpensesTextView = findViewById(R.id.totalExpenses_textView);
        this.totalExpenses = total;
        totalExpensesTextView.setText(getString(R.string.totalExpenses_text, totalExpenses));
    }

    @Override
    public void subtractFromTotalExpenses(@NonNull BigDecimal subtraction) {
        TextView totalExpensesTextView = findViewById(R.id.totalExpenses_textView);
        this.totalExpenses = this.totalExpenses.subtract(subtraction);
        totalExpensesTextView.setText(getString(R.string.totalExpenses_text, totalExpenses));
    }

    @Override
    public void addToTotalExpenses(@NonNull BigDecimal addition) {
        TextView totalExpensesTextView = findViewById(R.id.totalExpenses_textView);
        this.totalExpenses = this.totalExpenses.add(addition);
        totalExpensesTextView.setText(getString(R.string.totalExpenses_text, totalExpenses));
    }
}