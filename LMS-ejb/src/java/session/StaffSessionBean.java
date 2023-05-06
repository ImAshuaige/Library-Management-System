/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Staff;
import error.InvalidLoginException;
import error.NonUniqueUsernameException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 60540
 */
@Stateless
public class StaffSessionBean implements StaffSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewStaff(Staff staff) {
        em.persist(staff);
        em.flush();
        return staff.getId();
    }

    @Override
    public Staff retrieveStaffByLoginDetails(String username, String password) throws InvalidLoginException {
        Query query = em.createQuery("SELECT s FROM Staff s WHERE s.userName = :username AND s.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        if (query.getResultList().isEmpty()) {
            throw new InvalidLoginException();
        } else {
            return (Staff)query.getSingleResult();
        }
    }
    
    @Override
    public void checkUniqueUsername(String username) throws NonUniqueUsernameException {
    Query query = em.createQuery("SELECT s FROM Staff s WHERE s.userName = :username");
    query.setParameter("username", username);
    //System.out.println("username is " + username);
    //if (query.getResultList().isEmpty()) {
        //System.out.println("this username is unique");
    //}
    if (!query.getResultList().isEmpty()) {
        //System.out.println("this username is non-unique");
        throw new NonUniqueUsernameException();
    }
}
    
    
}
