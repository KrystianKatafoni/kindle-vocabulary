package com.katafoni.util;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

public class TestJsonUtils {

    public static CustomComparator getTimestampJsonCustomComparator() {
        return new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("timestamp", (o1, o2) -> true)
        );
    }
}
