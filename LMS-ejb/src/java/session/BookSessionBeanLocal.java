/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Book;
import error.BookNotFoundException;
import error.DeletionErrorException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface BookSessionBeanLocal {

    public Long createNewBook(Book book);

    public Book retrieveBookByTitle(String title) throws BookNotFoundException;

    public List<Book> retrieveAllBooks();

    public Book retrieveBookByIsbn(String isbn) throws BookNotFoundException;

    public void deleteBook(Long bookId) throws DeletionErrorException, BookNotFoundException;

    public List<Book> searchBooksByText(String searchText);

    public void updateBook(Book book);

    public Book retrieveBookByBookId(Long id) throws BookNotFoundException;

}
