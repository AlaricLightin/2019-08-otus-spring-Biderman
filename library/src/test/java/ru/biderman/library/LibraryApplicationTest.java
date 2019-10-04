package ru.biderman.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Приложение ")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LibraryApplicationTest {

	@DisplayName(" должно уметь создавать контекст.")
	@Test
	void contextLoads() {
	}

}
