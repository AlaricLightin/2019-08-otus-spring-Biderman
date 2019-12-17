package ru.biderman.librarywebclassic.info;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.biderman.librarywebclassic.services.DatabaseService;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class LibraryInfoContributor implements InfoContributor {
    private final DatabaseService databaseService;

    @Override
    public void contribute(Info.Builder builder) {
        HashMap<String, Long> libraryStatistics = new HashMap<>();
        libraryStatistics.put("authorCount", databaseService.getAuthorCount());
        libraryStatistics.put("bookCount", databaseService.getBookCount());
        builder.withDetail("libraryStat", libraryStatistics);
    }
}
