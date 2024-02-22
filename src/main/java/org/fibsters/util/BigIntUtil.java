package org.fibsters.util;

import java.math.BigInteger;

public class BigIntUtil {

    public static BigInteger toBigInt(int number) {
        return BigInteger.valueOf(number);
    }

    public static BigInteger toBigInt(double number) {
        return BigInteger.valueOf((long) number);
    }

}
