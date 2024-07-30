package radhouene.develop.nakivo.nakivoapp.services.impl;

import lombok.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.nakivo.nakivoapp.globalVars.GlobalVars;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthenticationManagement {




    private static RestTemplate restTemplate = new RestTemplate();
    String username = "Berrzig";
    String password= "root";
    private  static HttpHeaders headers = new HttpHeaders();
    public static HttpHeaders sendJsonRpcRequestLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("insecure", true);
        requestBody.put("action", "AuthenticationManagement");
        requestBody.put("method", "login");
        requestBody.put("data", new Object[]{"Berrzig", "root", true});
        requestBody.put("type", "rpc");
        requestBody.put("tid", 1);
        System.out.println("data is == "+requestBody.get("data").toString());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GlobalVars.getNakivoServiceEndpoint(),
                HttpMethod.POST,
                request,
                String.class
        );
        GlobalVars.cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        assert GlobalVars.cookies != null;
        headers.put(HttpHeaders.COOKIE, GlobalVars.cookies);
        System.out.println("cookies are "+GlobalVars.cookies);
        System.out.println(responseEntity.getBody());
        System.out.println("login req  "+request.getBody());
        //returning the headers to use this funtion as header parameter in other api call for session seved in cookies
        return headers;
    }

}
