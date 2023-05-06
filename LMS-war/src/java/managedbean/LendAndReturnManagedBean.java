/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.LendAndReturn;
import entity.MemberEntity;
import error.BookNotAvailableException;
import error.BookNotFoundException;
import error.LendingNotFoundException;
import error.MemberNotFoundException;
import error.ReturnDateEarlierThanLendingDateException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.LendAndReturnSessionBeanLocal;

/**
 *
 * @author 60540
 */
@Named(value = "lendAndReturnManagedBean")
@SessionScoped
public class LendAndReturnManagedBean implements Serializable {

    @EJB
    private LendAndReturnSessionBeanLocal lendAndReturnSessionBeanLocal;

    private String memberId;
    private String isbn;
    private Date currentDate;
    private Date returnDate;
    private BigDecimal fineAmount;
    private LendAndReturn lending;
    private Date minDate;
    private Date maxDate;
    private List<LendAndReturn> currentLendings;
    private Integer currLendingSize;

    public LendAndReturnManagedBean() {
        minDate = new Date();
        maxDate = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void lendBook() {
        //to be fixed after this week's lab :) with prime faces to pass messages to the front end
        try {
            Long lendingId = lendAndReturnSessionBeanLocal.createNewLending(getMemberId(), getIsbn(), getCurrentDate());
            memberId = null;
            isbn = null;
            currentDate = null;
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "A Lending Record is Successfully Created"));
        } catch (BookNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book Not Found!"));

        } catch (MemberNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member Not Found!"));

        } catch (BookNotAvailableException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Book is Currently Unavailable!"));
            //book not available exception is not thrown 
        }

    }

    public String calculateFineAmountAndRedirect() {
        try {
            lending = lendAndReturnSessionBeanLocal.retrieveLendingByMemberIdAndBookIsbn(memberId, isbn);
            lendAndReturnSessionBeanLocal.checkValidReturnDate(lending.getLendDate(), returnDate);
            currentDate = lending.getLendDate();
            fineAmount = lendAndReturnSessionBeanLocal.calculateFineAmount(returnDate, lending.getId());
            if (fineAmount.compareTo(BigDecimal.ZERO) > 0) {
                return "payFine.xhtml?faces-redirect=true";
            } else {
                lendAndReturnSessionBeanLocal.returnLending(lending.getId(), returnDate);
                memberId = null;
                isbn = null;
                currentDate = null;
                returnDate = null;
                fineAmount = null;
                lending = null;
                return "returnConfirmation.xhtml?faces-redirect=true";
            }
        } catch (LendingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Lending Not Found!"));
        } catch (ReturnDateEarlierThanLendingDateException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please Enter an Valid Return Date"));
        } catch (BookNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book Not Found!"));
        } catch (MemberNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member Not Found!"));
        }
        return "returnBook.xhtml";
    }

    public String finishPayment() {
        try {
            System.out.println("ISBN: " + isbn);
            System.out.println("MemberId: " + memberId);
            System.out.println("return date: " + returnDate.toString());
            lendAndReturnSessionBeanLocal.returnLending(lending.getId(), returnDate);//which one is null?? lendingGetId or returnDate???
            //reset everything to null
            memberId = null;
            isbn = null;
            currentDate = null;
            returnDate = null;
            fineAmount = null;
            lending = null;

            return "returnConfirmation.xhtml?faces-redirect=true";
        } catch (LendingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lending Not Found!", null));
        } catch (BookNotFoundException | MemberNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an Error. Please Try Again.", null));
        }
        return "payFine.xhtml?faces-redirect=true";
    }

    /**
     * @return the memberId
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the returnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the fineAmount
     */
    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    /**
     * @param fineAmount the fineAmount to set
     */
    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    /**
     * @return the minDate
     */
    public Date getMinDate() {
        return minDate;
    }

    /**
     * @param minDate the minDate to set
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * @return the maxDate
     */
    public Date getMaxDate() {
        return maxDate;
    }

    /**
     * @param maxDate the maxDate to set
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    /**
     * @return the currLendings
     */
    public Integer getCurrLendingSize() {
        //logic to be fix such that only those are currently in effect will be return
        return lendAndReturnSessionBeanLocal.retrieveAllCurrentLendings().size();
    }

    /**
     * @param currLendings the currLendings to set
     */
    public void setCurrLendingSize(Integer currLendings) {
        this.currLendingSize = currLendings;
    }

    /**
     * @return the currentLendings
     */
    public List<LendAndReturn> getCurrentLendings() {
         return lendAndReturnSessionBeanLocal.retrieveAllCurrentLendings();
    }

    /**
     * @param currentLendings the currentLendings to set
     */
    public void setCurrentLendings(List<LendAndReturn> currentLendings) {
        this.currentLendings = currentLendings;
    }

}
