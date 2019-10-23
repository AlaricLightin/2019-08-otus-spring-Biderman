package ru.biderman.librarymongo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.biderman.librarymongo.domain.Book;

public class BookRepositoryImpl implements BookRepositoryCustom {
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void updateGenre(String oldGenreText, String newGenreText) {
        if (newGenreText.equals(oldGenreText))
            return;

        Criteria criteria = Criteria.where("genres").is(oldGenreText);
        Update update = new Update().set("genres.$", newGenreText);
        mongoOperations.updateMulti(Query.query(criteria), update, Book.class);
    }

    @Override
    public boolean isAuthorUsed(String authorId) {
        Criteria criteria = Criteria.where("authorList").is(authorId);
        return mongoOperations.exists(Query.query(criteria), Book.class);
    }
}
