package radhouene.develop.nakivo.nakivoapp.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tenants {
    @Id
    private Integer id;
    private String name;
    private String userName;
    private String contactEmail;
    private String state;
    private Integer usedVms;
    private String type;
    private String uuid;
    private String connected;
    private String enabled;
    private String remoteTenant;
    private Boolean isAllocated;
    private Integer usedBackupStorage;
    private Integer allocatedBackupStorage;
    private Integer allocatedLicences;
    private Integer allocatedDesiredLicences;
    private Integer usedLicences;


}
