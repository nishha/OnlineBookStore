package edu.leicester.co2103s2.part1s2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Author {
    @Id
    @JsonProperty("id")
    @GeneratedValue
    private Long id;
    private String name;
    private int birthyear;
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Book> books;


    public Author() {
        this.books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void addnewBook(Book book){
        this.books.add(book);
        book.getAuthors().add(this);
    }

    public void deleteBook(Book book){
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public String toString() {
        return "Authors{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthyear=" + birthyear +
                ", nationality='" + nationality + '\'' +
                ", books=" + books +
                '}';
    }
}
