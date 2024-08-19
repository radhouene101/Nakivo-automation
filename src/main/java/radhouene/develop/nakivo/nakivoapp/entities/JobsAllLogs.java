package radhouene.develop.nakivo.nakivoapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobsAllLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String job_id;
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
    public JobsAllLogs(Jobs jobs){
        this.job_id=jobs.getId();
        this.TenantUUID=jobs.getTenantUUID();
        this.TenantNAME=jobs.getTenantNAME();
        this.name=jobs.getName();
        this.status=jobs.getStatus();
        this.hvType=jobs.getHvType();
        this.jobType=jobs.getJobType();
        this.vmCount=jobs.getVmCount();
        this.isEnabled=jobs.getIsEnabled();
        this.currentState=jobs.getCurrentState();
        this.lrState=jobs.getLrState();
        this.nextRun=jobs.getNextRun();
        this.startTime=jobs.getStartTime();
        this.schedule=jobs.getSchedule();
        this.prePerscriotionError=jobs.getPrePerscriotionError();
        this.postPrescriptionError=jobs.getPostPrescriptionError();

    }
}
