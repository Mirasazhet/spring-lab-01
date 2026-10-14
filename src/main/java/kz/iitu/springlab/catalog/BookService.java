package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    // every book when author is null, otherwise only the books whose author contains the fragment
    public List<Book> findAll(String author) {
        return repository.findAll().stream()
                .filter(b -> author == null || author.isBlank()
                        || b.author().toLowerCase().contains(author.toLowerCase()))
                .sorted(Comparator.comparing(Book::id))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        return repository.save(new Book(null, book.title(), book.author(), book.year()));
    }

    // replaces the record if it exists; an empty Optional means 404
    public Optional<Book> replace(long id, Book book) {
        return repository.findById(id)
                .map(old -> repository.save(new Book(id, book.title(), book.author(), book.year())));
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }

    // variant 2: the books published in the given year
    public List<Book> findByYear(int year) {
        return findAll(null).stream()
                .filter(b -> b.year() == year)
                .toList();
    }
}
