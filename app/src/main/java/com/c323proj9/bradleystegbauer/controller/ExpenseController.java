package com.c323proj9.bradleystegbauer.controller;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidIDException;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public interface ExpenseController {
    List<Expense> getAllExpenses();
    List<Expense> getExpensesFromSearch(String searchInput, int type, String category);
    Expense addExpense(Expense expense) throws InvalidInputException;
    Expense addExpense(String name, String moneyString, String date, String category) throws InvalidInputException;
    Expense getExpense(int id) throws InvalidIDException;
    Expense updateExpense(Expense expense);
    Expense deleteExpense(int id) throws InvalidIDException;
}
