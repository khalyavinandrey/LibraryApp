package Project2.services;

import Project2.models.Book;
import Project2.models.Person;
import Project2.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAllPerson() {
        return repository.findAll();
    }

    public Person findPersonById(int id) {
        Optional<Person> foundPerson = repository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void createPerson(Person person) {
        repository.save(person);
    }

    @Transactional
    public void updatePerson(Person person, int id) {
        person.setPersonId(id);
        repository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        repository.deleteById(id);
    }

    public List<Book> getBookList(int id) {
        Optional<Person> person = repository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBookList());
            person.get().getBookList().forEach(book ->
                    book.setOverdue(new Date().getTime() - book.getCreatedAt().getTime() >= 864000000)
            );
            return person.get().getBookList();
        } else {
            return Collections.emptyList();
        }
    }
}
