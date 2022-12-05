package com.c323proj9.bradleystegbauer.controller;

import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidIDException;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.model.Expense;

import java.util.List;

public interface ExpenseController {
    /**
     * Returns a List object of all expenses in the DB.
     * @return List of all Expenses
     */
    List<Expense> getAllExpenses();

    /**
     * Takes a search term and a category, and returns a List of Expense items based on the inputs. Always filters by category.
     * @param searchInput The search input from the user. Can be empty.
     * @param type An int determining the type of the search input. 0 for name, 1 for money, and 2 for date.
     * @param category The category from which to retrieve Expense items from.
     * @return List of Expense items based on search input.
     * @throws InvalidInputException - thrown if a search input is invalid based on the type.
     */
    List<Expense> getExpensesFromSearch(String searchInput, int type, String category) throws InvalidInputException;
    Expense addExpense(Expense expense) throws InvalidInputException;
    Expense addExpense(String name, String moneyString, String date, String category) throws InvalidInputException;

    /**
     * Takes an Expense id and returns the item from the DB.
     * @param id - id of the Expense to return.
     * @return Expense with the same id as the input.
     * @throws InvalidIDException - thrown if the id is a negative, or if the id did not return any item.
     */
    Expense getExpense(int id) throws InvalidIDException;
    Expense updateExpense(Expense expense) throws InvalidInputException;

    /**
     * Takes an Expense id and deletes the item from the DB.
     * @param id - id of the Expense to delete.
     * @return Expense that was deleted from the DB.
     * @throws InvalidIDException - thrown if the id is a negative, or if the id did not correspond to any item.
     */
    Expense deleteExpense(int id) throws InvalidIDException;
}
