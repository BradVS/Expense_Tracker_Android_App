package com.c323proj9.bradleystegbauer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.c323proj9.bradleystegbauer.controller.ExpenseController;
import com.c323proj9.bradleystegbauer.controller.ExpenseControllerObject;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.datepicker.DateInfoConsumer;
import com.c323proj9.bradleystegbauer.datepicker.DatePickerFragment;
import com.c323proj9.bradleystegbauer.model.Expense;

public class MainActivity extends AppCompatActivity implements DateInfoConsumer {
    private String category = "";
    private ExpenseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new ExpenseControllerObject(this);
        Spinner spinner = findViewById(R.id.categoryChoice_spinner_main);
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
    }

    /**
     * Inserts an entry into the database if it is valid.
     * @param view The view used for the callback function.
     */
    public void addExpenseCallback(View view) {
        EditText nameInput = findViewById(R.id.expenseInput_edittext_main);
        EditText moneyInput = findViewById(R.id.moneyInput_edittext_main);
        EditText dateInput = findViewById(R.id.dateInput_edittext_main);
        String name = nameInput.getText().toString();
        String moneyString = moneyInput.getText().toString();
        String date = dateInput.getText().toString();
        try{
            Expense expense = controller.addExpense(name, moneyString, date, category);
            Toast.makeText(this, expense.getName() + " added!", Toast.LENGTH_SHORT).show();
            nameInput.setText("");
            moneyInput.setText("");
            dateInput.setText("");
        } catch (InvalidInputException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts the ViewActivity.
     * @param view The view used for the callback function.
     */
    public void viewExpensesCallback(View view) {
        startActivity(new Intent(MainActivity.this, ViewActivity.class));
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment(this);
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void getDateString(String dateString) {
        EditText dateInput = findViewById(R.id.dateInput_edittext_main);
        dateInput.setText(dateString);
    }
}