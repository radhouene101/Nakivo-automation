package radhouene.develop.nakivo.nakivoapp.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BackUpObjectsManagement {
    private RestTemplate restTemplate = new RestTemplate();


    public String sendJsonRpcRequestBackUp() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("type", "EQ");
        criteria.put("name", "REPOSITORY_ID");
        criteria.put("value", 3);

        List<Map<String, Object>> criteriaList = new ArrayList<>();
        criteriaList.add(criteria);

        Map<String, Object> filter = new HashMap<>();
        filter.put("start", 0);
        filter.put("count", 9999);
        filter.put("sort", "NAME");
        filter.put("sortAsc", true);
        filter.put("criteria", criteriaList);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("filter", filter);

        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(dataMap);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "BackupManagement");
        requestBody.put("method", "getBackupObjects");
        requestBody.put("data", dataList);
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, AuthenticationManagement.sendJsonRpcRequestLogin());


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.getNakivoServiceEndpoint(),
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }



}
