package edu.leicester.co2103s2.part1s2.repos;

import edu.leicester.co2103s2.part1s2.domain.Author;
import edu.leicester.co2103s2.part1s2.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByAuthors(Author author);


}
