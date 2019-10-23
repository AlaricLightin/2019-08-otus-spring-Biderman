package ru.biderman.librarymongo.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.google.common.collect.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;

import java.util.Arrays;
import java.util.Collections;

@ChangeLog(order = "001")
public class InitMongoDB {

    @ChangeSet(order = "001", id = "initAuthorsAndBooks", author = "biderman")
    public void initAuthorsAndBooks(MongoTemplate template) {
        Author ilf = template.save(Author.createNewAuthor("Ильф", "Илья"));
        Author petrov = template.save(Author.createNewAuthor("Петров", "Евгений"));
        template.save(Book.createNewBook(Arrays.asList(ilf, petrov), "Двенадцать стульев", Collections.singleton("Приключения")));

        Author pushkin = template.save(Author.createNewAuthor("Пушкин", "Александр"));
        template.save(Book.createNewBook(Collections.singletonList(pushkin), "Евгений Онегин", Sets.newHashSet("Поэзия", "Роман")));
    }
}
