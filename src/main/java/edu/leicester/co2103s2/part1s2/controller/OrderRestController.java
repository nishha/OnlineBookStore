package edu.leicester.co2103s2.part1s2.controller;

import edu.leicester.co2103s2.part1s2.ErrorInfo;
import edu.leicester.co2103s2.part1s2.domain.Book;
import edu.leicester.co2103s2.part1s2.domain.Order;
import edu.leicester.co2103s2.part1s2.repos.BookRepository;
import edu.leicester.co2103s2.part1s2.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;

@RestController

public class OrderRestController {
    @Autowired
    OrderRepository repo;

    @Autowired
    BookRepository bookRepo;

    @GetMapping("/orders") //endpoint #14
    public ResponseEntity<List<Order>> listAllOrders() {
        List<Order>orders = repo.findAll();
        if (orders.isEmpty()){
            return new ResponseEntity(new ErrorInfo("no orders found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    //endpoint #15
    @PostMapping("/orders") //create an author //to place the object in the uri path, requestbody, convert from json document to java object
    public ResponseEntity<?> createOrder(@RequestBody Order order, UriComponentsBuilder builder){
        if (repo.existsById(order.getId())){
            return new ResponseEntity<>(new ErrorInfo("This order: " + order.getId() + " already exists."), HttpStatus.CONFLICT);
        }
        repo.save(order);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(builder.path("/orders/{id}").buildAndExpand(order.getId()).toUri());
        return new ResponseEntity<>(httpHeaders,HttpStatus.CREATED);

    }

    @GetMapping("/orders/{id}") //endpoint #16
    public ResponseEntity<?> getOrderbyID(@PathVariable("id") Long id){
        Order orders = repo.findById(id).orElse(null);
        if (orders == null){
            return new ResponseEntity<>(new ErrorInfo("This order with id: " + id + " does not exist"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Order>(orders, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}") //endpoint #17
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestBody Order orders){
        Order anOrder = repo.findById(id).orElse(null);
        if (anOrder == null){
            return new ResponseEntity(new ErrorInfo("This order with id: " + id + " does not exist"), HttpStatus.NOT_FOUND);
        }

        anOrder.setCustomerName(orders.getCustomerName());
        anOrder.setDatetime(orders.getDatetime());
        repo.save(anOrder);
        return new ResponseEntity(anOrder, HttpStatus.OK);

    }

    @GetMapping("/orders/{id}/books") //endpoint #18
    public ResponseEntity<?> booksInOrder(@PathVariable("id") Long id){
        Order orders = repo.findById(id).orElse(null);
        if (orders == null) {
            return new ResponseEntity(new ErrorInfo("This order of id: " + id + " does not exist"), HttpStatus.NOT_FOUND);
        }
        List<Book> books = orders.getBooksList();

        return new ResponseEntity(books, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/books") //endpoint #19
    public ResponseEntity<?> addBooktoOrder(@PathVariable("id") Long id, @RequestBody Book book) {
        Order thisOrder = repo.findById(id).orElse(null);
        if (thisOrder == null) {
            return new ResponseEntity<>(new ErrorInfo("Order not found "), HttpStatus.NOT_FOUND);
        }
        thisOrder.getBooksList().add(book);
        repo.save(thisOrder);
        return new ResponseEntity<>(thisOrder, HttpStatus.OK);

    }

    @DeleteMapping("/orders/{id}/books/{isbn}") //endpoint #20
    public ResponseEntity<?> deleteBookfromOrder(@PathVariable("id") Long id, @PathVariable("isbn") String isbn){
        Book thisBook = bookRepo.findById(isbn).orElse(null);
        Order thisOrder = repo.findById(id).orElse(null);
        if (thisBook == null){
            return new ResponseEntity(new ErrorInfo("Book with isbn" + isbn + " does not exist"), HttpStatus.NOT_FOUND);
        }
        if (thisOrder == null){
            return new ResponseEntity(new ErrorInfo("This order " + id + " does not exist"), HttpStatus.NOT_FOUND);
        }

        thisOrder.getBooksList().remove(thisBook);
        repo.save(thisOrder);

        return new ResponseEntity("This book " + isbn + " has been deleted from this order", HttpStatus.OK);


    }




}
