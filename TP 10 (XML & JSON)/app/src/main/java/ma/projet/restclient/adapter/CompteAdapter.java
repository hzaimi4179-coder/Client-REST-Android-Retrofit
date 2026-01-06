package ma.projet.restclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.projet.restclient.R;
import ma.projet.restclient.beans.Compte;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes;
    private OnCompteClickListener listener;

    public interface OnCompteClickListener {
        void onEditClick(Compte compte);
        void onDeleteClick(Compte compte);
    }

    public CompteAdapter(OnCompteClickListener listener) {
        this.comptes = new ArrayList<>();
        this.listener = listener;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes != null ? comptes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.bind(compte, listener);
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    static class CompteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewSolde;
        private TextView textViewType;
        private TextView textViewDate;
        private Button buttonEdit;
        private Button buttonDelete;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewSolde = itemView.findViewById(R.id.textViewSolde);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(Compte compte, OnCompteClickListener listener) {
            textViewId.setText("ID: " + compte.getId());
            textViewSolde.setText("Solde: " + compte.getSolde() + " DH");
            textViewType.setText("Type: " + compte.getType());
            textViewDate.setText("Date: " + (compte.getDateCreation() != null ? compte.getDateCreation() : "N/A"));

            buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(compte);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(compte);
                }
            });
        }
    }
}
