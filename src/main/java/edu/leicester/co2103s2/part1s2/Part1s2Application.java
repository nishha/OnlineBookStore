package edu.leicester.co2103s2.part1s2;

import edu.leicester.co2103s2.part1s2.domain.Author;
import edu.leicester.co2103s2.part1s2.domain.Book;
import edu.leicester.co2103s2.part1s2.domain.Order;
import edu.leicester.co2103s2.part1s2.repos.AuthorRepository;
import edu.leicester.co2103s2.part1s2.repos.BookRepository;
import edu.leicester.co2103s2.part1s2.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Part1s2Application implements CommandLineRunner {

    @Autowired
    AuthorRepository authorRepo;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    OrderRepository orderRepo;

    Timestamp dateTime = new Timestamp(System.currentTimeMillis());

    public static void main(String[] args) {
        SpringApplication.run(Part1s2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Author> authorsList = new ArrayList<>();

        Author a1 = new Author();
        a1.setName("JK Rowling");
        a1.setBirthyear(1995);
        a1.setNationality("British");

        a1=authorRepo.save(a1);
        authorsList.add(a1);


        Author a2 = new Author();
        a2.setName("Khalid Hosseini");
        a2.setBirthyear(1980);
        a2.setNationality("American");

        a2 = authorRepo.save(a2);
        authorsList.add(a2);

        List<Book> booksList = new ArrayList<>();

        Book book1 = new Book();
        book1.setTitle("Harry Potter");
        book1.setPublicationYear(2000);
        book1.setPrice(7.99);

        book1=bookRepo.save(book1);
        booksList.add(book1);

        Book book2 = new Book();
        book2.setTitle("And the mountains echoed");
        book2.setPublicationYear(2013);
        book2.setPrice(9.99);

        book2=bookRepo.save(book2);
        booksList.add(book2);

        a1.getBooks().add(book1);
        a1=authorRepo.save(a1);

        a2.getBooks().add(book2);
        a2 = authorRepo.save(a2);


        book2.setAuthors(List.of(a2));
        book1.setAuthors(List.of(a1));
        book1=bookRepo.save(book1);
        book2=bookRepo.save(book2);

        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();

        order1.setCustomerName("John");
        order1.setDatetime(dateTime);
        order1.setBooksList(List.of(book1, book2));
        orderList.add(order1);
        order1 = orderRepo.save(order1);











    }




}
