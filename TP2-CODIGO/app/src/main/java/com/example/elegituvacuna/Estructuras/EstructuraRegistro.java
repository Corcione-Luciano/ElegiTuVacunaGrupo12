package com.example.elegituvacuna.Estructuras;

public class EstructuraRegistro {


    private String name;
    private String lastname;
    private int dni;
    private String email;
    private String password;
    private int commission;
    private int group;

    public EstructuraRegistro(){
    }



    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public int getDni() { return dni; }

    public void setDni(Integer dni) { this.dni = dni; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public int getCommission() { return commission; }

    public void setCommission(Integer commission) { this.commission = commission; }

    public int getGroup() { return group; }

    public void setGroup(Integer group) { this.group = group; }


}
