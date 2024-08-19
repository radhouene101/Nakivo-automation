package radhouene.develop.nakivo.nakivoapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TenantAllLogs{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer tenant_id;
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

    public TenantAllLogs(Tenants tenantInstance) {
        this.tenant_id = tenantInstance.getId();
        this.name = tenantInstance.getName();
        this.userName = tenantInstance.getUserName();
        this.contactEmail = tenantInstance.getContactEmail();
        this.state = tenantInstance.getState();
        this.usedVms = tenantInstance.getUsedVms();
        this.type = tenantInstance.getType();
        this.uuid = tenantInstance.getUuid();
        this.connected = tenantInstance.getConnected();
        this.enabled = tenantInstance.getEnabled();
        this.remoteTenant = tenantInstance.getRemoteTenant();
        this.isAllocated = tenantInstance.getIsAllocated();
        this.usedBackupStorage = tenantInstance.getUsedBackupStorage();
        this.allocatedBackupStorage = tenantInstance.getAllocatedBackupStorage();
        this.allocatedLicences = tenantInstance.getAllocatedLicences();
        this.allocatedDesiredLicences = tenantInstance.getAllocatedDesiredLicences();
        this.usedLicences = tenantInstance.getUsedLicences();


    }
}
