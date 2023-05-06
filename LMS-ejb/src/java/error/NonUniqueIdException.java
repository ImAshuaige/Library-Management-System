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
public class NonUniqueIdException extends Exception{

    /**
     * Creates a new instance of <code>NonUniqueIdException</code> without
     * detail message.
     */
    public NonUniqueIdException() {
    }

    /**
     * Constructs an instance of <code>NonUniqueIdException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NonUniqueIdException(String msg) {
        super(msg);
    }
}
