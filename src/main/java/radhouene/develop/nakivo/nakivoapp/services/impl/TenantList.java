package radhouene.develop.nakivo.nakivoapp.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@AllArgsConstructor


public class TenantList {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private final TenantRepository repository;
    @Scheduled(fixedRate = 10000)
    public void testBackUpObjects() throws JSONException {

        tenantDataFilterAndSaveTenants();
    }
    public ResponseEntity<String> getAllTenants() throws JSONException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("start", 0);
        filter.put("count", 2);
        filter.put("criteria", null);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("filter", filter);

        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(dataMap);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "MultitenancyManagement");
        requestBody.put("method", "getTenants");
        requestBody.put("data", dataList);
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, AuthenticationManagement.sendJsonRpcRequestLogin());


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.NakivoServiceEndpoint,
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println("here is the response");
        System.out.println(responseEntity.getBody());
        return responseEntity;
    }
    public void tenantDataFilterAndSaveTenants() throws JSONException {
        ResponseEntity<String> responseEntity = getAllTenants();
        System.out.println(responseEntity.getBody());
        JSONObject jsonObject = (JSONObject) new JSONObject(responseEntity.getBody());
        //System.out.println(jsonObject.get("type"));
        JSONObject dataObject = (JSONObject) jsonObject.get("data");
        List<JSONArray> tenants = new ArrayList<>();
        tenants.add(dataObject.getJSONArray("children"));
        for(JSONArray tenant: tenants){
            Tenants tenantInstance = new Tenants();
            for(int i=0; i<tenant.length(); i++){
                JSONObject tenantObject = (JSONObject) tenant.get(i);
                tenantInstance.setId((Integer) tenantObject.get("id"));
                tenantInstance.setName((String) tenantObject.get("name"));
                tenantInstance.setUserName((String) tenantObject.get("email"));
                tenantInstance.setContactEmail((String) tenantObject.get("email"));
                tenantInstance.setState((String) tenantObject.get("state"));
                tenantInstance.setUuid((String) tenantObject.get("uuid"));
                tenantInstance.setUsedBackupStorage((Integer) tenantObject.get("usedBackupStorage"));
                tenantInstance.setAllocatedBackupStorage((Integer) tenantObject.get("allocatedBackupStorage"));
                tenantInstance.setUsedVms((Integer) tenantObject.get("usedVms"));
                tenantInstance.setRemoteTenant(tenantObject.get("remoteTenant").toString());
                tenantInstance.setConnected( tenantObject.get("connected").toString());
                tenantInstance.setEnabled( tenantObject.get("enabled").toString());

                if(tenantObject.get("allocated").equals("1")) {
                    tenantInstance.setIsAllocated(true);
                }else{
                    tenantInstance.setIsAllocated(false);
                }
                repository.save(tenantInstance);
                System.out.println("tenant saved");
                System.out.println("##########################################################################");
                System.out.println(dataObject.getJSONArray("children"));
                System.out.println("##########################################################################");
            }

        }
    }
}
