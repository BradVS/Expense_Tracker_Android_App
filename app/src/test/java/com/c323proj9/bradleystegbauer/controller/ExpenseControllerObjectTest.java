package com.c323proj9.bradleystegbauer.controller;

import static org.junit.Assert.*;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.data.ExpenseDataManager;
import com.c323proj9.bradleystegbauer.data.ExpenseDataSQLite;
import com.c323proj9.bradleystegbauer.model.Expense;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpenseControllerObjectTest {

    @Before
    public void setUp() throws Exception {
        ExpenseController controller = new ExpenseControllerObject();
        controller.addExpense(new Expense("TestExpense", "12/12/2022", "Food", 12.12));
    }

    @Test
    public void getAllExpenses() {
        ExpenseController controller = new ExpenseControllerObject();
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        assertTrue(expenses.get(0).getId() >= 0);
        if (expenses.size() > 1){
            assertNotEquals(expenses.get(0), expenses.get(1));
        }
    }

    @Test
    public void getExpensesFromSearch() {
        ExpenseController controller = new ExpenseControllerObject();
        List<Expense> expenses = controller.getExpensesFromSearch("TestExpense", 0, "Food");
        assertTrue(expenses.size() > 0);
        assertTrue(expenses.contains(new Expense("TestExpense", "12/12/2022", "Food", 12.12)));
        assertEquals("TestExpense", expenses.get(0).getName());
    }

    @Test
    public void addExpense() throws InvalidInputException {
        ExpenseController controller = new ExpenseControllerObject();
        Expense addedExpense = controller.addExpense(new Expense("TestExpense2", "12/13/2022", "Miscellaneous", 32.24));
        assertNotEquals(-99, addedExpense.getId());
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.contains(addedExpense));
    }

    @Test
    public void getExpense() {
        ExpenseController controller = new ExpenseControllerObject();
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense searchedExpense = controller.getExpense(expenses.get(0).getId());
        assertEquals(searchedExpense.getId(), expenses.get(0).getId());
    }

    @Test
    public void updateExpense() {
        ExpenseController controller = new ExpenseControllerObject();
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense expense = expenses.get(0);
        expense.setName("Updated Test Object Name");
        controller.updateExpense(expense);
        List<Expense> expenses1 = controller.getAllExpenses();
        assertEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
        assertTrue(expenses1.contains(expense));
    }

    @Test
    public void deleteExpense() {
        ExpenseController controller = new ExpenseControllerObject();
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        controller.deleteExpense(expenses.get(0).getId());
        List<Expense> expenses1 = controller.getAllExpenses();
        assertNotEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
    }
}