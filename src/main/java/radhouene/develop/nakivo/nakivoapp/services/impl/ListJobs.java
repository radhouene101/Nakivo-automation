package radhouene.develop.nakivo.nakivoapp.services.impl;


import jakarta.transaction.Transactional;
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
import radhouene.develop.nakivo.nakivoapp.entities.Jobs;
import radhouene.develop.nakivo.nakivoapp.entities.JobsAllLogs;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsAllLogsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantRepository;

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
    @Autowired
    private final JobsAllLogsRepository jobsAllLogsRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Scheduled(fixedRate = 60000)
    public void testBackUpObjects() throws JSONException {
        jobsRepository.deleteAll();
        saveAllJobs();
    }
            //send a json rpc request to get the list of jobs in a group
    public ResponseEntity<String> sendJsonRpcRequestListJobsInGroup(String tenantUUID) {
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
                GlobalVars.NakivoServiceEndpointPrefix+tenantUUID+GlobalVars.NakivoServiceEndpointsuffix,
                HttpMethod.POST,
                request,
                String.class
        );
        //System.out.println(responseEntity);
        return responseEntity;
    }
    // this methods returns a list of JSONArray of childJobsIds for all groupes
    // that we use later for the filterAndSaveJobs method
    public List<JSONArray> childJobsIdsAllGroupes(String tenantUUID) throws JSONException {
        JSONObject jsonObject = new JSONObject(sendJsonRpcRequestListJobsInGroup(tenantUUID).getBody());
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
    public ResponseEntity<String> sendJsonRpcRequestListJobsInfo(Integer jobId,String tenantUUID) {
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
        System.out.println("*******************************************************************");
        System.out.println(tenantUUID);
        System.out.println("*******************************************************************");

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.NakivoServiceEndpointPrefix+tenantUUID+GlobalVars.NakivoServiceEndpointsuffix,
                HttpMethod.POST,
                request,
                String.class
        );
        return responseEntity;
    }
    @Transactional
    // this method filters the job info and return a Jobs instance that we'll be using later in the with listJobsInfos() method
    public Jobs filterJobAndReturnJobsInstance(ResponseEntity<String> response, String tenantUUID, String name) throws JSONException {

        JSONObject jsonObject = (JSONObject) new JSONObject(response.getBody());
        JSONObject resultData =  jsonObject.getJSONObject("data");
        JSONArray childrens = resultData.getJSONArray("children");
        Jobs job = new Jobs();
        for(int i = 0 ; i<childrens.length() ; i++){
            JSONObject currentChild = (JSONObject) childrens.get(i);
            // ADDED UUID TO KNOW THE JOBS OF TENANTS
            job.setTenantUUID(tenantUUID);
            job.setTenantNAME(name);
            System.out.println(currentChild.toString());
            job.setId(tenantUUID+ currentChild.get("id"));
            job.setName((String) currentChild.get("name"));
            job.setStatus((String) currentChild.get("status"));
            job.setHvType((String) currentChild.get("hvType"));
            job.setJobType((String) currentChild.get("jobType"));
            job.setVmCount((Integer) currentChild.get("vmCount"));
            job.setIsEnabled( currentChild.get("isEnabled").toString());
            job.setCurrentState( currentChild.get("crState").toString());
            // TODO FIX THIS SCHEDULE PROBLEM
            JSONArray schedulesArray = (JSONArray) currentChild.get("schedules");
            JSONObject schedualesObject = (JSONObject) schedulesArray.get(0);
            job.setNextRun(schedualesObject.get("nextRun").toString());
            job.setStartTime(schedualesObject.get("startTime").toString());
            job.setPrePerscriotionError((String) currentChild.get("preScriptErrorMode"));
            job.setPostPrescriptionError((String) currentChild.get("postScriptErrorMode"));
        }

        return job;
    }
    @Transactional
    public void saveAllJobs() throws JSONException {
        List<Tenants> tenants = tenantRepository.findAll();
        for(Tenants tenant : tenants){

        List<JSONArray> IdsJson = childJobsIdsAllGroupes(tenant.getUuid());
        List<Integer> Ids = new ArrayList<>();
        for (int i = 0 ; i<IdsJson.size() ; i++){
            JSONArray current = IdsJson.get(i);
            for (int j = 0 ; j<current.length() ; j++){
                Ids.add((Integer) current.get(j));
            }
        }

        for (Integer id : Ids){
            Jobs job = filterJobAndReturnJobsInstance(sendJsonRpcRequestListJobsInfo(id, tenant.getUuid()), tenant.getUuid(),tenant.getName());
            job.setContactEmail(tenant.getContactEmail());
            jobsRepository.save(job);
            JobsAllLogs jobsAllLogs = new JobsAllLogs(job);
            jobsAllLogs.setContactEmail(tenant.getContactEmail());
            jobsAllLogsRepository.save(jobsAllLogs);
        }
    }
    }
}
