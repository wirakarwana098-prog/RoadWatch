package com.mycompany.roadwatch;

public class Petugas extends Pengguna {

    public Petugas(String idPengguna, String nama, String username, String password) {
        super(idPengguna, nama, username, password);
    }

    @Override
    public String getPeran() {
        return "Petugas PUPR";
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("=== MENU PETUGAS PUPR ===");
        tampilkanPilihanPetugas();
        System.out.println("0. Logout");
    }

    protected void tampilkanPilihanPetugas() {
        System.out.println("1. Lihat laporan masuk");
        System.out.println("2. Verifikasi laporan");
        System.out.println("3. Ubah status laporan");
        System.out.println("4. Catat tindak lanjut");
        System.out.println("5. Lihat prioritas");
    }

    public void verifikasiLaporan(Laporan laporan, boolean valid) {
        if (laporan.getStatus().equals("Baru")) {
            if (valid) {
                laporan.setStatus("Terverifikasi");
                System.out.println("Laporan " + laporan.getKodeLaporan() + " terverifikasi.");
            } else {
                laporan.setStatus("Ditolak");
                System.out.println("Laporan " + laporan.getKodeLaporan() + " ditolak.");
            }
        } else {
            System.out.println("Laporan ini sudah diverifikasi (status: "
                    + laporan.getStatus() + ").");
        }
    }

    public void ubahStatus(Laporan laporan, String statusBaru) {
        String sekarang = laporan.getStatus();
        if (sekarang.equals("Terverifikasi") && statusBaru.equals("Dalam Penanganan")) {
            laporan.setStatus(statusBaru);
            System.out.println("Status diubah menjadi " + statusBaru + ".");
        } else if (sekarang.equals("Dalam Penanganan") && statusBaru.equals("Selesai")) {
            laporan.setStatus(statusBaru);
            System.out.println("Status diubah menjadi " + statusBaru + ".");
        } else {
            System.out.println("Perubahan status tidak sah: dari " + sekarang
                    + " ke " + statusBaru + ".");
        }
    }

    public void catatTindakLanjut(Laporan laporan, TindakLanjut tindak) {
        laporan.tambahTindakLanjut(tindak);
    }
}