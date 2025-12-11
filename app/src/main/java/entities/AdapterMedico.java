package entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellmeadowshospital_2025.R;
import java.util.List;

public class AdapterMedico extends RecyclerView.Adapter<AdapterMedico.ViewHolder> {

    private List<Medico> lista;

    public AdapterMedico(List<Medico> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<Medico> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medico, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Medico m = lista.get(pos);

        h.txtId.setText("ID: " + m.getId());
        h.txtNombre.setText("Nombre: " + m.getNombre());
        h.txtApellido.setText("Apellido: " + m.getApellido());
        h.txtNumDep.setText("Depto: " + m.getNumeroDepartamento());
        h.txtCalle.setText("Calle: " + m.getCalle());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtNombre, txtApellido, txtNumDep, txtCalle;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.tvIdMedico);
            txtNombre = itemView.findViewById(R.id.tvNombreMedico);
            txtApellido = itemView.findViewById(R.id.tvApellidoMedico);
            txtNumDep = itemView.findViewById(R.id.tvNumDepartamento);
            txtCalle = itemView.findViewById(R.id.tvCalleMedico);

            //card = (CardView) itemView;
        }
    }

    public void eliminarPorId(int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                lista.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }
}