package User;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class UserRepository {
    private final File userFile = new File("Userdata.txt");

    public Optional<User> checkAccount(User user) throws IOException {
        if (!userFile.exists()) userFile.createNewFile();
        return new BufferedReader(new FileReader(userFile))
                .lines()
                .map(lines -> lines.split(","))
                .filter(lines -> user.getUsername().equalsIgnoreCase(lines[0]) && user.getPassword().equalsIgnoreCase(lines[1]))
                .map(lines -> new User(lines[0], lines[1]))
                .findFirst();
    }
}
