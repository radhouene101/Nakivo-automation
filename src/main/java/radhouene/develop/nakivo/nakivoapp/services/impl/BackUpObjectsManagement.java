package radhouene.develop.nakivo.nakivoapp.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.nakivo.nakivoapp.entities.BackUpObjectsEntity;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;
import radhouene.develop.nakivo.nakivoapp.repositories.BackUpObjectsRepository;
import radhouene.develop.nakivo.nakivoapp.services.BackUpObjectsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BackUpObjectsManagement implements BackUpObjectsService {
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private BackUpObjectsRepository repository;

    @Scheduled(fixedRate = 5000)
    public void testBackUpObjects() throws JSONException {
        System.out.println(GlobalVars.getNakivoServiceEndpoint());
        sendJsonRpcRequestBackUpRepositories();
    }

    public ResponseEntity<String> sendJsonRpcRequestBackUpRepositories() throws JSONException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("type", "LOCAL");
        criteria.put("name", "REPOSITORY_ID");
        criteria.put("value", 1);

        List<Map<String, Object>> criteriaList = new ArrayList<>();
        criteriaList.add(criteria);

        Map<String, Object> filter = new HashMap<>();
        filter.put("start", 0);
        filter.put("count", 60);
        filter.put("sort", "NAME");
        filter.put("sortAsc", true);
        filter.put("criteria", null);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("filter", filter);

        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(dataMap);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "BackupManagement");
        //getting the repositories
        requestBody.put("method", "getBackupRepositories");
        requestBody.put("data", dataList);
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, AuthenticationManagement.sendJsonRpcRequestLogin());


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.NakivoServiceEndpointTenant2,
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println(responseEntity.getBody());

        JSONObject jsonObject = (JSONObject) new JSONObject(responseEntity.getBody());
        System.out.println(
                jsonObject.get("type"));
        return responseEntity;
    }
    public void filterResponse() throws JSONException {
        ResponseEntity<String> response = sendJsonRpcRequestBackUp();
        JSONObject jsonObject = (JSONObject) new JSONObject(response.getBody());
        System.out.println(jsonObject.getJSONObject("data").getJSONArray("children"));
    }

    @Override
    public BackUpObjectsEntity save(BackUpObjectsEntity dto) {
        return null;
    }

    @Override
    public List<BackUpObjectsEntity> findAll() {
        return List.of();
    }

    @Override
    public BackUpObjectsEntity findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
