package radhouene.develop.nakivo.nakivoapp.services.impl;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import radhouene.develop.nakivo.nakivoapp.entities.Jobs;
import radhouene.develop.nakivo.nakivoapp.entities.Schedules;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@AllArgsConstructor

public class ListJobs {
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private final JobsRepository jobsRepository;
    @Scheduled(fixedRate = 5000)
    public void testBackUpObjects() throws JSONException {
        saveAllJobs();    }
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
    // this method filters the job info and return a Jobs instance that we'll be using later in the with listJobsInfos() method
    public Jobs filterJobAndReturnJobsInstance(ResponseEntity<String> response) throws JSONException {

        JSONObject jsonObject = (JSONObject) new JSONObject(response.getBody());
        JSONObject resultData =  jsonObject.getJSONObject("data");
        JSONArray childrens = resultData.getJSONArray("children");
        Jobs job = new Jobs();
        for(int i = 0 ; i<childrens.length() ; i++){
            JSONObject currentChild = (JSONObject) childrens.get(i);
            System.out.println(currentChild.toString());
            job.setId((Integer) currentChild.get("id"));
            job.setName((String) currentChild.get("name"));
            job.setStatus((String) currentChild.get("status"));
            job.setHvType((String) currentChild.get("hvType"));
            job.setJobType((String) currentChild.get("jobType"));
            job.setVmCount((Integer) currentChild.get("vmCount"));
            job.setIsEnabled( currentChild.get("isEnabled").toString());
            job.setCurrentState( currentChild.get("crState").toString());
            // TODO FIX THIS SCHEDULE PROBLEM
            // job.setSchedule( currentChild.getJSONObject("schedules").toString());
            job.setPrePerscriotionError((String) currentChild.get("preScriptErrorMode"));
            job.setPostPrescriptionError((String) currentChild.get("postScriptErrorMode"));
        }

        return job;
    }
    public void saveAllJobs() throws JSONException {
        List<JSONArray> IdsJson = childJobsIdsAllGroupes();
        List<Integer> Ids = new ArrayList<>();
        for (int i = 0 ; i<IdsJson.size() ; i++){
            JSONArray current = IdsJson.get(i);
            for (int j = 0 ; j<current.length() ; j++){
                Ids.add((Integer) current.get(j));
            }
        }
        for (Integer id : Ids){
            jobsRepository.save(filterJobAndReturnJobsInstance(sendJsonRpcRequestListJobsInfo(id)));
        }
    }
}
