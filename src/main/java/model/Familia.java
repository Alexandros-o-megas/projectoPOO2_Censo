package model;

import java.util.*;

public class Familia {
    private int idFamilia;
    private String nome;
    private List<Cidadao> membros;
    private Bairro bairro;
    private Recenseador recenseador;
    private int NumeroMembros;
    private Date data;

    // Construtor para criar nova família (ID será auto-gerado)
    public Familia(String nome, Bairro bairro, Recenseador recenseador) {
        this.nome = nome;
        this.bairro = bairro;
        this.recenseador = recenseador;
        this.membros = new ArrayList<>();
    }

    // Construtor para instanciar a partir do DB
    public Familia(int idFamilia, String nome, Bairro bairro, Recenseador recenseador) {
        this.idFamilia = idFamilia;
        this.nome = nome;
        this.bairro = bairro;
        this.recenseador = recenseador;
        this.membros = new ArrayList<>();
    }

    public Familia(int idFamilia, String nome, Bairro bairro, Recenseador recenseador, int numeroMebros) {
        this.idFamilia = idFamilia;
        this.nome = nome;
        this.bairro = bairro;
        this.recenseador = recenseador;
        this.NumeroMembros = numeroMebros;
    }

    public Date getData() {
        return data;
    }

    public Familia(int idFamilia, String nome, Bairro bairro, Recenseador recenseador,int numeroMebros, Date dataCadastro) {
        this.idFamilia = idFamilia;
        this.nome = nome;
        this.bairro = bairro;
        this.recenseador = recenseador;
        this.NumeroMembros = numeroMebros;
        this.data = dataCadastro;
    }

    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(int idFamilia) { // Setter para o ID
        this.idFamilia = idFamilia;
    }

    public String getNome() {
        return nome;
    }

    public int getNumeroMembros() {
        return NumeroMembros;
    }

    public byte getNMembros() {
        return (byte) membros.size();
    }

    public List<Cidadao> getMembros() {
        if (this.membros == null) {
            this.membros = new ArrayList<>();
        }
        return membros;
    }

    public void setRecenseador(Recenseador recenseador) {
        this.recenseador = recenseador;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public Recenseador getRecenseador() {
        return recenseador;
    }

    public void addMembro(Cidadao cidadao) {
        if (this.membros == null) {
            this.membros = new ArrayList<>();
        }
        if (cidadao != null && !this.membros.contains(cidadao)) {
            this.membros.add(cidadao);
        }
    }

    public void setBairro(Bairro newBairro) {
        this.bairro = newBairro;
    }

    @Override
    public String toString() {
        return "Família: " + nome + " (ID: " + idFamilia + "), " + getNMembros() + " membros. Recenseador: " + (recenseador != null ? recenseador.getNome() : "N/A");
    }
}