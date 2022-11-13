package com.jpmc.theater.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingUtil {
    public static BigDecimal getBigDecimal(double price) {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.UP);
    }
}
