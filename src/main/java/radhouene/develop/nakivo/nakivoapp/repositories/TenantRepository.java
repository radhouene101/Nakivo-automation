package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenants, Integer> {
    @Query("select distinct t.contactEmail from Tenants t")
    List<String> retrieveAllEmails();
}
