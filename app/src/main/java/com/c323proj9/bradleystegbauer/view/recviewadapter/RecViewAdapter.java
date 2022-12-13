package com.c323proj9.bradleystegbauer.view.recviewadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.c323proj9.bradleystegbauer.R;
import com.c323proj9.bradleystegbauer.controller.ExpenseController;
import com.c323proj9.bradleystegbauer.controller.ExpenseControllerObject;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidIDException;
import com.c323proj9.bradleystegbauer.controller.exceptions.InvalidInputException;
import com.c323proj9.bradleystegbauer.data.ExpenseDataSQLite;
import com.c323proj9.bradleystegbauer.datepicker.DateInfoConsumer;
import com.c323proj9.bradleystegbauer.datepicker.DatePickerFragment;
import com.c323proj9.bradleystegbauer.model.Expense;
import com.c323proj9.bradleystegbauer.view.activities.datareceivers.TotalExpensesReceiver;

import java.math.BigDecimal;
import java.util.List;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ItemViewHolder> {
    private final Context context;
    private final FragmentActivity fragmentActivity;
    private List<Expense> expenses;
    private final ExpenseController controller;
    private String editCategory = "";
    private PopupWindow popupWindow;
    private final TotalExpensesReceiver totalExpensesReceiver;
    public RecViewAdapter(Context context, FragmentActivity fragmentActivity, TotalExpensesReceiver totalExpensesReceiver) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.controller = new ExpenseControllerObject(new ExpenseDataSQLite(this.context));
        this.expenses = controller.getAllExpenses();
        this.totalExpensesReceiver = totalExpensesReceiver;
        this.totalExpensesReceiver.getTotalExpenses(totalExpenseSum());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_card_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        String cat = this.expenses.get(position).getCategory();
        switch (cat) {
            case "Food":
                holder.categoryIcon.setImageResource(R.drawable.ic_baseline_food_24);
                break;
            case "Transportation":
                holder.categoryIcon.setImageResource(R.drawable.ic_baseline_car_24);
                break;
            case "Shopping":
                holder.categoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                break;
            default:
                holder.categoryIcon.setImageResource(R.drawable.ic_baseline_misc_24);
                break;
        }
        holder.name.setText(this.expenses.get(position).getName());
        holder.date.setText(this.expenses.get(position).getDateString());
        holder.money.setText(String.valueOf(this.expenses.get(position).getMoney()));
        holder.category.setText(this.expenses.get(position).getCategory());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder.getAdapterPosition());
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(holder.getAdapterPosition(), view);
            }
        });
    }

    /**
     * Sets up a screen for editing an item in the list
     * @param position The position of the entry in the list
     * @param view The view
     */
    private void editItem(final int position, View view) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.edit_popup_layout, null);
        final EditText namePop = popupView.findViewById(R.id.nameEdit_edittext_popup);
        final EditText moneyPop = popupView.findViewById(R.id.moneyEdit_edittext_popup);
        final EditText datePop = popupView.findViewById(R.id.dateEdit_edittext_popup);
        Spinner catPop = popupView.findViewById(R.id.categoryChoice_spinner_popup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.list_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catPop.setAdapter(adapter);
        Button save = popupView.findViewById(R.id.editButton_button_popup);
        namePop.setText(expenses.get(position).getName());
        moneyPop.setText(String.valueOf(expenses.get(position).getMoney()));
        datePop.setText(expenses.get(position).getDateString());
        datePop.setShowSoftInputOnFocus(false);
        datePop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    DialogFragment dialogFragment = new DatePickerFragment(new DateInfoConsumer() {
                        @Override
                        public void getDateString(String dateString) {
                            datePop.setText(dateString);
                        }
                    });
                    dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "datePicker");
                }
            }
        });
        catPop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdit(position, namePop.getText().toString(), moneyPop.getText().toString(), datePop.getText().toString());
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * Saves the edit made to the database if it is valid.
     * @param position The position of the item in the list
     * @param name The name of the item in the list.
     * @param money The money value of the item in the list.
     * @param date The date value of the item in the list.
     */
    private void saveEdit(int position, String name, String money, String date) {
        try{
            Expense updatedExpense = new Expense(expenses.get(position).getId(), name, date, editCategory, money);
            updatedExpense = controller.updateExpense(updatedExpense);
            totalExpensesReceiver.addToTotalExpenses(updatedExpense.getMoney().subtract(expenses.get(position).getMoney()));
            expenses.set(position, updatedExpense);
            notifyItemChanged(position);
        }catch (NumberFormatException e){
            Toast.makeText(context, "Input a valid monetary value.", Toast.LENGTH_SHORT).show();
        }catch (InvalidInputException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return this.expenses.size();
    }

    /**
     * Function for performing a search and updating the dataset in the recycler view
     * @param input the user's search term that they used
     * @param type the type of search that the user selected
     * @param category the category that was selected at the time of the search
     */
    @SuppressLint("NotifyDataSetChanged")
    public void search(String input, int type, String category){
        List<Expense> filteredExpenses;
        try {
            filteredExpenses = controller.getExpensesFromSearch(input, type, category);
            expenses = filteredExpenses;
        } catch (InvalidInputException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (expenses.isEmpty()){
            Toast.makeText(context, "No expenses found.", Toast.LENGTH_SHORT).show();
        }
        totalExpensesReceiver.getTotalExpenses(totalExpenseSum());
        notifyDataSetChanged();
    }

    /**
     * Deletes an item from the database and the list for the RecyclerView
     * @param position The position of the item being deleted.
     */
    public void deleteItem(int position){
        try{
//            db.execSQL("DELETE FROM expenses WHERE id = " + expenses.get(position).getId() + ";");
            Expense expense = controller.deleteExpense(expenses.get(position).getId());
            expenses.remove(position);
            Toast.makeText(context, expense.getName() + " deleted!", Toast.LENGTH_SHORT).show();
            totalExpensesReceiver.subtractFromTotalExpenses(expense.getMoney());
            notifyItemRemoved(position);
        } catch (SQLException e){
            Toast.makeText(context, "Error: Problem deleting item from database.", Toast.LENGTH_SHORT).show();
        } catch (InvalidIDException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadAllItems(){
        expenses = controller.getAllExpenses();
        totalExpensesReceiver.getTotalExpenses(totalExpenseSum());
        notifyDataSetChanged();
    }

    private BigDecimal totalExpenseSum(){
        BigDecimal sum = new BigDecimal("0");
        for (Expense e: this.expenses) {
            sum = sum.add(e.getMoney());
        }
        return sum;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name, money, category, date;
        ImageView categoryIcon;
        ImageButton edit, delete;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_textview_itemview);
            money = itemView.findViewById(R.id.money_textview_itemview);
            category = itemView.findViewById(R.id.category_textview_itemview);
            date = itemView.findViewById(R.id.date_textview_itemview);
            categoryIcon = itemView.findViewById(R.id.icon_imageview_itemview);
            edit = itemView.findViewById(R.id.editButton_imagebutton_itemview);
            delete = itemView.findViewById(R.id.deleteButton_imagebutton_itemview);
        }
    }
}
