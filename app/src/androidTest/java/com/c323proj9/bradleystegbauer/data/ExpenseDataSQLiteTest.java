package com.c323proj9.bradleystegbauer.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.c323proj9.bradleystegbauer.data.exceptions.NoExpenseFoundException;
import com.c323proj9.bradleystegbauer.model.Expense;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExpenseDataSQLiteTest {

    ExpenseDataManager dataManager;

    @Before
    public void setUp(){
        dataManager = new ExpenseDataSQLite(ApplicationProvider.getApplicationContext());
        dataManager.addExpense(new Expense("TestExpense", "12/12/2022", "Food", "12.12"));
    }

    @Test
    public void getAllExpenses() {
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
        List<Expense> expenses = dataManager.getExpensesFromSearch("Food", additionalQueryPart);
        assertTrue(expenses.size() > 0);
        assertEquals(expenses.get(0).getName(), "TestExpense");
    }

    @Test
    public void addExpense() {
        Expense addedExpense = dataManager.addExpense(new Expense("TestExpense2", "12/13/2022", "Miscellaneous", "32.24"));
        assertNotEquals(-99, addedExpense.getId());
//        List<Expense> expenses = dataManager.getAllExpenses();
//        assertTrue(expenses.contains(addedExpense));
    }

    @Test
    public void getExpense() throws NoExpenseFoundException {
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense searchedExpense = dataManager.getExpense(expenses.get(0).getId());
        assertEquals(searchedExpense.getId(), expenses.get(0).getId());
    }

    @Test
    public void updateExpense() {
        List<Expense> expenses = dataManager.getAllExpenses();
//        System.out.println(expenses.toString());
        assertTrue(expenses.size() > 0);
        Expense expense = expenses.get(0);
        expense.setName("Updated Test Object Name");
//        System.out.println(expense);
        dataManager.updateExpense(expense);
        List<Expense> expenses1 = dataManager.getAllExpenses();
        assertEquals(expenses.size(), expenses1.size());
//        System.out.println(expenses1.toString());
//        assertFalse(expenses1.contains(expenses.get(0)));
//        assertTrue(expenses1.contains(expense));
    }

    @Test
    public void deleteExpense() throws NoExpenseFoundException {
        List<Expense> expenses = dataManager.getAllExpenses();
        assertTrue(expenses.size() > 0);
        dataManager.deleteExpense(expenses.get(0).getId());
        List<Expense> expenses1 = dataManager.getAllExpenses();
        assertNotEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
    }

    @After
    public void tearDown() throws Exception {
        List<Expense> expenses = dataManager.getAllExpenses();
        for (Expense expense: expenses) {
            dataManager.deleteExpense(expense.getId());
        }
    }
}