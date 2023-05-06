/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.MemberEntity;
import error.MemberNotFoundException;
import error.NonUniqueIdException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface MemberSessionBeanLocal {
    public Long createNewMember (MemberEntity member);
    public MemberEntity retrieveMemberByIdentityNumber(String idNumber) throws MemberNotFoundException;

    public List<MemberEntity> retrieveAllMembers();

    public void checkUniqueId(String id) throws NonUniqueIdException;
    
}
