package model;

import java.util.ArrayList;
import java.util.List;

public class Bairro {
    private int id_Bairro; // Mudado para int
    private String nome;
    private List<Familia> familias;

    // Construtor para criar novo bairro (ID será auto-gerado)
    public Bairro(String nome) {
        this.nome = nome;
        this.familias = new ArrayList<>();
    }

    // Construtor para instanciar a partir do DB
    public Bairro(int id_Bairro, String nome) {
        this.id_Bairro = id_Bairro;
        this.nome = nome;
        this.familias = new ArrayList<>();
    }

    public int getIdBairro() { // Mudado para int
        return id_Bairro;
    }

    public void setIdBairro(int id_Bairro) { // Setter para o ID
        this.id_Bairro = id_Bairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String newName) {
        this.nome = newName;
    }

    public List<Familia> getFamilias() {
        if (this.familias == null) { // Garantir que a lista não seja nula
            this.familias = new ArrayList<>();
        }
        return familias;
    }

    public void addFamilia(Familia familia) {
        if (this.familias == null) {
            this.familias = new ArrayList<>();
        }
        if (familia != null && !this.familias.contains(familia)) {
            this.familias.add(familia);
        }
    }
    
    @Override
    public String toString() {
        return "Bairro: " + nome + " (ID: " + id_Bairro + ")";
    }
}