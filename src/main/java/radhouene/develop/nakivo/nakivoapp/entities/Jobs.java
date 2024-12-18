package radhouene.develop.nakivo.nakivoapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Jobs {
    @Id
    private String id;
    private String TenantUUID;
    private String TenantNAME;
    private String name;
    private String status;
    private String hvType;
    private String jobType;
    private Integer vmCount;
    private String isEnabled;
    private String currentState;
    private String lrState;
    private String nextRun;
    private String startTime;
    private String schedule;
    private String prePerscriotionError;
    private String postPrescriptionError;
    private String ContactEmail;

}
