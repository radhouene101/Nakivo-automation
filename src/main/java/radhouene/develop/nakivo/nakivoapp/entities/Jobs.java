package radhouene.develop.nakivo.nakivoapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Jobs {
    @Id
    private Integer id;
    private String name;
    private String status;
    private String hvType;
    private String jobType;
    private String vmCount;
    private String schedule;

}
