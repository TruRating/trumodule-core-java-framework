package com.trurating.device;

import java.util.Scanner;

/**
 * Created by Paul on 07/03/2016.
 */
public class ConsoleManager {

    public int getKey() {
        Scanner s = new Scanner(System.in);
        int rv = -1;
        try {
            rv = s.nextInt();
        }
        catch (java.util.InputMismatchException e) {
        }
        return rv;
    }
}
