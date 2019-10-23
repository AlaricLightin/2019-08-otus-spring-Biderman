package ru.biderman.librarymongo.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.time.ZonedDateTime;
import java.util.List;

import static ru.biderman.librarymongo.testutils.TestData.*;

@ChangeLog(order = "001-test")
public class InitMongoTestDB {

    @ChangeSet(order = "001", id = "initComments", author = "biderman")
    public void initComments(MongoTemplate mongoTemplate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_WITH_COMMENT_TITLE));
        List<Book> bookList = mongoTemplate.find(query, Book.class);
        if (bookList.size() == 1) {
            Comment comment = new Comment(USER, ZonedDateTime.now(), EXISTING_COMMENT_TEXT, bookList.get(0));
            mongoTemplate.save(comment);
        }
    }
}
