/* $Id: DAOException.java,v 1.1 2007/01/31 12:02:52 sorina Exp $ */

package com.moodmedia.adcom.dao;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * DAOException class encapsulates all the
 * exceptions that can occure at DAO layer level
 *
 * @author Ciprian Dragoi
 * @version $Revision: 1.1 $ $Date: 2007/01/31 12:02:52 $
 */
public class DAOException extends Exception {

    /***/
    private Throwable root;

    /**
     * Constructs an <code>Exception</code> with no specified detail message.
     */
    public DAOException() {
        super();
    }

    /**
     * Constructs an <code>Exception</code> with the specified detail message.
     *
     * @param s the detail message.
     */
    public DAOException(String s) {
        super(s);
    }

    public DAOException(Throwable t) {
        this.root = t;
    }

    public DAOException(Throwable root, String message) {
        super(message);
        this.root = root;
    }

    public Throwable getRoot() {
        return root;
    }

    /**
     * Prints this <code>Throwable</code> and its backtrace to the specified
     * print writer.
     *
     * @since JDK1.1
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (root != null) {
            s.println("----------------------------ROOT CAUSE--------------------------------");
            root.printStackTrace(s);
            s.println("----------------------------END ROOT CAUSE----------------------------");
        }
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (root != null) {
            System.err.println("----------------------------ROOT CAUSE--------------------------------");
            root.printStackTrace();
            System.err.println("----------------------------END ROOT CAUSE----------------------------");
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (root != null) {
            s.println("----------------------------ROOT CAUSE--------------------------------");
            root.printStackTrace(s);
            s.println("----------------------------END ROOT CAUSE----------------------------");
        }
    }

}
