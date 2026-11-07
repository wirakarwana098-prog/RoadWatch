package com.mycompany.roadwatch;
import java.util.ArrayList;

public class Dashboard {
    private SistemRoadWatch sistem;

    public Dashboard(SistemRoadWatch sistem) {
        this.sistem = sistem;
    }

    public int hitungByStatus(String status) {
        int jumlah = 0;
        ArrayList<Laporan> daftar = sistem.getDaftarLaporan();
        for (int i = 0; i < daftar.size(); i++) {
            if (daftar.get(i).getStatus().equals(status)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    public int hitungByTingkat(String tingkat) {
        int jumlah = 0;
        ArrayList<Laporan> daftar = sistem.getDaftarLaporan();
        for (int i = 0; i < daftar.size(); i++) {
            if (daftar.get(i).getTingkatKerusakan().equals(tingkat)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    public void tampilkanRingkasanStatus() {
        int total = sistem.getDaftarLaporan().size();
        int selesai = hitungByStatus("Selesai");
        System.out.println("--- Ringkasan Status ---");
        System.out.println("Total laporan      : " + total);
        System.out.println("Baru               : " + hitungByStatus("Baru"));
        System.out.println("Terverifikasi      : " + hitungByStatus("Terverifikasi"));
        System.out.println("Dalam Penanganan   : " + hitungByStatus("Dalam Penanganan"));
        System.out.println("Selesai            : " + selesai);
        System.out.println("Ditolak            : " + hitungByStatus("Ditolak"));
        if (total > 0) {
            double persen = selesai * 100.0 / total;
            System.out.println("Persentase selesai : " + String.format("%.1f", persen) + "%");
        } else {
            System.out.println("Persentase selesai : 0%");
        }
    }

    public void tampilkanSebaranKerusakan() {
        System.out.println("--- Sebaran Tingkat Kerusakan ---");
        System.out.println("Ringan : " + hitungByTingkat("Ringan"));
        System.out.println("Sedang : " + hitungByTingkat("Sedang"));
        System.out.println("Berat  : " + hitungByTingkat("Berat"));
    }

    public void tampilkanPeringkatJalan() {
        System.out.println("--- Peringkat Jalan Paling Prioritas (Top 5) ---");
        ArrayList<Laporan> urut = sistem.urutkanPrioritas();
        if (urut.isEmpty()) {
            System.out.println("Belum ada laporan.");
            return;
        }
        int batas = Math.min(5, urut.size());
        for (int i = 0; i < batas; i++) {
            Laporan l = urut.get(i);
            System.out.println((i + 1) + ". " + l.getNamaJalan() + " (" + l.getKecamatan() + ")"
                    + " | " + l.getTingkatKerusakan()
                    + " | " + l.getJumlahPelapor() + " pelapor"
                    + " | skor " + l.hitungSkorPrioritas());
        }
    }

    public void tampilkanPerbaikanTerbaru() {
        System.out.println("--- Perbaikan Terbaru (Selesai) ---");
        ArrayList<Laporan> daftar = sistem.getDaftarLaporan();
        boolean ada = false;
        for (int i = 0; i < daftar.size(); i++) {
            Laporan l = daftar.get(i);
            if (l.getStatus().equals("Selesai")) {
                System.out.println("- " + l.getNamaJalan() + " (" + l.getKecamatan() + ") [" + l.getKodeLaporan() + "]");
                ada = true;
            }
        }
        if (!ada) {
            System.out.println("Belum ada perbaikan yang selesai.");
        }
    }

    public void tampilkan() {
        System.out.println();
        System.out.println("=========== DASHBOARD ROADWATCH LAMPUNG ===========");
        tampilkanRingkasanStatus();
        System.out.println();
        tampilkanSebaranKerusakan();
        System.out.println();
        tampilkanPeringkatJalan();
        System.out.println();
        tampilkanPerbaikanTerbaru();
        System.out.println("===================================================");
    }
}