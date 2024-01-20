package user;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class UserResource {
    private String id;
    private String link;
    private String name;
    private String email;

    public UserResource() {
    }

    public UserResource(String id, String link, String name, String email) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.email = email;
    }

    public UserResource(UserData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.name = data.name;
        this.email = data.email;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
