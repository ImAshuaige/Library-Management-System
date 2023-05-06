/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Staff;
import error.InvalidLoginException;
import error.NonUniqueUsernameException;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface StaffSessionBeanLocal {
    public Long createNewStaff (Staff staff);
    public Staff retrieveStaffByLoginDetails(String username, String password) throws InvalidLoginException;

    public void checkUniqueUsername(String username) throws NonUniqueUsernameException;
    
}
