/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Book;
import entity.LendAndReturn;
import entity.MemberEntity;
import error.BookNotAvailableException;
import error.BookNotFoundException;
import error.LendingNotFoundException;
import error.MemberNotFoundException;
import error.ReturnDateEarlierThanLendingDateException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LendAndReturnSessionBean implements LendAndReturnSessionBeanLocal {

    @EJB
    private BookSessionBeanLocal bookSessionBeanLocal;

    @EJB
    private MemberSessionBeanLocal memberSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewLending(String memberIdNumber, String isbn, Date currentDate) throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {
        MemberEntity member = memberSessionBeanLocal.retrieveMemberByIdentityNumber(memberIdNumber);
        Book book = bookSessionBeanLocal.retrieveBookByIsbn(isbn);

        if (book.getAvailability() == true) {
            LendAndReturn lending = new LendAndReturn();
            lending.setBook(book);
            lending.setLendDate(currentDate);
            lending.setMemberEntity(member);
            lending.setBookId(book.getId());
            lending.setMemberId(member.getId());
            book.setAvailability(Boolean.FALSE);//will the book be persisted?
            em.persist(lending);
            return lending.getId();
        } else {
            throw new BookNotAvailableException();
        }
    }

    @Override
    public LendAndReturn retrieveLendingByMemberIdAndBookIsbn(String idNo, String isbn) throws LendingNotFoundException {
        Query query = em.createQuery("SELECT l FROM LendAndReturn l WHERE l.memberEntity.identityNo = :idNumber AND l.book.isbn = :isbn AND l.returnDate = null");
        query.setParameter("idNumber", idNo);
        query.setParameter("isbn", isbn);

        if (query.getResultList().isEmpty()) {
            throw new LendingNotFoundException();
        } else {
            return (LendAndReturn) query.getSingleResult();
        }
        //more than 1 result is return here because the member lend the boook twice omg, thats a hugu bug. 
    }

    //later design such in a way that only after pay the fine amount then can proceed to return lending, i.e. if returnLending() is called, meaning that fine is paid 
    @Override
    public BigDecimal calculateFineAmount(Date currentDate, Long lendingId) {
        LendAndReturn lending = em.find(LendAndReturn.class, lendingId);
        Date lendDate = lending.getLendDate();
        LocalDate lendingDate = lendDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate returnDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysLate = ChronoUnit.DAYS.between(lendingDate.plusWeeks(2), returnDate);
        if (daysLate <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal fine = new BigDecimal(daysLate * 0.5);
            lending.setFineAmount(fine);//automatically synchronized with the database 
            return fine;
        }
    }

    @Override
    public LendAndReturn returnLending(Long lendingId, Date returnDate) throws LendingNotFoundException, BookNotFoundException, MemberNotFoundException {
        LendAndReturn lending = em.find(LendAndReturn.class, lendingId);
        Book book = em.find(Book.class, lending.getBookId());
        book.setAvailability(Boolean.TRUE);
        lending.setReturnDate(returnDate);
        return lending;
    }

    @Override
    public List<LendAndReturn> retrieveAllLendings() {
        Query query = em.createQuery("SELECT l FROM LendAndReturn l");
        return query.getResultList();
    }

    @Override
    public void checkValidReturnDate(Date start, Date end) throws ReturnDateEarlierThanLendingDateException {
        if (end.before(start)) {
            throw new ReturnDateEarlierThanLendingDateException();
        }
    }
    @Override
    public List<LendAndReturn> retrieveAllCurrentLendings() {
        Query query = em.createQuery("SELECT l FROM LendAndReturn l WHERE l.returnDate = null");
        return query.getResultList();
    }
}
