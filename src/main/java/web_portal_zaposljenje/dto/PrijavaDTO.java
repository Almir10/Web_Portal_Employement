package web_portal_zaposljenje.dto;

import lombok.Data;
import web_portal_zaposljenje.model.Prijava;

@Data
public class PrijavaDTO {

    private Long id;
    private String status;
    private Long developerId;
    private String developerFirstName;
    private String developerLastName;


    public PrijavaDTO(Prijava prijava) {
        this.id = prijava.getId();
        this.status = prijava.getStatus();
        this.developerId = prijava.getDeveloper().getId();
        this.developerFirstName = prijava.getDeveloper().getFirstName();
        this.developerLastName = prijava.getDeveloper().getLastName();
    }


    public Long getId() { return id; }
    public String getStatus() { return status; }
    public Long getDeveloperId() { return developerId; }
    public String getDeveloperFirstName() { return developerFirstName; }
    public String getDeveloperLastName() { return developerLastName; }
}
