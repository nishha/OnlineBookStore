package edu.leicester.co2103s2.part1s2.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private Timestamp datetime;

    private String customerName;
    @ManyToMany
    @JoinTable(
            name = "order_book",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_isbn")
    )
    private List<Book> booksList;

    public List<Book> getBooksList() {
        return booksList;
    }

    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addNewBook(Book book){
        booksList.add(book);
        book.getOrders().add(this);
    }

    public void deleteBook(Book book){
        booksList.remove(book);
        book.getOrders().remove(this);
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", customerName='" + customerName + '\'' +
                ", booksList=" + booksList +
                '}';
    }
}
