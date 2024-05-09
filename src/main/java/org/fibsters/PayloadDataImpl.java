package org.fibsters;

import org.fibsters.interfaces.PayloadData;

public class PayloadDataImpl implements PayloadData {

    public int[] calcFibNumbersUpTo;
    public String[] outputLocations;

    @Override
    public int[] getCalcFibNumbersUpTo() {
        return calcFibNumbersUpTo;
    }

    @Override
    public void setCalcFibNumbersUpTo(int[] calcFibNumbersUpTo) {
        this.calcFibNumbersUpTo = calcFibNumbersUpTo;
    }

}