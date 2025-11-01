package model;

public class Recenseador {
    private int id_Recenseador;
    private String nome;
    private String contacto;
    private int idAdmin; 
    private Endereco endereco;
    // A lista de famílias/cidadãos recenseados por este recenseador pode ser
    // inferida ou explicitamente mantida se necessário para queries específicas.
    // No diagrama, Familia tem uma referência a Recenseador.

    public Recenseador(String nome,int idAdmin, String contacto) {
        this.nome = nome;
        this.contacto = contacto;
        this.idAdmin = idAdmin;
    }

    public Recenseador(int id_Recenseador, String nome) {
        this.id_Recenseador = id_Recenseador;
        this.nome = nome;
    }

    public Recenseador(int idRecenseador, String nome, String contacto, Endereco endereco) {
        this.id_Recenseador = idRecenseador;
        this.nome = nome;
        this.contacto = contacto;
        this.endereco = endereco;
    }

    public int getIdRecenseador() {
        return id_Recenseador;
    }

    public String getNome() {
        return nome;
    }

    public String getContacto() {
        return contacto;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public void setIdRecenseador(int idRecenseador) {
        this.id_Recenseador = idRecenseador;
    }

    public void setContacto(String newCont) {
        this.contacto = newCont;
    }
    
    // O método registrarFamilia do UML parece mais uma ação do sistema
    // do que um método interno do Recenseador que modifica seu estado.

    @Override
    public String toString() {
        return "Recenseador: " + nome + " (ID: " + id_Recenseador + ")";
    }
}
