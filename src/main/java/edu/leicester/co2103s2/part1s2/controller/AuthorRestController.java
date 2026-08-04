package edu.leicester.co2103s2.part1s2.controller;

import edu.leicester.co2103s2.part1s2.ErrorInfo;
import edu.leicester.co2103s2.part1s2.domain.Author;
import edu.leicester.co2103s2.part1s2.domain.Book;
import edu.leicester.co2103s2.part1s2.repos.AuthorRepository;
import edu.leicester.co2103s2.part1s2.repos.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController

public class AuthorRestController {

    @Autowired
    AuthorRepository repo;

    @Autowired
    BookRepository bookRepo;

    @GetMapping("/authors") //get ALL authors ENDPOINT #1 DONE (testing success)
    public ResponseEntity<List<Author>> listAllAuthors() {
        List<Author> authors = repo.findAll();

        if (authors.isEmpty()){
            return new ResponseEntity(new ErrorInfo("There are no authors"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Author>>(authors, HttpStatus.OK);
    }


    @PostMapping(value= "/authors", consumes = MediaType.APPLICATION_JSON_VALUE) //create an author ENDPOINT #2
    public ResponseEntity<?> createAuthor(@RequestBody Author author, UriComponentsBuilder builder){
        if (repo.existsById(author.getId())){
            return new ResponseEntity(new ErrorInfo("this author:" + author.getId() + " already exists"), HttpStatus.CONFLICT);
        }
        repo.save(author);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(builder.path("/authors/{id}").buildAndExpand(author.getId()).toUri());
        return new ResponseEntity<>(httpHeaders,HttpStatus.CREATED);
    }

    @GetMapping("/authors/{id}") //get author by their id ENDPOINT #3
    public ResponseEntity<?> getAuthorByID(@PathVariable("id") Long id){
        Author author = repo.findById(id).orElse(null);
        if (author == null) {
            return new ResponseEntity<>(new ErrorInfo("this author with this id" + id + " does not exist"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Author>(author, HttpStatus.OK);
    }

    @PutMapping("/authors/{id}") //update author using their id ENDPOINT #4
    public ResponseEntity<?> updateAuthor(@PathVariable("id") Long id, @RequestBody Author authors){
        Author thisAuthor = repo.findById(id).orElse(null);
        if (thisAuthor == null){
            return new ResponseEntity<>(new ErrorInfo("Author with this id " + id + "  cannot be found"),
                    HttpStatus.NOT_FOUND);
        }

        thisAuthor.setName(authors.getName());
        thisAuthor.setBirthyear(authors.getBirthyear());
        thisAuthor.getBooks().clear();
        thisAuthor.getBooks().addAll(authors.getBooks());

        repo.save(thisAuthor); //save the updated author (found by id) to the author repository
        return new ResponseEntity<Author>(thisAuthor, HttpStatus.OK);

    }

    @DeleteMapping("/authors/{id}") //delete author by id ENDPOINT #5
    public ResponseEntity<?> deleteAuthorbyId(@PathVariable("id") Long id) {
        Author thisAuthor = repo.findById(id).orElse(null);
        if (thisAuthor == null) {
            return new ResponseEntity(new ErrorInfo("This author with the id " + id + " does not exist."), HttpStatus.NOT_FOUND);
        }

        repo.delete(thisAuthor);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/authors/{id}/books") //list all books by a specific author ENDPOINT #6
    public ResponseEntity<?> listBookbyAuthor(@PathVariable("id") Long id){
        Author thisAuthor = repo.findById(id).orElse(null);

        if (thisAuthor == null) {
            return new ResponseEntity(new ErrorInfo("This author with the id " + id + " does not exist."), HttpStatus.NOT_FOUND);
        }
        List<Book> books = bookRepo.findByAuthors(thisAuthor);

        if (books.isEmpty()) {
            return new ResponseEntity<>(new ErrorInfo("No books found for author " + id), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }











}
