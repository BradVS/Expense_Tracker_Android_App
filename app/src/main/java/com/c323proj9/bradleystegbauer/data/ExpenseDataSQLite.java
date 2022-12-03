package com.c323proj9.bradleystegbauer.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
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
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM expenses ORDER BY date(date);", null);
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
    public List<Expense> getExpensesFromSearch(String category, String additionalQueryPart) {
        List<Expense> expenses = new ArrayList<>();
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
    public Expense getExpense(int id) throws NoExpenseFoundException{
        //TODO: add exception handling
        Cursor cursor = db.rawQuery("SELECT * FROM expenses WHERE id = '"+id+"';", null);
        int idCol = cursor.getColumnIndex("id");
        int nameCol = cursor.getColumnIndex("name");
        int moneyCol = cursor.getColumnIndex("money");
        int dateCol = cursor.getColumnIndex("date");
        int categoryCol = cursor.getColumnIndex("category");
        cursor.moveToFirst();
        if (cursor.getCount() <= 0){
            throw new NoExpenseFoundException("Expense not found.");
        }
        Expense expense = new Expense(cursor.getInt(idCol), cursor.getString(nameCol), cursor.getString(dateCol),
                cursor.getString(categoryCol), cursor.getDouble(moneyCol));

        cursor.close();
        return expense;
    }

    @Override
    public Expense updateExpense(Expense expense) {
//        TODO: replace when better date formatter is implemented
        String[] dateArray = expense.getDate().split("/");
        String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
        db.execSQL("UPDATE expenses SET name = '" + expense.getName() +"', money = '" + expense.getMoney() +"', date = '" + dateFormat +"', category = '" +
                expense.getCategory() +"' WHERE id = " + expense.getId() + ";");
        return expense;
    }

    @Override
    public Expense deleteExpense(int id) {
        db.execSQL("DELETE FROM expenses WHERE id = " + id + ";");
        return null;
    }
}
