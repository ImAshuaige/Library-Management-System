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
public class NonUniqueUsernameException extends Exception{

    /**
     * Creates a new instance of <code>NonUniqueUsernameException</code> without
     * detail message.
     */
    public NonUniqueUsernameException() {
    }

    /**
     * Constructs an instance of <code>NonUniqueUsernameException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NonUniqueUsernameException(String msg) {
        super(msg);
    }
}
