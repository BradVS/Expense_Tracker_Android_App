package com.c323proj9.bradleystegbauer.controller;

import com.c323proj9.bradleystegbauer.controller.ExpenseController;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.data.ExpenseDataManager;
import com.c323proj9.bradleystegbauer.data.ExpenseDataSQLite;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public class ExpenseControllerObject implements ExpenseController {
    private final ExpenseDataManager dataManager;

    public ExpenseControllerObject() {
        dataManager = new ExpenseDataSQLite();
    }

    @Override
    public List<Expense> getAllExpenses() {
        return dataManager.getAllExpenses();
    }

    @Override
    public List<Expense> getExpensesFromSearch(String searchInput, int type, String category) {
        String additionalQueryPart = "";
        if (type == 0){
            additionalQueryPart = " AND name = '" + searchInput + "'";
        }else if(type == 2){
            if (searchInput.equals("")){
                additionalQueryPart = "";
            }else{
                //TODO: replace with better data format system
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
        return dataManager.getExpensesFromSearch(category, additionalQueryPart);
    }

    @Override
    public Expense addExpense(Expense expense) throws InvalidInputException {

        return null;
    }

    @Override
    public Expense getExpense(int id) {
        return null;
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return null;
    }

    @Override
    public Expense deleteExpense(int id) {
        return null;
    }
}
