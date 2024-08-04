package radhouene.develop.nakivo.nakivoapp.services.impl;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
public class ListJobs {
    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 5000)
    public void testBackUpObjects() throws JSONException {
        childJobsIdsAllGroupes().forEach(System.out::println);
    }
    public ResponseEntity<String> sendJsonRpcRequestListJobsInGroup() {
        List<Object> data = new ArrayList<>();
        data.add(new Object[]{null});
        data.add(0);
        data.add(true);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "JobSummaryManagement");
        requestBody.put("method", "getGroupInfo");
        requestBody.put("data", data);
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, AuthenticationManagement.sendJsonRpcRequestLogin());


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.NakivoServiceEndpointTenant1,
                HttpMethod.POST,
                request,
                String.class
        );
        //System.out.println(responseEntity);
        return responseEntity;
    }
    // this methods returns a list of JSONArray of childJobsIds for all groupes
    // that we use later for the filterAndSaveJobs method
    public List<JSONArray> childJobsIdsAllGroupes() throws JSONException {
        JSONObject jsonObject = new JSONObject(sendJsonRpcRequestListJobsInGroup().getBody());
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray children =  data.getJSONArray("children");

        List<JSONArray> childJobsIdsList = new ArrayList<>();
        for (int i = 0; i < children.length(); i++) {
            JSONObject currentChild = (JSONObject) children.get(i);
            childJobsIdsList.add((JSONArray)currentChild.get("childJobIds"));
        }

        return childJobsIdsList;
    }
    //we can use this method to get the job info by job id
    public ResponseEntity<String> sendJsonRpcRequestListJobsInfo(Integer jobId) {
        List<Object> data = new ArrayList<>();
        data.add(new Object[]{jobId});
        data.add(0);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "JobSummaryManagement");
        requestBody.put("method", "getJobInfo");
        requestBody.put("data", data);
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, AuthenticationManagement.sendJsonRpcRequestLogin());


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.NakivoServiceEndpointTenant1,
                HttpMethod.POST,
                request,
                String.class
        );
        return responseEntity;
    }
    public NullPointerException filterJobAndReturnJobsInstance(ResponseEntity<String> response) throws JSONException {
        System.out.println(response.getBody());
        JSONObject jsonObject = (JSONObject) new JSONObject(response.getBody());
        JSONObject result = (JSONObject) jsonObject.get("data");
        JSONArray resultData =  result.getJSONArray("data");
        for (int i = 0; i < resultData.length(); i++) {
            JSONObject job = resultData.getJSONObject(i);
            System.out.println(job);
        }
        return new NullPointerException("didnt finish this method yet");
    }

}
