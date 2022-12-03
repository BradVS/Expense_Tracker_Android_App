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
        return dataManager.getExpensesFromSearch(searchInput, type, category);
    }

    @Override
    public Expense addExpense(Expense expense) throws InvalidInputException {

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
