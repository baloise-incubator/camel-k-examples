package ch.baloise.example.demojar.service;

import ch.baloise.example.demojar.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ContactService {

    private static Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    private final List<Contact> contacts;

    public ContactService() {
        contacts = createContactList();
    }
    
    public Collection<Contact> getContacts() {
        LOGGER.info("getContacts called...");
        return contacts;
    }

    public Contact getContact(long id) throws ContactNotFoundException {
        LOGGER.info("getContact with id " + id + " called...");
        Iterator<Contact> iterator = contacts.iterator();
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }

        throw new ContactNotFoundException(id);
    }

    public Contact createContact(Contact contact) {
        LOGGER.info("createContact called...");
        int size = contacts.size();
        long nextId = size + 1;
        contact.setId(nextId);
        contacts.add(contact);
        return contact;
    }

    private List<Contact> createContactList() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1l, "Peter", "Meier", "4051", "Basel"));
        contacts.add(new Contact(2l, "Tom", "Eiger", "4052", "Basel"));
        contacts.add(new Contact(3l, "Tim", "Mönch", "4052", "Basel"));
        contacts.add(new Contact(4l, "Frank", "Müller", "4051", "Basel"));

        return contacts;
    }
}
