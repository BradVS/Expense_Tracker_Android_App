package com.c323proj9.bradleystegbauer.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Object for holding & managing instances of Expenses during runtime.
 */
public class Expense {
    private final int id;
    @NonNull
    private String name, date, category;
    private double money;

    public Expense(@NonNull String name, @NonNull String date, @NonNull String category, double money) {
        this.id = -99;
        this.name = name;
        this.date = date;
        this.category = category;
        this.money = money;
    }

    public Expense(int id, @NonNull String name, @NonNull String date, @NonNull String category, double money) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.category = category;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public double getMoney() {
        return money;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @NonNull
    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id == expense.id && Double.compare(expense.money, money) == 0 && name.equals(expense.name) && date.equals(expense.date) && category.equals(expense.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, category, money);
    }
}
