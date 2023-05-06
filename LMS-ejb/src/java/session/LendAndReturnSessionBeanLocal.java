/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.LendAndReturn;
import error.BookNotAvailableException;
import error.BookNotFoundException;
import error.LendingNotFoundException;
import error.MemberNotFoundException;
import error.ReturnDateEarlierThanLendingDateException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface LendAndReturnSessionBeanLocal {

    public Long createNewLending(String memberIdNumber, String isbn, Date currentDate) throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException;

    public LendAndReturn retrieveLendingByMemberIdAndBookIsbn(String idNo, String isbn) throws LendingNotFoundException;

    public BigDecimal calculateFineAmount(Date currentDate, Long lendingId);

    public LendAndReturn returnLending(Long lendingId, Date returnDate) throws LendingNotFoundException, BookNotFoundException, MemberNotFoundException;

    public List<LendAndReturn> retrieveAllLendings();

    public void checkValidReturnDate(Date start, Date end) throws ReturnDateEarlierThanLendingDateException;

    public List<LendAndReturn> retrieveAllCurrentLendings();

   
}
