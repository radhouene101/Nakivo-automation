package radhouene.develop.nakivo.nakivoapp.services.impl;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
public class ListJobsInGroup {
    private RestTemplate restTemplate = new RestTemplate();
    @Scheduled(fixedRate = 5000)
    public void testBackUpObjects(){
        System.out.println(GlobalVars.getNakivoServiceEndpoint());
        sendJsonRpcRequestListJobsInGroup();
    }
    public String sendJsonRpcRequestListJobsInGroup() {
        /*List<Object> dataList = new ArrayList<>();
        dataList.add(null);*/
        List<Object> data = new ArrayList<>();
        data.add(new Object[]{1});
        data.add(0);
        //data.add(false);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "JobSummaryManagement");
        requestBody.put("method", "getGroupInfo");
        requestBody.put("data", data);
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
