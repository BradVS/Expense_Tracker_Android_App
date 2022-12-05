package com.c323proj9.bradleystegbauer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpenseDataSQLite implements ExpenseDataManager{
    private final SQLiteDatabase db;

    public ExpenseDataSQLite(Context context) {
        db = context.openOrCreateDatabase("ExpensesDB", Context.MODE_PRIVATE, null);
//        db = SQLiteDatabase.openOrCreateDatabase("ExpensesDB", null);
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
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid();", null);
        int colId = cursor.getColumnIndex("last_insert_rowid()");
        cursor.moveToFirst();
        Expense expenseWithId = new Expense(cursor.getInt(colId), expense.getName(), expense.getDate(), expense.getCategory(), expense.getMoney());
        cursor.close();
        return expenseWithId;
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
        String dateFormat;
        if(dateArray.length == 1){
            dateFormat = expense.getDate();
        }else{
            dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
        }
        db.execSQL("UPDATE expenses SET name = '" + expense.getName() +"', money = '" + expense.getMoney() +"', date = '" + dateFormat +"', category = '" +
                expense.getCategory() +"' WHERE id = " + expense.getId() + ";");
        return expense;
    }

    @Override
    public Expense deleteExpense(int id) throws NoExpenseFoundException{
        Expense expense = getExpense(id);
        db.execSQL("DELETE FROM expenses WHERE id = " + id + ";");
        return expense;
    }
}
