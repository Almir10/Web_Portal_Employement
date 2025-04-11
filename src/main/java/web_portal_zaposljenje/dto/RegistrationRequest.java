package web_portal_zaposljenje.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<Long> roleIds;

}
