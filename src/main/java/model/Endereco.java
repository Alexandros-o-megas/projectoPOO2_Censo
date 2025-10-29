package model;

public class Endereco {
    private String provincia;
    private String municipioCidade;
    private String bairroLocalidade;
    private String ruaAvenida;
    private String quarteirao;
    private String numeroCasa;

    public Endereco(String provincia,
                    String municipioCidade,
                    String bairroLocalidade,
                    String ruaAvenida,
                    String quarteirao,
                    String numeroCasa) {
        this.provincia = provincia;
        this.municipioCidade = municipioCidade;
        this.bairroLocalidade = bairroLocalidade;
        this.ruaAvenida = ruaAvenida;
        this.quarteirao = quarteirao;
        this.numeroCasa = numeroCasa;
    }

    // Getters e setters
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipioCidade() {
        return municipioCidade;
    }

    public void setMunicipioCidade(String municipioCidade) {
        this.municipioCidade = municipioCidade;
    }

    public String getBairroLocalidade() {
        return bairroLocalidade;
    }

    public void setBairroLocalidade(String bairroLocalidade) {
        this.bairroLocalidade = bairroLocalidade;
    }

    public String getRuaAvenida() {
        return ruaAvenida;
    }

    public void setRuaAvenida(String ruaAvenida) {
        this.ruaAvenida = ruaAvenida;
    }

    public String getQuarteirao() {
        return quarteirao;
    }

    public void setQuarteirao(String quarteirao) {
        this.quarteirao = quarteirao;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    @Override
    public String toString() {
        return provincia + ", " +
               municipioCidade + ", " +
               bairroLocalidade +
               (ruaAvenida != null && !ruaAvenida.isEmpty() ? ", " + ruaAvenida : "") +
               (quarteirao   != null && !quarteirao.isEmpty()   ? ", " + quarteirao   : "") +
               (numeroCasa   != null && !numeroCasa.isEmpty()   ? ", " + numeroCasa   : "");
    }
}
