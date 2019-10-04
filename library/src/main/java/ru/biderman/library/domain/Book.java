package ru.biderman.library.domain;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authorList;

    @Column(name = "title")
    private String title;

    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.EAGER)
    @JoinTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id", nullable = false)
    private List<Comment> commentList = new ArrayList<>();

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

    public List<Comment> getCommentList() {
        return commentList;
    }
}
