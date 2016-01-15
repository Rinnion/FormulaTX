package com.rinnion.archived.fragment;

import android.database.MatrixCursor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tretyakov on 15.01.2016.
 */
public class ProgramCursor extends MatrixCursor{

    private static String[] names = new String[]{"type", "name", "time", "in_past"};
    private static String[] columns = new String[]{"_id", "type", "name", "time", "in_past"};

    public ProgramCursor() {
        super(columns);
    }

    public void addRow(int type, String name, String time, boolean in_past) {
        super.addRow(new Object[]{type, name, time, in_past});
    }
}
