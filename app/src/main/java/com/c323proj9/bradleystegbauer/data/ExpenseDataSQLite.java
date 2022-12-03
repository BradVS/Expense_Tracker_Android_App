package com.c323proj9.bradleystegbauer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDataSQLite implements ExpenseDataManager{
    private final SQLiteDatabase db;

    public ExpenseDataSQLite() {
        db = SQLiteDatabase.openOrCreateDatabase("ExpensesDB", null);
        //TODO: wrap this in exception handler?
        db.execSQL("CREATE TABLE IF NOT EXISTS expenses" + "(id integer primary key, name text, money real, date text, category text);");
    }

    @Override
    public List<Expense> getAllExpenses() {
        return null;
    }

    @Override
    public List<Expense> getExpensesFromSearch(String searchInput, int type, String category) {
        List<Expense> expenses = new ArrayList<>();
        String additionalQueryPart = "";
        if (type == 0){
            additionalQueryPart = " AND name = '" + searchInput + "'";
        }else if(type == 2){
            if (searchInput.equals("")){
                additionalQueryPart = "";
            }else{
                String[] dateArray = searchInput.split("/");
                String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
                additionalQueryPart = " AND date = '" + dateFormat +"'";
            }
        }else if(type == 1){
            additionalQueryPart = " AND money = '" + searchInput + "'";
        }
        if (searchInput.equals("")){ //makes it so the search only does a filter if nothing was input
            additionalQueryPart = "";
        }
        Cursor cursor = db.rawQuery("SELECT * FROM expenses WHERE category = '"+category+"' "+additionalQueryPart+" ORDER BY date(date);", null);
        int idCol = cursor.getColumnIndex("id");
        int nameCol = cursor.getColumnIndex("name");
        int moneyCol = cursor.getColumnIndex("money");
        int dateCol = cursor.getColumnIndex("date");
        int categoryCol = cursor.getColumnIndex("category");
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            do{
                expenses.add(new Expense(cursor.getInt(idCol), cursor.getString(nameCol), cursor.getString(dateCol),
                        cursor.getString(categoryCol), cursor.getDouble(moneyCol)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return expenses;
    }

    @Override
    public Expense addExpense(Expense expense) {
        //TODO: replace this by implementing better date formatting system
        String[] dateArray = expense.getDate().split("/");
        String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
        db.execSQL("INSERT INTO expenses(name, money, date, category) VALUES('"+expense.getName()+"','"+expense.getMoney()+"','"+dateFormat+"','"+expense.getCategory()+"');");
        return null;
    }

    @Override
    public Expense getExpense() {
        return null;
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return null;
    }

    @Override
    public Expense deleteExpense() {
        return null;
    }
}
