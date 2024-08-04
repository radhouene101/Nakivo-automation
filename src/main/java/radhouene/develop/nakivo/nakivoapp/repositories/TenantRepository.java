package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;

@Repository
public interface TenantRepository extends JpaRepository<Tenants, Integer> {
}
