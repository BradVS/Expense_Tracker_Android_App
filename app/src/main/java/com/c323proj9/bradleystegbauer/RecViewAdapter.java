package com.c323proj9.bradleystegbauer;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ItemViewHolder> {
    Context context;
    private SQLiteDatabase db;
    private ArrayList<Expense> expenses;
    String editCategory = "";
    PopupWindow popupWindow;
    public RecViewAdapter(Context context) {
        this.context = context;
        expenses = new ArrayList<>();
        db = context.openOrCreateDatabase("ExpensesDB",  MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("SELECT * FROM expenses ORDER BY date(date);", null);
        int idCol = cursor.getColumnIndex("id");
        int nameCol = cursor.getColumnIndex("name");
        int moneyCol = cursor.getColumnIndex("money");
        int dateCol = cursor.getColumnIndex("date");
        int categoryCol = cursor.getColumnIndex("category");
        cursor.moveToFirst();
        if (cursor!=null && cursor.getCount()> 0){
            do{
                expenses.add(new Expense(cursor.getInt(idCol), cursor.getString(nameCol), cursor.getString(dateCol),
                        cursor.getString(categoryCol), cursor.getDouble(moneyCol)));
            }while(cursor.moveToNext());
        }
        if(expenses.isEmpty()){
            Toast.makeText(context, "No expenses found.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
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
        if (cat.equals("Food")){
            holder.categoryIcon.setImageResource(R.drawable.ic_baseline_food_24);
        } else if(cat.equals("Transportation")){
            holder.categoryIcon.setImageResource(R.drawable.ic_baseline_car_24);
        }else if(cat.equals("Shopping")){
            holder.categoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        }else{
            holder.categoryIcon.setImageResource(R.drawable.ic_baseline_misc_24);
        }
        holder.name.setText(this.expenses.get(position).getName());
        holder.date.setText(this.expenses.get(position).getDate());
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
        String[] dateArray = expenses.get(position).getDate().split("-");
        String correctDate = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
        datePop.setText(correctDate);
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
        if (name.equals("") || money.equals("") || date.equals("") || editCategory.equals("")){
            Toast.makeText(context, "Error: Fill all boxes to save edit.", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            double newMoney = Double.parseDouble(money);
            if (!dateFormatCheck(date)){
                Toast.makeText(context, "Error: Invalid date format. Use mm/dd/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] dateArray = date.split("/");
            String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
            db.execSQL("UPDATE expenses SET name = '" + name +"', money = '" + money +"', date = '" + dateFormat +"', category = '" +
                    editCategory +"' WHERE id = " + expenses.get(position).getId() + ";");
            expenses.get(position).setName(name);
            expenses.get(position).setMoney(newMoney);
            expenses.get(position).setCategory(editCategory);
            expenses.get(position).setDate(dateFormat);
            notifyItemChanged(position);
            Toast.makeText(context, "Edit saved.", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            Toast.makeText(context, "Error: Enter a valid monetary amount to save.", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context, "Error: Could not update database.", Toast.LENGTH_SHORT).show();
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
        expenses = new ArrayList<>();
        db = context.openOrCreateDatabase("ExpensesDB",  MODE_PRIVATE,null);
        String additionalQueryPart = "";
        if (type == 0){
            additionalQueryPart = " AND name = '" + input + "'";
        }else if(type == 2){
            if (input.equals("")){
                additionalQueryPart = "";
            }else{
                String[] dateArray = input.split("/");
                String dateFormat = dateArray[2]+"-"+dateArray[0]+"-"+dateArray[1];
                additionalQueryPart = " AND date = '" + dateFormat +"'";
            }
        }else if(type == 1){
            additionalQueryPart = " AND money = '" + input + "'";
        }
        if (input.equals("")){ //makes it so the search only does a filter if nothing was input
            additionalQueryPart = "";
        }
        Cursor cursor = db.rawQuery("SELECT * FROM expenses WHERE category = '"+category+"' "+additionalQueryPart+" ORDER BY date(date);", null);
        int idCol = cursor.getColumnIndex("id");
        int nameCol = cursor.getColumnIndex("name");
        int moneyCol = cursor.getColumnIndex("money");
        int dateCol = cursor.getColumnIndex("date");
        int categoryCol = cursor.getColumnIndex("category");
        cursor.moveToFirst();
        if (cursor!=null && cursor.getCount()> 0){
            do{
                expenses.add(new Expense(cursor.getInt(idCol), cursor.getString(nameCol), cursor.getString(dateCol),
                        cursor.getString(categoryCol), cursor.getDouble(moneyCol)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        if (expenses.isEmpty()){
            Toast.makeText(context, "No expenses found.", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }

    /**
     * Deletes an item from the database and the list for the RecyclerView
     * @param position The position of the item being deleted.
     */
    public void deleteItem(int position){
        try{
            db.execSQL("DELETE FROM expenses WHERE id = " + expenses.get(position).getId() + ";");
            expenses.remove(position);
            Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show();
            notifyItemRemoved(position);
        } catch (SQLException e){
            Toast.makeText(context, "Error: Problem deleting item from database.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks to see if the date string entered is a valid date.
     * @param date String holding the date
     * @return True if valid, false otherwise
     */
    private boolean dateFormatCheck(String date) {
        String[] dateSplit = date.split("/");
//        first check if split was into 3 parts
        if (dateSplit.length != 3){
            return false;
        }
//        check if month, day, and year are correct length
        if (dateSplit[0].length() != 2 || dateSplit[1].length() != 2 ||dateSplit[2].length() != 4){
            return false;
        }
//        try making them into integers
        try{
            int month = Integer.parseInt(dateSplit[0]);
            int day = Integer.parseInt(dateSplit[1]);
            int year = Integer.parseInt(dateSplit[2]);

//            check if they are within bounds
//            start with month, year, and day total bounds
            if(month < 1 || month > 12 || day < 1 || day > 31 || year < 1){
                return false;
            }
//            check for leap years
            if(year % 4 != 0 && month == 2 && day > 28){
//                fail condition (common year)
                return false;
            } else if(year % 100 != 0 && month == 2 && day > 29){
//                fail condition (leap year)
                return false;
            } else if(year % 400 != 0 && month == 2 && day > 28){
//                fail condition (common year)
                return false;
            } else if(month == 2 && day > 29){
//                fail condition (leap year)
                return false;
            }
//            if it made it past this, leap year rules have been followed (based on Gregorian calendar)
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView money;
        TextView category;
        TextView date;
        ImageView categoryIcon;
        ImageButton edit;
        ImageButton delete;
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
