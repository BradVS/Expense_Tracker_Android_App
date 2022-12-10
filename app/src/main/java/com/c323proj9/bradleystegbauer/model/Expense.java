package com.c323proj9.bradleystegbauer.model;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Object for holding & managing instances of Expenses during runtime.
 */
public class Expense {
    private final int id;
    @NonNull
    private String name, category;
    @NonNull
    private LocalDate date;
    @NonNull
    private BigDecimal money;

    public Expense(@NonNull String name, @NonNull String date, @NonNull String category, @NonNull String money) throws NumberFormatException {
        this.id = -99;
        this.name = name;
        if (date.split("/").length > 1){
            this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }else{
            this.date = LocalDate.parse(date);
        }
        this.category = category;
        this.money = new BigDecimal(money);
        if (this.money.compareTo(new BigDecimal("0")) <= -1){
            throw new NumberFormatException("Input a number greater than 0");
        }
    }

    public Expense(@NonNull String name, @NonNull String date, @NonNull String category, @NonNull BigDecimal money) {
        this.id = -99;
        this.name = name;
        if (date.split("/").length > 1){
            this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }else{
            this.date = LocalDate.parse(date);
        }
        this.category = category;
        this.money = money;
        if (this.money.compareTo(new BigDecimal("0")) <= -1){
            throw new NumberFormatException("Input a number greater than 0");
        }
    }

    public Expense(int id, @NonNull String name, @NonNull String date, @NonNull String category, @NonNull String money) throws NumberFormatException {
        this.id = id;
        this.name = name;
        if (date.split("/").length > 1){
            this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }else{
            this.date = LocalDate.parse(date);
        }
        this.category = category;
        this.money = new BigDecimal(money);
        if (this.money.compareTo(new BigDecimal("0")) <= -1){
            throw new NumberFormatException("Input a number greater than 0");
        }
    }

    public Expense(int id, @NonNull String name, @NonNull String date, @NonNull String category, @NonNull BigDecimal money) {
        this.id = id;
        this.name = name;
        if (date.split("/").length > 2){
            this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }else{
            this.date = LocalDate.parse(date);
        }
        this.category = category;
        this.money = money;
        if (this.money.compareTo(new BigDecimal("0")) <= -1){
            throw new NumberFormatException("Input a number greater than 0");
        }
    }

    public Expense(@NonNull String name, @NonNull String category, @NonNull LocalDate date, @NonNull BigDecimal money) {
        this(-99, name, category, date, money);
    }

    public Expense(int id, @NonNull String name, @NonNull String category, @NonNull LocalDate date, @NonNull BigDecimal money) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.money = money;
        if (this.money.compareTo(new BigDecimal("0")) <= -1){
            throw new NumberFormatException("Input a number greater than 0");
        }
    }

    public Expense(@NonNull String name, @NonNull String category, @NonNull LocalDate date, @NonNull String money) {
        this(-99, name, category, date, new BigDecimal(money));
    }

    public Expense(int id, @NonNull String name, @NonNull String category, @NonNull LocalDate date, @NonNull String money) {
        this(id, name, category, date, new BigDecimal(money));
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public String getDateString() {
        return this.date.toString();
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @NonNull
    public BigDecimal getMoney() {
        return money;
    }

    @NonNull
    public String getMoneyString() {
        return this.money.toString();
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setDate(@NonNull String date) {
        if (date.split("/").length > 1){
            this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }else{
            this.date = LocalDate.parse(date);
        }
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public void setMoney(@NonNull BigDecimal money) {
        this.money = money;
    }

    public void setMoney(String money) throws NumberFormatException {
        this.money = new BigDecimal(money);
    }

    @NonNull
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
        return id == expense.id && name.equals(expense.name) && date.equals(expense.date) && category.equals(expense.category) && money.equals(expense.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, category, money);
    }
}
