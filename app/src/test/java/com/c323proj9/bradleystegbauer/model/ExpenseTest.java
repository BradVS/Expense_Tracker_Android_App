package com.c323proj9.bradleystegbauer.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class ExpenseTest {
    Expense expense1;
    Expense expense2;
    Expense noIdExpense;

    @Before
    public void setUp() throws Exception {
        expense1 = new Expense(0, "TestExpense1", "11/12/2021", "Food", 12.12);
        expense2 = new Expense(1, "TestExpense2", "09/11/2022", "Miscellaneous", 34.21);
        noIdExpense = new Expense("TestExpense3", "06/21/2020", "Shopping", 22.00);
    }

    @Test
    public void getId() {
        assertEquals(expense1.getId(), 0);
        assertEquals(expense2.getId(), 1);
        assertEquals(noIdExpense.getId(), -99);
    }

    @Test
    public void getName() {
        assertNotNull(expense1.getName());
        assertNotNull(expense2.getName());
        assertNotNull(noIdExpense.getName());
        assertEquals(expense1.getName(), "TestExpense1");
        assertEquals(expense2.getName(), "TestExpense2");
        assertEquals(noIdExpense.getName(), "TestExpense3");
    }

    @Test
    public void getDate() {
        assertNotNull(expense1.getDate());
        assertNotNull(expense2.getDate());
        assertNotNull(noIdExpense.getDate());
        assertEquals(expense1.getDate(), "11/12/2021");
        assertEquals(expense2.getDate(), "09/11/2022");
        assertEquals(noIdExpense.getDate(), "06/21/2020");
    }

    @Test
    public void getCategory() {
        assertNotNull(expense1.getCategory());
        assertNotNull(expense2.getCategory());
        assertNotNull(noIdExpense.getCategory());
        assertEquals(expense1.getCategory(), "Food");
        assertEquals(expense2.getCategory(), "Miscellaneous");
        assertEquals(noIdExpense.getCategory(), "Shopping");
    }

    @Test
    public void getMoney() {
        assertEquals(expense1.getMoney(), 12.12, .0000001);
        assertEquals(expense2.getMoney(), 34.21, .0000001);
        assertEquals(noIdExpense.getMoney(), 22.00, .0000001);
    }

    @Test
    public void setName() {
        expense1.setName("ExpenseOneNewName");
        assertNotEquals(expense1.getName(), "TestExpense1");
        assertEquals(expense1.getName(), "ExpenseOneNewName");
        expense2.setName("ExpenseTwoNewName");
        assertNotEquals(expense2.getName(), "TestExpense2");
        assertEquals(expense2.getName(), "ExpenseTwoNewName");
    }

    @Test
    public void setDate() {
        expense1.setDate("11/12/2022");
        assertNotEquals(expense1.getDate(), "11/12/2021");
        assertEquals(expense1.getDate(), "11/12/2022");
        expense2.setDate("08/11/2011");
        assertNotEquals(expense2.getDate(), "09/11/2022");
        assertEquals(expense2.getDate(), "08/11/2011");
    }

    @Test
    public void setCategory() {
        expense1.setCategory("Miscellaneous");
        assertNotEquals(expense1.getCategory(), "Food");
        assertEquals(expense1.getCategory(), "Miscellaneous");
        expense2.setCategory("Shopping");
        assertNotEquals(expense2.getCategory(), "Miscellaneous");
        assertEquals(expense2.getCategory(), "Shopping");
    }

    @Test
    public void setMoney() {
        expense1.setMoney(10.41);
        assertNotEquals(expense1.getMoney(), 12.12, .0000001);
        assertEquals(expense1.getMoney(), 10.41, .0000001);
        expense2.setMoney(9.87);
        assertNotEquals(expense2.getMoney(), 34.21, .0000001);
        assertEquals(expense2.getMoney(), 9.87, .0000001);
    }

    @Test
    public void testToString() {
        assertEquals(expense1.toString(), "Expense{" +
                "id=" + expense1.getId() +
                ", name='" + expense1.getName() + '\'' +
                ", date='" + expense1.getDate() + '\'' +
                ", category='" + expense1.getCategory() + '\'' +
                ", money=" + expense1.getMoney() +
                '}');
        assertEquals(expense2.toString(), "Expense{" +
                "id=" + expense2.getId() +
                ", name='" + expense2.getName() + '\'' +
                ", date='" + expense2.getDate() + '\'' +
                ", category='" + expense2.getCategory() + '\'' +
                ", money=" + expense2.getMoney() +
                '}');
    }

    @Test
    public void testEquals() {
        Expense expenseOneCopy = new Expense(0, "TestExpense1", "11/12/2021", "Food", 12.12);
        assertEquals(expense1, expenseOneCopy);
        Expense expenseTwoCopy = new Expense(1, "TestExpense2", "09/11/2022", "Miscellaneous", 34.21);
        assertEquals(expense2, expenseTwoCopy);
        Expense noIdExpenseCopy = new Expense("TestExpense3", "06/21/2020", "Shopping", 22.00);
        assertEquals(noIdExpense, noIdExpenseCopy);
        assertNotEquals(expense1, expense2);
        assertNotEquals(expense2, noIdExpense);
    }

    @Test
    public void testHashCode() {
        assertEquals(expense1.hashCode(), Objects.hash(expense1.getId(), expense1.getName(), expense1.getDate(), expense1.getCategory(), expense1.getMoney()));
        assertEquals(expense2.hashCode(), Objects.hash(expense2.getId(), expense2.getName(), expense2.getDate(), expense2.getCategory(), expense2.getMoney()));
    }
}