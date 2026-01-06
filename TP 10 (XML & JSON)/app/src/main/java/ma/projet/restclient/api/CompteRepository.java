package ma.projet.restclient.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CompteRepository {
    
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static CompteRepository instance;
    private Retrofit retrofitJson;
    private Retrofit retrofitXml;
    private CompteService compteServiceJson;
    private CompteService compteServiceXml;

    private CompteRepository() {
        // Retrofit instance for JSON
        retrofitJson = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        // Retrofit instance for XML
        retrofitXml = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        
        compteServiceJson = retrofitJson.create(CompteService.class);
        compteServiceXml = retrofitXml.create(CompteService.class);
    }

    public static synchronized CompteRepository getInstance() {
        if (instance == null) {
            instance = new CompteRepository();
        }
        return instance;
    }

    public CompteService getCompteServiceJson() {
        return compteServiceJson;
    }

    public CompteService getCompteServiceXml() {
        return compteServiceXml;
    }
}
