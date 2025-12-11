package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Medico")
public class Medico {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "apellido")
    private String apellido;

    @ColumnInfo(name = "numero_departamento")
    private int numeroDepartamento;

    @ColumnInfo(name = "calle")
    private String calle;

    // Constructor
    public Medico(String nombre, String apellido, int numeroDepartamento, String calle) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDepartamento = numeroDepartamento;
        this.calle = calle;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getNumeroDepartamento() {
        return numeroDepartamento;
    }

    public void setNumeroDepartamento(int numeroDepartamento) {
        this.numeroDepartamento = numeroDepartamento;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }
}