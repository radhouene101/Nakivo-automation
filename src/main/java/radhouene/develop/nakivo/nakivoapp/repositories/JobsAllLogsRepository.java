package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import radhouene.develop.nakivo.nakivoapp.entities.JobsAllLogs;

import java.util.List;

@Repository
public interface JobsAllLogsRepository extends JpaRepository<JobsAllLogs, Integer> {
    @Query("SELECT t FROM JobsAllLogs t where t.ContactEmail = :contactEmail group by t.job_id ")
    List<JobsAllLogs> retrieveByContactEmail(String contactEmail);
}
