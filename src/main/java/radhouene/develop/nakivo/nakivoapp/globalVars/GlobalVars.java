package radhouene.develop.nakivo.nakivoapp.globalVars;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantRepository;
import radhouene.develop.nakivo.nakivoapp.services.impl.TenantList;

import java.util.List;
@Component
public  class GlobalVars {
    @Autowired
    private TenantRepository repository;

    @Value("${nakivo.url}")
    private static String  envURL;
    @Value("${nakivo.serverIp}")
    private static final String envIP="192.168.30.207";
    @Getter
    public static final String NakivoServiceEndpoint = "https://192.168.30.207:4443/c/router";
    public static final String NakivoTenant2UUID = "2cf9b013-f92c-4061-a1fd-89a121381daf";
    public static final String NakivoServiceEndpointTenant1 = "https://192.168.30.207:4443/t/2cf9b013-f92c-4061-a1fd-89a121381daf/c/router";
    public static final String NakivoServiceEndpointPrefix = "https://192.168.30.207:4443/t/";
    public static final String NakivoServiceEndpointsuffix = "/c/router";

    public static List<String> cookies;

//    private List<String> getAllTenantsUUIDs() {
//        List<Tenants> tenants = repository.findAll();
//        for (Tenants tenant : tenants) {
//            allTenantsUUIDs.add(tenant.getUuid());
//        }
//        return allTenantsUUIDs;
//    }
//
//    @Setter
//    public static List<String> allTenantsUUIDs;



}
