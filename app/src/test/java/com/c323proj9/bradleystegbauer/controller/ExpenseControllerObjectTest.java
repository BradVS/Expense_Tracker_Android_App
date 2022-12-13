package com.c323proj9.bradleystegbauer.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidIDException;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.data.ExpenseDataSQLite;
import com.c323proj9.bradleystegbauer.model.Expense;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExpenseControllerObjectTest {

    ExpenseController controller;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        controller = new ExpenseControllerObject(new ExpenseDataSQLite(ApplicationProvider.getApplicationContext()));
//        controller = new ExpenseControllerObject(RuntimeEnvironment.systemContext);
        controller.addExpense(new Expense("TestExpense", "12/12/2022", "Food", "12.12"));
        controller.addExpense(new Expense("TestExpense2", "09/11/2022", "Miscellaneous", "34.21"));
        controller.addExpense(new Expense("TestExpense3", "06/21/2020", "Shopping", "22.00"));
    }

    @Test
    public void getAllExpenses() {
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        assertTrue(expenses.get(0).getId() >= 0);
        if (expenses.size() > 1){
            assertNotEquals(expenses.get(0), expenses.get(1));
        }
    }

    @Test
    public void getExpensesFromSearch() throws InvalidInputException {
        List<Expense> allExpenses = controller.getAllExpenses();
        List<Expense> emptyInputSearch = controller.getExpensesFromSearch("", 0, "Food");
        assertNotEquals(allExpenses.size(), emptyInputSearch.size());
        List<Expense> expenses = controller.getExpensesFromSearch("TestExpense", 0, "Food");
        assertTrue(expenses.size() > 0);
        assertNotEquals(expenses.size(), allExpenses.size());
//        assertTrue(expenses.contains(new Expense("TestExpense", "12/12/2022", "Food", 12.12)));
        assertEquals("TestExpense", expenses.get(0).getName());

        expenses = controller.getExpensesFromSearch("12/12/2022", 2, "Food");
        assertTrue(expenses.size() > 0);
        assertNotEquals(expenses.size(), allExpenses.size());
//        assertTrue(expenses.contains(new Expense("TestExpense", "12/12/2022", "Food", 12.12)));
        assertEquals("TestExpense", expenses.get(0).getName());

        expenses = controller.getExpensesFromSearch("12.12", 1, "Food");
        assertTrue(expenses.size() > 0);
        assertNotEquals(expenses.size(), allExpenses.size());
//        assertTrue(expenses.contains(new Expense("TestExpense", "12/12/2022", "Food", 12.12)));
        assertEquals("TestExpense", expenses.get(0).getName());
    }

    @Test
    public void addExpense() throws InvalidInputException {
        Expense addedExpense = controller.addExpense(new Expense("TestExpense2", "12/13/2022", "Miscellaneous", "32.24"));
        assertNotEquals(-99, addedExpense.getId());
//        List<Expense> expenses = controller.getAllExpenses();
//        assertTrue(expenses.contains(addedExpense));
    }

    @Test
    public void addEmptyName() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter name, money value, date, and select a category.");

        controller.addExpense("", "12.12", "12/12/2022", "Food");
    }

    @Test
    public void addEmptyMoney() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter name, money value, date, and select a category.");

        controller.addExpense("Test", "", "12/12/2022", "Food");
    }

    @Test
    public void addEmptyDate() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter name, money value, date, and select a category.");

        controller.addExpense("Test", "12.12", "", "Food");
    }

    @Test
    public void addEmptyCategory() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter name, money value, date, and select a category.");

        controller.addExpense("Test", "12.12", "12/12/2022", "");
    }

    @Test
    public void addInvalidDate() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter a valid date.");

        controller.addExpense("Test", "12.12", "20/20/2022", "Food");
    }

    @Test
    public void addInvalidMoney() throws InvalidInputException {
        exception.expect(InvalidInputException.class);
        exception.expectMessage("Please enter a valid monetary value.");

        controller.addExpense("Test", "asdfas", "12/20/2022", "Food");
    }

    @Test
    public void getExpense() throws InvalidIDException {
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense searchedExpense = controller.getExpense(expenses.get(0).getId());
        assertEquals(searchedExpense.getId(), expenses.get(0).getId());
    }

    @Test
    public void getExpenseNegativeId() throws InvalidIDException {
        exception.expect(InvalidIDException.class);
        exception.expectMessage("ID for search is not valid.");

        controller.getExpense(-99);
    }

    @Test
    public void getExpenseInvalidId() throws InvalidIDException {
        exception.expect(InvalidIDException.class);
        exception.expectMessage("ID for search does not correlate to any entries.");

        controller.getExpense(335667);
    }

    @Test
    public void updateExpense() throws InvalidInputException {
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        Expense expense = expenses.get(0);
        expense.setName("Updated Test Object Name");
        controller.updateExpense(expense);
        List<Expense> expenses1 = controller.getAllExpenses();
        assertEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
//        System.out.println(expense);
//        System.out.println(expenses1.toString());
//        assertTrue(expenses1.contains(expense));
    }

    @Test
    public void deleteExpense() throws InvalidIDException{
        List<Expense> expenses = controller.getAllExpenses();
        assertTrue(expenses.size() > 0);
        controller.deleteExpense(expenses.get(0).getId());
        List<Expense> expenses1 = controller.getAllExpenses();
        assertNotEquals(expenses.size(), expenses1.size());
        assertFalse(expenses1.contains(expenses.get(0)));
    }

    @Test
    public void deleteExpenseNegativeId() throws InvalidIDException {
        exception.expect(InvalidIDException.class);
        exception.expectMessage("ID for deletion is not valid.");

        controller.deleteExpense(335667);
    }

    @Test
    public void deleteExpenseInvalidId() throws InvalidIDException {
        exception.expect(InvalidIDException.class);
        exception.expectMessage("ID for deletion is not valid.");

        controller.deleteExpense(-99);
    }

    @After
    public void tearDown() throws Exception {
        List<Expense> expenses = controller.getAllExpenses();
        for (Expense expense: expenses) {
            controller.deleteExpense(expense.getId());
        }
    }
}