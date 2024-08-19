package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import radhouene.develop.nakivo.nakivoapp.entities.TenantAllLogs;

import java.util.List;
import java.util.Set;

@Repository
public interface TenantAllLogsRepository  extends JpaRepository<TenantAllLogs, Integer> {
    @Query("SELECT distinct t FROM TenantAllLogs t WHERE t.uuid = :tenantUUID")
    List<TenantAllLogs> tenantsByClient(String tenantUUID);
    @Query("SELECT t FROM TenantAllLogs t group by t.uuid")
    List<TenantAllLogs> tenants();
    @Query("SELECT t FROM TenantAllLogs t where t.contactEmail = :email group by t.uuid ")
    List<TenantAllLogs> findAllBytenantsemail(String email);
}
