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
public class DeletionErrorException extends Exception {

    /**
     * Creates a new instance of <code>DeletionErrorException</code> without
     * detail message.
     */
    public DeletionErrorException() {
    }

    /**
     * Constructs an instance of <code>DeletionErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeletionErrorException(String msg) {
        super(msg);
    }
}
