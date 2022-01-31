package com.example.bee_honeyt;

import com.jjoe64.graphview.DefaultLabelFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeAsXAxisLabelFormatter extends DefaultLabelFormatter
{
    protected final String mFormat;

    public TimeAsXAxisLabelFormatter(String format)
    {
        mFormat = format;
    }

    @Override
    public String formatLabel(double value, boolean isValueX)
    {
        if (isValueX)
        {
            Date d = new Date(0, 0, 0, (int)value, 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat(mFormat);
            return dateFormat.format(d);
        }
        else
        {
            return super.formatLabel(value, isValueX);
        }
    }
}