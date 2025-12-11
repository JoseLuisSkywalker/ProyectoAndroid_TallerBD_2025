package controllers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import entities.Medico;

import java.util.List;

@Dao
public interface MedicoDAO {

    //-------------- ALTAS ----------------
    @Insert
    void insertarMedico(Medico medico);

    //-------------- BAJAS ----------------
    @Delete
    void eliminarMedico(Medico medico);

    // Eliminación por ID
    @Query("DELETE FROM Medico WHERE id = :idMedico")
    void eliminarMedicoPorId(int idMedico);

    //-------------- MODIFICACIONES ----------------
    @Update
    void actualizarMedico(Medico medico);

    //-------------- CONSULTAS ----------------
    // Buscar por ID
    @Query("SELECT * FROM Medico WHERE id = :idMedico LIMIT 1")
    Medico obtenerMedicoPorId(int idMedico);

    // Obtener todos los médicos
    @Query("SELECT * FROM Medico")
    List<Medico> obtenerTodosLosMedicos();
}