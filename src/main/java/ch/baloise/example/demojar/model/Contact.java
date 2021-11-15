package ch.baloise.example.demojar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Long id;

    private String firstName;

    private String lastName;

    private String postalCode;

    private String city;
}
