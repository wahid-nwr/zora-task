package user;

import jakarta.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "users")
public class UserData {

    public UserData() {
    }

    public UserData(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String name;
    public String email;
}
