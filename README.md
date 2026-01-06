    # TP 10 — Client REST Android (Retrofit XML & JSON)


##  Objectif du TP

- Mettre en place un client Android **Retrofit** pour consommer une API REST.
- Gérer deux formats d’échange : **JSON** et **XML**.
- Implémenter un CRUD complet :
  -  Affichage des comptes
  -  Ajout d’un compte
  -  Modification d’un compte
  -  Suppression d’un compte
- Tester la gestion d’erreurs : absence de connexion, données invalides, etc.





---

##  Étapes principales (rappel)

1. Création du projet Android
2. Configuration de la sécurité réseau (HTTP/HTTPS)
3. Ajout des dépendances Retrofit + convertisseurs
4. Création des entités (Compte)
5. Interface du service REST
6. Configuration Retrofit (JSON / XML)
7. Création du Repository
8. Conception des Layouts
9. Adaptateur RecyclerView
10. Implémentation de `MainActivity`

---

##  Dépendances (build.gradle)

Ajoutez (ou vérifiez) les dépendances suivantes :

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.retrofit2:converter-simplexml:2.9.0'

    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
}
```

---

##  Configuration réseau (Network Security Config)

Créer le fichier :

`res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```

Et ajouter dans `AndroidManifest.xml` :

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
</application>
```

---

##  Modèle : Compte

Exemple d’entité Kotlin :

```kotlin
data class Compte(
    val id: Long,
    val solde: Double,
    val type: String,
    val dateCreation: String
)
```

---

##  Service REST (Retrofit Interface)

Exemple :

```kotlin
interface CompteService {
    @GET("comptes")
    fun getComptes(): Call<List<Compte>>

    @POST("comptes")
    fun addCompte(@Body compte: Compte): Call<Compte>

    @PUT("comptes/{id}")
    fun updateCompte(@Path("id") id: Long, @Body compte: Compte): Call<Compte>

    @DELETE("comptes/{id}")
    fun deleteCompte(@Path("id") id: Long): Call<Void>
}
```

---

##  Configuration Retrofit (JSON / XML)

Selon le format choisi dans l’UI, on initialise Retrofit avec le converter adapté :

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create()) // JSON
    .build()
```

Ou :

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(SimpleXmlConverterFactory.create()) // XML
    .build()
```

---

##  Captures d'écran

Les captures suivantes illustrent l’interface utilisateur :

### 1) Ajout / Modification d’un compte

<img width="1005" height="1189" alt="tp102" src="https://github.com/user-attachments/assets/298f9c36-3c49-4247-ad55-b4db2d72599b" />


### 2) Affichage de la liste des comptes
<img width="1024" height="1536" alt="tp103" src="https://github.com/user-attachments/assets/117466af-ac53-43ac-9d73-68ef922b6283" />


### 3) Confirmation de suppression
<img width="1024" height="1536" alt="tp101" src="https://github.com/user-attachments/assets/75650c02-2001-4950-aa7d-1b83e757a143" />



---

##  Exécution & Tests

1. Lancer l’application sur un **émulateur** 
2. Tester :
   - Affichage des comptes
   - Ajout d’un compte (via le bouton flottant **+**)
   - Modification d’un compte (bouton **Modifier**)
   - Suppression d’un compte (bouton **Supprimer** + confirmation)
3. Vérifier la gestion d’erreurs :
   - Pas de connexion
   - Erreur serveur
   - Champs invalides

---


##  Auteur

- **Hadil Zaimi**

---


