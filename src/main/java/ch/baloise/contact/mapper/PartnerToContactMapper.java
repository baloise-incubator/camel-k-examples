package ch.baloise.contact.mapper;

import ch.baloise.contact.Contact;
import ch.baloise.contact.Partner;

public class PartnerToContactMapper {

    public Contact mapToContact(Partner partner) {
        Contact contact = new Contact();
        contact.setFirstName(partner.getFirstName());
        contact.setLastName(partner.getLastName());
        contact.setPostalCode(partner.getPostalCode());
        contact.setCity(getCity(partner.getPostalCode()));
        
        return contact;
    }

    private String getCity(String postalCode) {
        if (postalCode.equals("4051")) {
            return "Basel";
        } else if (postalCode.equals("3800")) {
            return "Interlaken";
        } else {
            return "";
        }
    }
}
