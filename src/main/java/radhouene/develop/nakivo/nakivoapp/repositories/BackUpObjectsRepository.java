package radhouene.develop.nakivo.nakivoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import radhouene.develop.nakivo.nakivoapp.entities.BackUpObjectsEntity;

public interface BackUpObjectsRepository extends JpaRepository<BackUpObjectsEntity, Integer> {
}
