package com.mycompany.roadwatch;

import java.util.ArrayList;

public class SistemRoadWatch {

    private ArrayList<Laporan> daftarLaporan;
    private ArrayList<Pengguna> daftarPengguna;
    private int penghitungKode;

    public SistemRoadWatch() {
        daftarLaporan = new ArrayList<>();
        daftarPengguna = new ArrayList<>();
        penghitungKode = 0;

        daftarPengguna.add(new Admin("A001", "Administrator", "admin", "admin123"));
        daftarPengguna.add(new Petugas("P001", "Budi Santoso", "budi", "budi123"));

        tambahLaporan("Jl. Soekarno-Hatta", "Rajabasa", "berat", "lubang1.jpg");
        tambahLaporan("Jl. ZA Pagar Alam", "Kedaton", "sedang", "lubang2.jpg");
        tambahLaporan("Jl. Teuku Umar", "Kedaton", "ringan", "lubang3.jpg");
    }

    private String buatKodeLaporan() {
        penghitungKode++;
        return "RW-" + String.format("%04d", penghitungKode);
    }

    public Laporan cariLaporanByKode(String kode) {
        for (int i = 0; i < daftarLaporan.size(); i++) {
            if (daftarLaporan.get(i).getKodeLaporan().equalsIgnoreCase(kode.trim())) {
                return daftarLaporan.get(i);
            }
        }
        return null;
    }

    public Laporan cariLaporanByLokasi(String jalan, String kec) {
        for (int i = 0; i < daftarLaporan.size(); i++) {
            if (daftarLaporan.get(i).cocokDenganLokasi(jalan, kec)) {
                return daftarLaporan.get(i);
            }
        }
        return null;
    }

    public String tambahLaporan(String jalan, String kec, String tingkat, String foto) {
        Laporan lama = cariLaporanByLokasi(jalan, kec);
        if (lama != null) {
            lama.tambahPelapor();
            return lama.getKodeLaporan();
        }
        String kode = buatKodeLaporan();
        Laporan baru = new Laporan(kode, jalan.trim(), kec.trim(), tingkat, foto);
        daftarLaporan.add(baru);
        return kode;
    }

    public void tampilkanSemuaLaporan() {
        if (daftarLaporan.isEmpty()) {
            System.out.println("Belum ada laporan.");
            return;
        }
        for (int i = 0; i < daftarLaporan.size(); i++) {
            daftarLaporan.get(i).tampilkanRingkas();
        }
    }

    public void tampilkanLaporanByStatus(String status) {
        boolean ada = false;
        for (int i = 0; i < daftarLaporan.size(); i++) {
            if (daftarLaporan.get(i).getStatus().equals(status)) {
                daftarLaporan.get(i).tampilkanRingkas();
                ada = true;
            }
        }
        if (!ada) {
            System.out.println("Tidak ada laporan berstatus " + status + ".");
        }
    }

    public ArrayList<Laporan> urutkanPrioritas() {
        ArrayList<Laporan> salinan = new ArrayList<>(daftarLaporan);
        for (int i = 0; i < salinan.size() - 1; i++) {
            for (int j = 0; j < salinan.size() - 1 - i; j++) {
                if (salinan.get(j).hitungSkorPrioritas()
                        < salinan.get(j + 1).hitungSkorPrioritas()) {
                    Laporan temp = salinan.get(j);
                    salinan.set(j, salinan.get(j + 1));
                    salinan.set(j + 1, temp);
                }
            }
        }
        return salinan;
    }

    public void tampilkanPrioritas() {
        ArrayList<Laporan> urut = urutkanPrioritas();
        if (urut.isEmpty()) {
            System.out.println("Belum ada laporan.");
            return;
        }
        for (int i = 0; i < urut.size(); i++) {
            System.out.print((i + 1) + ". ");
            urut.get(i).tampilkanRingkas();
            System.out.println("   Skor prioritas: " + urut.get(i).hitungSkorPrioritas());
        }
    }

    public Pengguna login(String user, String pass) {
        for (int i = 0; i < daftarPengguna.size(); i++) {
            Pengguna p = daftarPengguna.get(i);
            if (p.getUsername().equals(user) && p.login(user, pass)) {
                return p;
            }
        }
        return null;
    }

    public boolean usernameSudahDipakai(String user) {
        for (int i = 0; i < daftarPengguna.size(); i++) {
            if (daftarPengguna.get(i).getUsername().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void tambahPengguna(Pengguna p) {
        daftarPengguna.add(p);
    }

    public void tampilkanDaftarPetugas() {
        for (int i = 0; i < daftarPengguna.size(); i++) {
            Pengguna p = daftarPengguna.get(i);
            System.out.println((i + 1) + ". " + p.getNama() + " | " + p.getUsername()
                    + " | " + p.getPeran());
        }
    }

    public ArrayList<Laporan> getDaftarLaporan() {
        return daftarLaporan;
    }

    public ArrayList<Pengguna> getDaftarPengguna() {
        return daftarPengguna;
    }
}