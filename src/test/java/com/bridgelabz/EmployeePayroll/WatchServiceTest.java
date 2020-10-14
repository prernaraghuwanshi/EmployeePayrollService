package com.bridgelabz.EmployeePayroll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class WatchServiceTest {
	public static String HOME = System.getProperty("user.home");
	public static String PLAY_WITH_NIO = "TempPlayGround";

	@Test
	public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException {
		Path dir = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
		new WatcherService(dir).processEvents();
	}

}
