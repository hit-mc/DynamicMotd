package com.keuin.dynamicmotd.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {
    public static String getStackTrace(Exception e) {
        var writer1 = new StringWriter();
        var writer2 = new PrintWriter(writer1);
        e.printStackTrace(writer2);
        return writer1.toString();
    }
}
