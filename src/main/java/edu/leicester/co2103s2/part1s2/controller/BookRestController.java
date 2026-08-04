package edu.leicester.co2103s2.part1s2.controller;

import edu.leicester.co2103s2.part1s2.ErrorInfo;
import edu.leicester.co2103s2.part1s2.domain.Author;
import edu.leicester.co2103s2.part1s2.domain.Book;
import edu.leicester.co2103s2.part1s2.domain.Order;
import edu.leicester.co2103s2.part1s2.repos.AuthorRepository;
import edu.leicester.co2103s2.part1s2.repos.BookRepository;
import edu.leicester.co2103s2.part1s2.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;


    @RestController

    public class BookRestController {

        @Autowired
        BookRepository repo;

        @Autowired
        AuthorRepository authorRepo;

        @Autowired
        OrderRepository orderRepo;

        @GetMapping("/books") //endpoint #7
        public ResponseEntity<List<Book>> listAllBooks() {
            List<Book> books = repo.findAll();
            if (books.isEmpty()) {
                return new ResponseEntity(new ErrorInfo("There are no books found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
        }

        @PostMapping("/books") //endpoint #8
        public ResponseEntity<?> createBook(@RequestBody Book book, UriComponentsBuilder builder){
            if (repo.existsById(book.getISBN())){
                return new ResponseEntity<>(new ErrorInfo("this book of ISBN " + book.getISBN() + " already exists"), HttpStatus.CONFLICT);

            }
            repo.save(book);
            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.setLocation(builder.path("/books/{isbn}").buildAndExpand(book.getISBN()).toUri());

            return new ResponseEntity<>(httpHeaders,HttpStatus.CREATED);


        }

        @GetMapping("/books/{isbn}") //endpoint #9
        public ResponseEntity<?> getBookByISBN(@PathVariable("isbn") String isbn){
            Book books = repo.findById(isbn).orElse(null);
            if (books == null){
                return new ResponseEntity<>(new ErrorInfo("this book with this isbn" + isbn + " does not exist"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Book>(books, HttpStatus.OK);
        }

        @PutMapping("/books/{isbn}") //endpoint #10
        public ResponseEntity<?> updateBook(@PathVariable("isbn") String isbn, @RequestBody Book books){
            Book thisBook = repo.findById(isbn).orElse(null);
            if (thisBook == null){
                return new ResponseEntity<>(new ErrorInfo("Book with isbn: " + isbn + "  does not exist"),
                        HttpStatus.NOT_FOUND);
            }

            thisBook.setTitle(books.getTitle());
            thisBook.setPublicationYear(books.getPublicationYear());
            thisBook.getAuthors().clear();
            thisBook.getAuthors().addAll(books.getAuthors());

            repo.save(thisBook); //save the updated hotel (found by id) to the hotel repository
            return new ResponseEntity<Book>(thisBook, HttpStatus.OK);

        }

        @DeleteMapping("/books/{isbn}") //endpoint #11
        public ResponseEntity<?> deleteBookbyISBN(@PathVariable("isbn") String isbn) {
            Book thisBook = repo.findById(isbn).orElse(null);
            if (thisBook == null) {
                return new ResponseEntity(new ErrorInfo("book with the ISBN: " + isbn + " does not exist."), HttpStatus.NOT_FOUND);
            }

            repo.delete(thisBook);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @GetMapping("/books/{ISBN}/authors") //endpoint #12
        public ResponseEntity<?> getAuthorsofBook(@PathVariable("ISBN") String isbn){
            Book thisBook = repo.findById(isbn).orElse(null);
            if (thisBook == null) {
                return new ResponseEntity(new ErrorInfo("book with the ISBN: " + isbn + " does not exist."), HttpStatus.NOT_FOUND);
            }
            List<Author> authors = authorRepo.findByBooks(thisBook);
            if (authors.isEmpty()) {
                return new ResponseEntity<>(new ErrorInfo("No authors found for this book " + isbn), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }

        @GetMapping("/books/{isbn}/orders") //endpoint #13
        public ResponseEntity<?> getOrdersWithBook(@PathVariable("isbn") String isbn){
            Book books = repo.findById(isbn).orElse(null);
            if (books == null){
                return new ResponseEntity<>(new ErrorInfo("This books " + isbn + " does not exist"),HttpStatus.NOT_FOUND);

            }
            List<Order> theseOrders = new ArrayList<>();
            List<Order> ordersList = orderRepo.findAll();
            for (Order orders: ordersList){
                for (Book book: orders.getBooksList()){
                    if (book.getISBN().equals(isbn)){
                        theseOrders.add(orders); //add them all to a list (those containing the matching isbn)
                    }
                }
            }

            if (theseOrders.isEmpty()){
                return new ResponseEntity<>(new ErrorInfo("There are no existing orders containing book of isbn: " + isbn), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(theseOrders, HttpStatus.OK);
        }




}
