package com.c323proj9.bradleystegbauer.data;

import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public interface ExpenseDataManager {
    List<Expense> getAllExpenses();
    List<Expense> getExpensesFromSearch(String searchInput, int type, String category);
    Expense addExpense(Expense expense);
    Expense getExpense(int id) throws NoExpenseFoundException;
    Expense updateExpense(Expense expense);
    Expense deleteExpense(int id);
}
