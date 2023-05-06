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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 60540
 */
@Stateless
public class MemberSessionBean implements MemberSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewMember(MemberEntity member) {
        em.persist(member);
        em.flush();
        return member.getId();
    }

    @Override
    public MemberEntity retrieveMemberByIdentityNumber(String idNumber) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNo = :idNumber");
        query.setParameter("idNumber", idNumber);

        if (query.getResultList().isEmpty()) {
            throw new MemberNotFoundException();
        } else {
            return (MemberEntity) query.getSingleResult();
        }
    }

    @Override
    public List<MemberEntity> retrieveAllMembers() {
        Query query = em.createQuery("SELECT m FROM MemberEntity m");
        return query.getResultList();
    }

    @Override
    public void checkUniqueId(String id) throws NonUniqueIdException {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNo = :id");
        query.setParameter("id", id);
        if (!query.getResultList().isEmpty()) {
            throw new NonUniqueIdException();
        }
    }

}
