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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author 60540
 */
@Stateless
public class BookSessionBean implements BookSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewBook(Book book) {
        em.persist(book);
        em.flush();
        return book.getId();
    }

    @Override
    public Book retrieveBookByTitle(String title) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM Book b WHERE b.title = :title");
        query.setParameter("title", title);
        if (query.getResultList().isEmpty()) {
            throw new BookNotFoundException();
        } else if (query.getResultList().size() > 1) {
            return (Book) query.getResultList().get(0);
        } else {
            return (Book) query.getSingleResult();
        }
    }

    @Override
    public Book retrieveBookByBookId(Long id) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM Book b WHERE b.id = :id");
        query.setParameter("id", id);

        if (query.getResultList().isEmpty()) {
            throw new BookNotFoundException();
        } else {
            return (Book) query.getSingleResult();
        }
    }

    @Override
    public Book retrieveBookByIsbn(String isbn) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn");
        query.setParameter("isbn", isbn);

        if (query.getResultList().isEmpty()) {
            throw new BookNotFoundException();
        } else if (query.getResultList().size() > 1) {
            return (Book) query.getResultList().get(0);
        } else {
            return (Book) query.getSingleResult();
        }
    }

    @Override
    public List<Book> retrieveAllBooks() {
        Query query = em.createQuery("SELECT b FROM Book b");
        return query.getResultList();
    }

    @Override
    public void updateBook(Book book) {
        Book existingBook = em.find(Book.class, book.getId());
        //System.out.println("passed in  book : " + book.getTitle());
        //System.out.println("passed in  book : " + book.getIsbn());
        //System.out.println("passed in  book : " + book.getAuthor());
        if (existingBook != null) {
            if (book.getTitle() != null) {
                existingBook.setTitle(book.getTitle());
            }
            if (book.getIsbn() != null) {
                existingBook.setAuthor(book.getIsbn());
            }
            if (book.getAuthor() != null) {
                existingBook.setIsbn(book.getAuthor());
            }
            em.merge(existingBook);
        }

    }

    @Override
    public void deleteBook(Long bookId) throws DeletionErrorException, BookNotFoundException {
        Book book = em.find(Book.class, bookId);
        if (book != null) {
            if (book.getAvailability() == false) {
                throw new DeletionErrorException();
            } else {
                em.remove(book);
            }
        } else {
            throw new BookNotFoundException();
        }
    }

    @Override
    public List<Book> searchBooksByText(String searchText) {
        String jpql = "SELECT b FROM Book b WHERE b.title LIKE :searchText "
                + "OR b.author LIKE :searchText OR b.isbn LIKE :searchText";
        TypedQuery<Book> query = em.createQuery(jpql, Book.class);
        query.setParameter("searchText", "%" + searchText + "%");
        return query.getResultList();
    }
}
