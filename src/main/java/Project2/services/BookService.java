package Project2.services;

import Project2.models.Book;
import Project2.models.Person;
import Project2.repositories.BookRepository;
import Project2.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAllBooks(PageRequest pageRequest) {
        return bookRepository.findAll(pageRequest).getContent();
    }

    public List<Book> findAllSortedBooks(PageRequest pageRequest) {
        return bookRepository.findAllByOrderByYear(pageRequest).getContent();
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public Person checkBookOwner(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteBookOwner(int id) {
       Optional<Book> book = bookRepository.findById(id);
       if (book.isPresent()) {
           Hibernate.initialize(book.get().getOwner());
           Hibernate.initialize(book.get().getCreatedAt());
           book.get().setOwner(null);
           book.get().setCreatedAt(null);
       }
    }

    @Transactional
    public void assignBookToPerson(int person_id, int book_id) {
        Optional<Person> person = personRepository.findById(person_id);
        Optional<Book> book = bookRepository.findById(book_id);
        if (person.isPresent() && book.isPresent()) {
            Hibernate.initialize(book.get().getOwner());
            Hibernate.initialize(book.get().getCreatedAt());
            book.get().setOwner(person.get());
            person.get().getBookList().add(book.get());
            book.get().setCreatedAt(new Date());
        }
    }

    public List<Book> getBooksBySearch(String name) {
        return bookRepository.findByNameStartingWith(name);
    }
}
