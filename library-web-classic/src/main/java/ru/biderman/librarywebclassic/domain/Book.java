package ru.biderman.librarywebclassic.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Author.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authorList;

    @Column(name = "title")
    private String title;

    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    public static Book createNewBook(List<Author> authorList, String title, Set<Genre> genreList) {
        return new Book(0, authorList, title, genreList);
    }

    public Book() {
    }

    public Book(long id, List<Author> authorList, String title, Set<Genre> genres) {
        this.id = id;
        this.authorList = authorList;
        this.title = title;
        this.genres = genres;
    }

    public long getId() {
        return id;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public String getTitle() {
        return title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
