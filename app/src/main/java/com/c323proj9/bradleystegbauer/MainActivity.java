package com.c323proj9.bradleystegbauer;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c323proj9.bradleystegbauer.controller.ExpenseController;
import com.c323proj9.bradleystegbauer.controller.ExpenseControllerObject;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.model.Expense;

public class MainActivity extends AppCompatActivity {
    String category = "";
//    private SQLiteDatabase db;
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
//        try{
//            db = this.openOrCreateDatabase("ExpensesDB",  MODE_PRIVATE,null);
//            db.execSQL("CREATE TABLE IF NOT EXISTS expenses" + "(id integer primary key, name text, money real, date text, category text);");
//        } catch (SQLException e){
//            Toast.makeText(this, "Error: Could not connect to database.", Toast.LENGTH_SHORT).show();
//        }
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
//        try{
//            EditText nameInput = findViewById(R.id.expenseInput_edittext_main);
//            EditText moneyInput = findViewById(R.id.moneyInput_edittext_main);
//            EditText dateInput = findViewById(R.id.dateInput_edittext_main);
//            String name = nameInput.getText().toString();
//            String moneyString = moneyInput.getText().toString();
//            String date = dateInput.getText().toString();
//            if (name.equals("") || moneyString.equals("") || date.equals("") || category.equals("")){
//                Toast.makeText(this, "Please fill all inputs.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            double money = Double.parseDouble(moneyString);
//            if (!dateFormatCheck(date)){
//                Toast.makeText(this, "Please enter a valid date.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String[] dateArray = date.split("/");
//            String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
//            if (db.isOpen() && db != null){
//                db.execSQL("INSERT INTO expenses(name, money, date, category) VALUES('"+name+"','"+money+"','"+dateFormat+"','"+category+"');");
//                Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
//                nameInput.setText("");
//                dateInput.setText("");
//                moneyInput.setText("");
//            }
//        } catch (NumberFormatException e){
//            Toast.makeText(this, "Please enter a valid monetary amount.", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * Checks to see if the date string entered is a valid date.
     * @param date String holding the date
     * @return True if valid, false otherwise
     */
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

    /**
     * Starts the ViewActivity.
     * @param view The view used for the callback function.
     */
    public void viewExpensesCallback(View view) {
        startActivity(new Intent(MainActivity.this, ViewActivity.class));
    }
}