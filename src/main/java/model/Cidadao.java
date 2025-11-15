package model;

import java.time.*;

public class Cidadao {
    private int idCidadao;
    private int id_familia;
    private String nome;
    private LocalDate anoNascimento;
    private String genero;
    private String estadoCivil;
    private String ocupacao;
    private String contacto;
    private String nacionalidade;

    // Construtor para criar novo cidadão (ID será auto-gerado)
    public Cidadao(int id_familia, String nome, LocalDate anoNascimento, String genero,
                   String estadoCivil, String ocupacao, String contacto, String nacionalidade) {
        this.id_familia = id_familia;
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.genero = genero;
        this.estadoCivil = estadoCivil;
        this.ocupacao = ocupacao;
        this.contacto = contacto;
        this.nacionalidade = nacionalidade;
    }

    public Cidadao(int idCidadao, int id_familia, String nome, LocalDate anoNascimento, String genero,
                   String estadoCivil, String ocupacao, String contacto, String nacionalidade) {
        this.idCidadao = idCidadao;
        this.id_familia = id_familia;
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.genero = genero;
        this.estadoCivil = estadoCivil;
        this.ocupacao = ocupacao;
        this.contacto = contacto;
        this.nacionalidade = nacionalidade;
    }


    public int getIdCidadao() { // Mudado para int
        return idCidadao;
    }

    public int getId_familia() {
        return id_familia;
    }

    public void setId_familia(int id_familia) {
        this.id_familia = id_familia;
    }

    public void setIdCidadao(int idCidadao) { // Setter para o ID
        this.idCidadao = idCidadao;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getAnoNascimento() {
        return anoNascimento;
    }

    public byte getIdade() {
        if (anoNascimento == null) return 0;
        return (byte) Period.between(anoNascimento, LocalDate.now()).getYears();
    }

    public String getGenero() {
        return genero;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public String getContacto() {
        return contacto;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNome(String newName) {
        this.nome = newName;
    }

    public void setEstadoCivil(String newEstadoCivil) {
        this.estadoCivil = newEstadoCivil;
    }

    public void setOcupacao(String newOcupacao) {
        this.ocupacao = newOcupacao;
    }

    public void setContacto(String newContacto) {
        this.contacto = newContacto;
    }

    public void setNacionalidade(String newNac) {
        this.nacionalidade = newNac;
    }

    @Override
    public String toString() {
        return "Cidadão: " + nome + " (ID: " + idCidadao + "), Idade: " + getIdade() + ", Gênero: " + genero;
    }
}