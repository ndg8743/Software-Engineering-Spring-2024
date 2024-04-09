package org.fibsters;

import org.fibsters.interfaces.PayloadData;

public class PayloadDataImpl implements PayloadData {
    public int[] CalcFibNumbersUpTo;
    public String[] outputLocations;
    @Override
    public int[] getCalcFibNumbersUpTo() {
        return CalcFibNumbersUpTo;
    }

    @Override
    public void setCalcFibNumbersUpTo(int[] calcFibNumbersUpTo) {
        CalcFibNumbersUpTo = calcFibNumbersUpTo;
    }
}