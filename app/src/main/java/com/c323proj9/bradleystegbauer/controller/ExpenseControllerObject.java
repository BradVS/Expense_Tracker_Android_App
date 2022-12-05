package com.c323proj9.bradleystegbauer.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidIDException;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.data.ExpenseDataManager;
import com.c323proj9.bradleystegbauer.data.ExpenseDataSQLite;
import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public class ExpenseControllerObject implements ExpenseController {
    private final ExpenseDataManager dataManager;

    public ExpenseControllerObject(Context context) {
        dataManager = new ExpenseDataSQLite(context);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return dataManager.getAllExpenses();
    }

    @Override
    public List<Expense> getExpensesFromSearch(String searchInput, int type, String category) throws InvalidInputException {
        String additionalQueryPart = "";
        if (searchInput.equals("")){ //makes it so the search only does a filter if nothing was input
            return dataManager.getExpensesFromSearch(category, additionalQueryPart);
        }
        if (type == 0){
            additionalQueryPart = " AND name = '" + searchInput + "'";
        }else if(type == 2){
            //TODO: replace with better data format system
            String[] dateArray = searchInput.split("/");
            if (dateArray.length <= 2){
                throw new InvalidInputException("Enter a valid date");
            }
            String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
            additionalQueryPart = " AND date = '" + dateFormat +"'";
        }else if(type == 1){
            try{
                double input = Double.parseDouble(searchInput);
                if (input < 0){
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e){
                throw new InvalidInputException("Enter a valid monetary amount");
            }
            additionalQueryPart = " AND money = '" + searchInput + "'";
        }
        return dataManager.getExpensesFromSearch(category, additionalQueryPart);
    }

    @Override
    public Expense addExpense(Expense expense) throws InvalidInputException {
        if(expense.getName().isEmpty() || expense.getDate().isEmpty() || expense.getCategory().isEmpty()){
            throw new InvalidInputException("Please enter name, date, and select a category.");
        }
        if (!dateFormatCheck(expense.getDate())){
            throw new InvalidInputException("Please enter a valid date.");
        }
        if (expense.getId() != -99){
            throw new InvalidInputException("This entry was already added.");
        }
        return dataManager.addExpense(expense);
    }

    @Override
    public Expense addExpense(String name, String moneyString, String date, String category) throws InvalidInputException {
        if (name.equals("") || moneyString.equals("") || date.equals("") || category.equals("")){
            throw new InvalidInputException("Please enter name, money value, date, and select a category.");
        }
        if (!dateFormatCheck(date)){
            throw new InvalidInputException("Please enter a valid date.");
        }
        try{
            double money = Double.parseDouble(moneyString);
            Expense expense = new Expense(name, date, category, money);
            return dataManager.addExpense(expense);
        }catch (NumberFormatException e){
            throw new InvalidInputException("Please enter a valid monetary value.");
        }
    }

    @Override
    public Expense getExpense(int id) throws InvalidIDException {
        if (id < 0){
            throw new InvalidIDException("ID for search is not valid.");
        }
        try{
            return dataManager.getExpense(id);
        }catch (NoExpenseFoundException e){
            throw new InvalidIDException("ID for search does not correlate to any entries.");
        }
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return null;
    }

    @Override
    public Expense deleteExpense(int id) throws InvalidIDException {
        if (id < 0){
            throw new InvalidIDException("ID for deletion is not valid.");
        }
        try{
            return dataManager.deleteExpense(id);
        } catch (NoExpenseFoundException e) {
            throw new InvalidIDException("ID for deletion is not valid.");
        }
    }

    /**
     * Checks to see if the date string entered is a valid date.
     * @param date String holding the date
     * @return True if valid, false otherwise
     */
    private boolean dateFormatCheck(@NonNull String date) {
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
