package radhouene.develop.nakivo.nakivoapp.globalVars;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public  class GlobalVars {
    @Value("${nakivo.url}")
    private static String  envURL;
    @Value("${nakivo.serverIp}")
    private static final String envIP="192.168.30.207";
    @Getter
    public static final String NakivoServiceEndpoint = "https://192.168.30.207:4443/c/router";
    public static final String NakivoTenant2UUID = "2cf9b013-f92c-4061-a1fd-89a121381daf";
    public static final String NakivoServiceEndpointTenant2 = "https://192.168.30.207:4443/t/2cf9b013-f92c-4061-a1fd-89a121381daf/c/router";

    public static List<String> cookies;
}
