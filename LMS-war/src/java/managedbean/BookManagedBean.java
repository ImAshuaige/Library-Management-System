/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import com.sun.xml.ws.client.RequestContext;
import entity.Book;
import error.BookNotFoundException;
import error.DeletionErrorException;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.BookSessionBeanLocal;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.primefaces.PrimeFaces;

/**
 *
 * @author 60540
 */
@Named(value = "bookManagedBean")
@RequestScoped
public class BookManagedBean {

    @EJB
    private BookSessionBeanLocal bookSessionBeanLocal;

    private Long bookId;
    private String title;
    private String isbn;
    private String author;
    private List<Book> books;
    private Book selectedBook;
    private String searchText;
    private List<Book> filteredBooks;
    private Integer numOfBooks;
    private String newTitle;
    private String newIsbn;
    private String newAuthor;

    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        books = bookSessionBeanLocal.retrieveAllBooks();
    }

    public/*String*/ void uploadBook() {
        Book b = new Book();
        b.setTitle(getTitle());
        b.setIsbn(getIsbn());
        b.setAuthor(getAuthor());
        bookSessionBeanLocal.createNewBook(b);
        title = null;
        isbn = null;
        author = null;
        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice",
                "The Book is Successfully Uploaded."));

    }

    public void deleteBook(Book book) {
        try {
            bookSessionBeanLocal.deleteBook(book.getId());
            books.remove(book);
        } catch (DeletionErrorException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "You Can't Delete a Book That is Currently in Rental."));
        } catch (BookNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Book Not Found!"));
        }

    }

    public void edit(Book book) {
        selectedBook = book;
        System.out.println("edit is executed");
        //PrimeFaces.current().executeScript("PF('editBookDialog').show()");
    }

    public void saveBook() {
        try {
            selectedBook = bookSessionBeanLocal.retrieveBookByBookId(bookId);
            if (!newTitle.isEmpty()) {
                selectedBook.setTitle(newTitle);
                //System.out.println("title is set");                     
            }
            if (!newIsbn.isEmpty()) {
                selectedBook.setIsbn(newIsbn);
                //System.out.println("isbn is set");  
            }
            if (!newAuthor.isEmpty()) {
                selectedBook.setAuthor(newAuthor);
                //System.out.println("dfdf is set");  
            }
            bookSessionBeanLocal.updateBook(selectedBook);
            //System.out.println("This line executed");
            search();
            selectedBook = null;
            newTitle = null;
            newIsbn = null;
            newAuthor = null;
        } catch (BookNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Book Not Found!"));
        }
        
    }

    public void search() {
        List<Book> resultList = bookSessionBeanLocal.searchBooksByText(searchText);
        books = resultList;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * @return the selectedBook
     */
    public Book getSelectedBook() {
        return selectedBook;
    }

    /**
     * @param selectedBook the selectedBook to set
     */
    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * @return the filteredBooks
     */
    public List<Book> getFilteredBooks() {
        if (searchText != null && !searchText.isEmpty()) {
            return books;
        }
        return filteredBooks;
    }

    /**
     * @param filteredBooks the filteredBooks to set
     */
    public void setFilteredBooks(List<Book> filteredBooks) {
        this.filteredBooks = filteredBooks;
    }

    /**
     * @return the numOfBooks
     */
    public Integer getNumOfBooks() {
        return books.size();
    }

    /**
     * @param numOfBooks the numOfBooks to set
     */
    public void setNumOfBooks(Integer numOfBooks) {
        this.numOfBooks = numOfBooks;
    }

    /**
     * @return the newTitle
     */
    public String getNewTitle() {
        return newTitle;
    }

    /**
     * @param newTitle the newTitle to set
     */
    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    /**
     * @return the newIsbn
     */
    public String getNewIsbn() {
        return newIsbn;
    }

    /**
     * @param newIsbn the newIsbn to set
     */
    public void setNewIsbn(String newIsbn) {
        this.newIsbn = newIsbn;
    }

    /**
     * @return the newAuthor
     */
    public String getNewAuthor() {
        return newAuthor;
    }

    /**
     * @param newAuthor the newAuthor to set
     */
    public void setNewAuthor(String newAuthor) {
        this.newAuthor = newAuthor;
    }

    /**
     * @return the bookId
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}
