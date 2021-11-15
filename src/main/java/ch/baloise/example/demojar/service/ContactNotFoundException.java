package ch.baloise.example.demojar.service;

public class ContactNotFoundException extends Exception {
    private long id;

    public ContactNotFoundException(long id) {
        super("Contact not found with id " + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
