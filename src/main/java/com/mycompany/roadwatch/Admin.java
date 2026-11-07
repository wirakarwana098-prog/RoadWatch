package com.mycompany.roadwatch;

public class Admin extends Petugas {

    public Admin(String idPengguna, String nama, String username, String password) {
        super(idPengguna, nama, username, password);
    }

    @Override
    public String getPeran() {
        return "Admin PUPR";
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("=== MENU ADMIN PUPR ===");
        tampilkanPilihanPetugas();
        System.out.println("6. Tambah petugas");
        System.out.println("7. Lihat daftar petugas");
        System.out.println("0. Logout");
    }
}