package com.c323proj9.bradleystegbauer.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final DateInfoConsumer dateInfoConsumer;

    public DatePickerFragment(DateInfoConsumer dateInfoConsumer) {
        this.dateInfoConsumer = dateInfoConsumer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month++;
        String dateString = new StringBuilder().append(year).append("-").append((month < 10 ? "0" + month : month)).append("-").append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)).toString();
        this.dateInfoConsumer.getDateString(dateString);
    }
}
