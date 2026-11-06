package com.mycompany.roadwatch;

public class Pengguna {
    String idPengguna;
    String nama;
    String username;
    String password;
    
    public Pengguna(String idPengguna, String nama, String username, String password){
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.username = username;
        this.password = password;
    }
    
    public String getPeran(){
        return "Pengguna";
    }
    
    public String getIdPengguna(){
        return this.idPengguna;
    }
    
    public String getNama(){
        return this.nama;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setIdPengguna(String idPengguna){
        this.idPengguna = idPengguna;
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setPassword(String password){
        if (password.length() < 6){
            System.out.println("Password minimal 6 karakter.");
        } else {
            this.password = password;
        }
    }
      
    public void tampilkanMenu() {
        System.out.println("=== MENU PENGGUNA ===");
        System.out.println("0. Logout");
    }
}
