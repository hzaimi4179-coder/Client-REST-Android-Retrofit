package ma.projet.restclient.api;

import java.util.List;

import ma.projet.restclient.beans.Compte;
import ma.projet.restclient.beans.CompteList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompteService {
    
    // JSON endpoints
    @Headers("Accept: application/json")
    @GET("banque/comptes")
    Call<List<Compte>> getAllComptesJson();
    
    @Headers("Accept: application/json")
    @GET("banque/comptes/{id}")
    Call<Compte> getCompteByIdJson(@Path("id") Long id);
    
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("banque/comptes")
    Call<Compte> createCompteJson(@Body Compte compte);
    
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompteJson(@Path("id") Long id, @Body Compte compte);
    
    @Headers("Accept: application/json")
    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompteJson(@Path("id") Long id);
    
    // XML endpoints
    @Headers("Accept: application/xml")
    @GET("banque/comptes")
    Call<CompteList> getAllComptesXml();
    
    @Headers("Accept: application/xml")
    @GET("banque/comptes/{id}")
    Call<Compte> getCompteByIdXml(@Path("id") Long id);
    
    @Headers({"Accept: application/xml", "Content-Type: application/xml"})
    @POST("banque/comptes")
    Call<Compte> createCompteXml(@Body Compte compte);
    
    @Headers({"Accept: application/xml", "Content-Type: application/xml"})
    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompteXml(@Path("id") Long id, @Body Compte compte);
    
    @Headers("Accept: application/xml")
    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompteXml(@Path("id") Long id);
}
