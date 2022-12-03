package com.c323proj9.bradleystegbauer.controller;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public interface ExpenseController {
    List<Expense> getAllExpenses();
    List<Expense> getExpensesFromSearch(String searchInput, int type, String category);
    Expense addExpense(Expense expense) throws InvalidInputException;
    Expense getExpense();
    Expense updateExpense(Expense expense);
    Expense deleteExpense(int id);
}
