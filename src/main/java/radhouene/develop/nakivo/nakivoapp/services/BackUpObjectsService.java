package radhouene.develop.nakivo.nakivoapp.services;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import radhouene.develop.nakivo.nakivoapp.entities.BackUpObjectsEntity;

public interface BackUpObjectsService extends AbstractService<BackUpObjectsEntity> {
    ResponseEntity<String> sendJsonRpcRequestBackUpRepositories() throws JSONException;
}
