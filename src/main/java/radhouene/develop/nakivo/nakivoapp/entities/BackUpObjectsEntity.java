package radhouene.develop.nakivo.nakivoapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BackUpObjectsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String backupIdFromNakivo;
    private String backupdUUID;
    private String sourceUUID;
    private String name;
    private String type;
    private String hyperVisorType;
    private String lastlyExecuted;
    private String sourceType;
    private String userDataSize;
    private Integer numberOfCorruptJobs;
    private Boolean isAccessible;

}
