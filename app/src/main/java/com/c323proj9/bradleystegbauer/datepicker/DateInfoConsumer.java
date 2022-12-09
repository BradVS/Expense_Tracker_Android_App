package com.c323proj9.bradleystegbauer.datepicker;

public interface DateInfoConsumer {
    /**
     * Accepts the date String that is passed into it for use by the imeplementing class.
     * @param dateString yyyy-MM-dd format String of the date that is passed in.
     */
    void getDateString(String dateString);
}
