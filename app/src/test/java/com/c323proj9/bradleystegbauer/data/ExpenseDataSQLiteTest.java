package com.c323proj9.bradleystegbauer.data;

import static org.junit.Assert.*;

import com.c323proj9.bradleystegbauer.controller.ExpenseController;
import com.c323proj9.bradleystegbauer.controller.ExpenseControllerObject;
import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
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
        assertTrue(expenses.size() > 0);
        assertTrue(expenses.get(0).getId() >= 0);
        if (expenses.size() > 1){
            assertNotEquals(expenses.get(0), expenses.get(1));
        }
    }

    @Test
    public void getExpensesFromSearch() {
        String additionalQueryPart = " AND name = 'TestExpense'";
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        List<Expense> expenses = dataManager.getExpensesFromSearch("Food", additionalQueryPart);
        assertTrue(expenses.size() > 0);
        assertEquals(expenses.get(0).getName(), "TestExpense");
    }

    @Test
    public void addExpense() {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        Expense addedExpense = dataManager.addExpense(new Expense("TestExpense2", "12/13/2022", "Miscellaneous", 32.24));
        assertNotEquals(-99, addedExpense.getId());
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.contains(addedExpense));
    }

    @Test
    public void getExpense() throws NoExpenseFoundException {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense searchedExpense = dataManager.getExpense(expenses.get(0).getId());
        assertEquals(searchedExpense.getId(), expenses.get(0).getId());
    }

    @Test
    public void updateExpense() {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense expense = expenses.get(0);
        expense.setName("Updated Test Object Name");
        dataManager.updateExpense(expense);
        List<Expense> expenses1 = dataManager.getAllExpenses();
        assertEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
        assertTrue(expenses1.contains(expense));
    }

    @Test
    public void deleteExpense() {
        ExpenseDataManager dataManager = new ExpenseDataSQLite();
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.size() > 0);
        dataManager.deleteExpense(expenses.get(0).getId());
        List<Expense> expenses1 = dataManager.getAllExpenses();
        assertNotEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
    }
}