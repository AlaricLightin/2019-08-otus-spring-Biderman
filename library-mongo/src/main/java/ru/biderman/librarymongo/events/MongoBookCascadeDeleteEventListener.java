package ru.biderman.librarymongo.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.repositories.CommentRepository;

@Component
public class MongoBookCascadeDeleteEventListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    public MongoBookCascadeDeleteEventListener(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        commentRepository.deleteByBook_Id(event.getSource().get("_id").toString());
    }
}
