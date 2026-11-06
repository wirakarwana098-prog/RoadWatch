package com.mycompany.roadwatch;

public class Petugas extends Pengguna {
    
    public Petugas(String idPengguna, String nama, String username, String password){
        super(idPengguna, nama, username, password);
    }
    
    @Override
    public String getPeran(){
        return "Petugas PUPR";
    }
    
    @Override
    public void tampilkanMenu() {
        System.out.println("=== MENU PENGGUNA ===");
        System.out.println("1. Lihat laporan masuk");
        System.out.println("2. Verifikasi laporan");
        System.out.println("3. Ubah status laporan");
        System.out.println("4. Catat tindak lanjut");
        System.out.println("5. Lihat prioritas");
        System.out.println("0. Logout");
    }
}
