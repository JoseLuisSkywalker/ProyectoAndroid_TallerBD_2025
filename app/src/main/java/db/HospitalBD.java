package db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import controllers.MedicoDAO;
import entities.Medico;

@Database(entities = {Medico.class}, version = 1)
public abstract class HospitalBD extends RoomDatabase {

    private static HospitalBD INSTANCE;

    // Método abstracto que retorna el DAO
    public abstract MedicoDAO medicoDAO();

    // Obtener instancia de la base de datos
    public static HospitalBD getAppDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HospitalBD.class, "BD_Hospital")
                    .build();
        }
        return INSTANCE;
    }

    // Destruir instancia
    public static void destroyInstance() {
        INSTANCE = null;
    }
}