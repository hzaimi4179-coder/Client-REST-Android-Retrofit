package ma.projet.restclient;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ma.projet.restclient.adapter.CompteAdapter;
import ma.projet.restclient.api.CompteRepository;
import ma.projet.restclient.api.CompteService;
import ma.projet.restclient.beans.Compte;
import ma.projet.restclient.beans.CompteList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CompteAdapter.OnCompteClickListener {

    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private RadioGroup radioGroupFormat;
    private RadioButton radioJson, radioXml;
    private FloatingActionButton fabAddCompte;
    
    private CompteService compteServiceJson;
    private CompteService compteServiceXml;
    private boolean isJsonFormat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      
        recyclerView = findViewById(R.id.recyclerViewComptes);
        radioGroupFormat = findViewById(R.id.radioGroupFormat);
        radioJson = findViewById(R.id.radioJson);
        radioXml = findViewById(R.id.radioXml);
        fabAddCompte = findViewById(R.id.fabAddCompte);

        
        adapter = new CompteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        
        CompteRepository repository = CompteRepository.getInstance();
        compteServiceJson = repository.getCompteServiceJson();
        compteServiceXml = repository.getCompteServiceXml();

        radioGroupFormat.setOnCheckedChangeListener((group, checkedId) -> {
            isJsonFormat = (checkedId == R.id.radioJson);
            loadComptes();
        });

       
        fabAddCompte.setOnClickListener(v -> showAddCompteDialog(null));

        
        loadComptes();
    }

    private void loadComptes() {
        if (isJsonFormat) {
            loadComptesJson();
        } else {
            loadComptesXml();
        }
    }

    private void loadComptesJson() {
        compteServiceJson.getAllComptesJson().enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setComptes(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de chargement (JSON)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComptesXml() {
        compteServiceXml.getAllComptesXml().enqueue(new Callback<CompteList>() {
            @Override
            public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setComptes(response.body().getComptes());
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de chargement (XML)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompteList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddCompteDialog(Compte compteToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_compte, null);
        builder.setView(dialogView);

        EditText editTextSolde = dialogView.findViewById(R.id.editTextSolde);
        RadioGroup radioGroupType = dialogView.findViewById(R.id.radioGroupType);
        RadioButton radioCourant = dialogView.findViewById(R.id.radioCourant);
        RadioButton radioEpargne = dialogView.findViewById(R.id.radioEpargne);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        // Pre-fill if editing
        if (compteToEdit != null) {
            editTextSolde.setText(String.valueOf(compteToEdit.getSolde()));
            if ("EPARGNE".equals(compteToEdit.getType())) {
                radioEpargne.setChecked(true);
            } else {
                radioCourant.setChecked(true);
            }
        }

        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonSave.setOnClickListener(v -> {
            String soldeStr = editTextSolde.getText().toString().trim();
            if (soldeStr.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un solde", Toast.LENGTH_SHORT).show();
                return;
            }

            double solde = Double.parseDouble(soldeStr);
            String type = radioCourant.isChecked() ? "COURANT" : "EPARGNE";

            if (compteToEdit == null) {
                // Create new compte
                Compte newCompte = new Compte(null, solde, type, null);
                createCompte(newCompte);
            } else {
                // Update existing compte
                compteToEdit.setSolde(solde);
                compteToEdit.setType(type);
                updateCompte(compteToEdit);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void createCompte(Compte compte) {
        if (isJsonFormat) {
            compteServiceJson.createCompteJson(compte).enqueue(new Callback<Compte>() {
                @Override
                public void onResponse(Call<Compte> call, Response<Compte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                        loadComptes();
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur de création", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Compte> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            compteServiceXml.createCompteXml(compte).enqueue(new Callback<Compte>() {
                @Override
                public void onResponse(Call<Compte> call, Response<Compte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                        loadComptes();
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur de création", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Compte> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateCompte(Compte compte) {
        if (isJsonFormat) {
            compteServiceJson.updateCompteJson(compte.getId(), compte).enqueue(new Callback<Compte>() {
                @Override
                public void onResponse(Call<Compte> call, Response<Compte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                        loadComptes();
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur de modification", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Compte> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            compteServiceXml.updateCompteXml(compte.getId(), compte).enqueue(new Callback<Compte>() {
                @Override
                public void onResponse(Call<Compte> call, Response<Compte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                        loadComptes();
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur de modification", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Compte> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteCompte(Compte compte) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer ce compte ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    if (isJsonFormat) {
                        compteServiceJson.deleteCompteJson(compte.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                                    loadComptes();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        compteServiceXml.deleteCompteXml(compte.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                                    loadComptes();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onEditClick(Compte compte) {
        showAddCompteDialog(compte);
    }

    @Override
    public void onDeleteClick(Compte compte) {
        deleteCompte(compte);
    }
}
