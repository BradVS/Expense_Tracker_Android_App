package com.c323proj9.bradleystegbauer.data;

import static org.junit.Assert.*;

import com.c323proj9.bradleystegbauer.model.Expense;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpenseDataSQLiteTest {

    @Before
    public void setUp() throws Exception {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        dataManager.addExpense(new Expense("TestExpense", "12/12/2022", "Food", 12.12));
    }

    @Test
    public void getAllExpenses() {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        List<Expense> expenses = dataManager.getAllExpenses();
        assert expenses.size() > 0;
        assert expenses.get(0).getId() >= 0;
        if (expenses.size() > 1){
            assert expenses.get(0) != expenses.get(1);
        }
    }

    @Test
    public void getExpensesFromSearch() {
    }

    @Test
    public void addExpense() {
    }

    @Test
    public void getExpense() {
    }

    @Test
    public void updateExpense() {
    }

    @Test
    public void deleteExpense() {
    }
}