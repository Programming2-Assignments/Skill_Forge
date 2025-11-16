import java.util.ArrayList;

public class Student extends User {

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Student");
    }
}