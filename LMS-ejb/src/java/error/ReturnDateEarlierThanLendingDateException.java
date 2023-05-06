/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author 60540
 */
public class ReturnDateEarlierThanLendingDateException extends Exception {

    /**
     * Creates a new instance of
     * <code>ReturnDateEarlierThanLendingDateException</code> without detail
     * message.
     */
    public ReturnDateEarlierThanLendingDateException() {
    }

    /**
     * Constructs an instance of
     * <code>ReturnDateEarlierThanLendingDateException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ReturnDateEarlierThanLendingDateException(String msg) {
        super(msg);
    }
}
