package com.c323proj9.bradleystegbauer.view.activities.datareceivers;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

public interface TotalExpensesReceiver {
    /**
     * Takes in a total of all expenses and uses the information.
     * @param total a BigDecimal of all the Expenses being used summed together
     */
    void getTotalExpenses(@NonNull BigDecimal total);
}
