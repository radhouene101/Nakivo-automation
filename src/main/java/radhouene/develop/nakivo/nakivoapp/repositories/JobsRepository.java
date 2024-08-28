package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import radhouene.develop.nakivo.nakivoapp.entities.Jobs;
import radhouene.develop.nakivo.nakivoapp.entities.JobsAllLogs;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;

import java.util.List;

@Repository
public interface JobsRepository  extends JpaRepository<Jobs, Integer> {
    @Query("SELECT distinct j.ContactEmail FROM JobsAllLogs j ")
    List<String> retrieveOnlyEmails();
}
