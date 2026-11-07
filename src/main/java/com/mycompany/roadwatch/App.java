package com.mycompany.roadwatch;

import java.util.Scanner;

public class App {
    static Scanner input = new Scanner(System.in);
    static SistemRoadWatch sistem = new SistemRoadWatch();
    static Dashboard dashboard = new Dashboard(sistem);

    public static void main(String[] args) {
        menuUtama();
    }

    static String bacaTeks(String pertanyaan) {
        String hasil = "";
        while (hasil.isEmpty()) {
            System.out.print(pertanyaan);
            hasil = input.nextLine().trim();
            if (hasil.isEmpty()) {
                System.out.println("Input tidak boleh kosong.");
            }
        }
        return hasil;
    }

    static boolean formatFotoValid(String foto) {
        String f = foto.trim().toLowerCase();
        return f.endsWith(".jpg") || f.endsWith(".jpeg") || f.endsWith(".png");
    }

    static void menuUtama() {
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n=== ROADWATCH LAMPUNG ===");
            System.out.println("1. Lapor jalan rusak");
            System.out.println("2. Cek status laporan");
            System.out.println("3. Lihat daftar laporan");
            System.out.println("4. Lihat dashboard");
            System.out.println("5. Login petugas / admin");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            String pilihan = input.nextLine().trim();
            switch (pilihan) {
                case "1":
                    menuLapor();
                    break;
                case "2":
                    menuCekStatus();
                    break;
                case "3":
                    sistem.tampilkanSemuaLaporan();
                    break;
                case "4":
                    dashboard.tampilkan();
                    break;
                case "5":
                    menuLogin();
                    break;
                case "0":
                    jalan = false;
                    System.out.println("Terima kasih.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void menuLapor() {
        System.out.println("\n--- Lapor jalan rusak ---");
        System.out.println("Tips: cek dulu menu 3 agar tidak melapor dobel.");
        String jalan = bacaTeks("Nama jalan        : ");
        String kec = bacaTeks("Kecamatan         : ");

        String tingkat = bacaTeks("Tingkat (ringan/sedang/berat): ").toLowerCase();
        while (!tingkat.equals("ringan") && !tingkat.equals("sedang")
                && !tingkat.equals("berat")) {
            System.out.println("Pilihan harus ringan, sedang, atau berat.");
            tingkat = bacaTeks("Tingkat (ringan/sedang/berat): ").toLowerCase();
        }

        String foto = bacaTeks("Nama file foto (.jpg/.png): ");
        while (!formatFotoValid(foto)) {
            System.out.println("Foto harus berakhiran .jpg, .jpeg, atau .png.");
            foto = bacaTeks("Nama file foto (.jpg/.png): ");
        }

        boolean sudahAda = sistem.cariLaporanByLokasi(jalan, kec) != null;
        String kode = sistem.tambahLaporan(jalan, kec, tingkat, foto);
        if (sudahAda) {
            System.out.println("Lokasi ini sudah pernah dilaporkan. Jumlah pelapor ditambah.");
        } else {
            System.out.println("Laporan diterima.");
        }
        System.out.println("Kode laporan Anda: " + kode);
        System.out.println("Simpan kode ini untuk mengecek status laporan.");
    }

    static void menuCekStatus() {
        String kode = bacaTeks("Masukkan kode laporan: ");
        Laporan l = sistem.cariLaporanByKode(kode);
        if (l == null) {
            System.out.println("Kode tidak ditemukan.");
        } else {
            l.tampilkanDetail();
        }
    }

    static void menuLogin() {
        String user = bacaTeks("Username: ");
        String pass = bacaTeks("Password: ");
        Pengguna p = sistem.login(user, pass);
        if (p == null) {
            System.out.println("Login gagal. Username atau password salah.");
        } else if (!(p instanceof Petugas)) {
            System.out.println("Akun ini tidak punya akses petugas.");
        } else {
            System.out.println("Selamat datang, " + p.getNama() + " (" + p.getPeran() + ")");
            menuPetugas(p);
        }
    }

    static void menuPetugas(Pengguna p) {
        Petugas petugas = (Petugas) p;
        boolean aktif = true;
        while (aktif) {
            System.out.println();
            p.tampilkanMenu();
            System.out.print("Pilih: ");
            String pilihan = input.nextLine().trim();
            switch (pilihan) {
                case "1":
                    sistem.tampilkanLaporanByStatus("Baru");
                    break;
                case "2":
                    prosesVerifikasi(petugas);
                    break;
                case "3":
                    prosesUbahStatus(petugas);
                    break;
                case "4":
                    prosesTindakLanjut(petugas);
                    break;
                case "5":
                    sistem.tampilkanPrioritas();
                    break;
                case "6":
                    if (p instanceof Admin) {
                        prosesTambahPetugas();
                    } else {
                        System.out.println("Menu ini khusus admin.");
                    }
                    break;
                case "7":
                    if (p instanceof Admin) {
                        sistem.tampilkanDaftarPetugas();
                    } else {
                        System.out.println("Menu ini khusus admin.");
                    }
                    break;
                case "0":
                    aktif = false;
                    System.out.println("Anda telah logout.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void prosesVerifikasi(Petugas petugas) {
        Laporan l = sistem.cariLaporanByKode(bacaTeks("Kode laporan: "));
        if (l == null) {
            System.out.println("Kode tidak ditemukan.");
            return;
        }
        l.tampilkanDetail();
        String jawab = bacaTeks("Laporan valid? (y/n): ");
        boolean valid = jawab.equalsIgnoreCase("y");
        petugas.verifikasiLaporan(l, valid);
    }

    static void prosesTindakLanjut(Petugas petugas) {
        Laporan l = sistem.cariLaporanByKode(bacaTeks("Kode laporan: "));
        if (l == null) {
            System.out.println("Kode tidak ditemukan.");
            return;
        }
        String catatan = bacaTeks("Catatan tindakan: ");
        String pj = bacaTeks("Penanggung jawab: ");
        String tgl = bacaTeks("Tanggal rencana (dd-MM-yyyy): ");
        petugas.catatTindakLanjut(l, new TindakLanjut(catatan, pj, tgl));
        System.out.println("Tindak lanjut dicatat.");
    }

    static void prosesUbahStatus(Petugas petugas) {
        Laporan l = sistem.cariLaporanByKode(bacaTeks("Kode laporan: "));
        if (l == null) {
            System.out.println("Kode tidak ditemukan.");
            return;
        }
        System.out.println("Status sekarang: " + l.getStatus());
        System.out.println("1. Dalam Penanganan");
        System.out.println("2. Selesai");
        String pilih = bacaTeks("Ubah ke: ");
        switch (pilih) {
            case "1":
                petugas.ubahStatus(l, "Dalam Penanganan");
                break;
            case "2":
                prosesSelesai(petugas, l);
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
    }

    static void prosesSelesai(Petugas petugas, Laporan l) {
        if (!l.getStatus().equals("Dalam Penanganan")) {
            System.out.println("Status Selesai hanya dari Dalam Penanganan.");
            return;
        }
        if (l.getDaftarTindakLanjut().isEmpty()) {
            System.out.println("Catat tindak lanjut dulu (menu 4).");
            return;
        }
        String tgl = bacaTeks("Tanggal selesai (dd-MM-yyyy): ");
        String foto = bacaTeks("Foto sesudah (.jpg/.png): ");
        while (!formatFotoValid(foto)) {
            System.out.println("Foto harus berakhiran .jpg, .jpeg, atau .png.");
            foto = bacaTeks("Foto sesudah (.jpg/.png): ");
        }
        int terakhir = l.getDaftarTindakLanjut().size() - 1;
        l.getDaftarTindakLanjut().get(terakhir).tandaiSelesai(tgl, foto);
        petugas.ubahStatus(l, "Selesai");
    }

    static void prosesTambahPetugas() {
        String user = bacaTeks("Username petugas baru: ");
        if (sistem.usernameSudahDipakai(user)) {
            System.out.println("Username sudah dipakai.");
            return;
        }
        String id = bacaTeks("ID petugas: ");
        String nama = bacaTeks("Nama petugas: ");
        String pass = bacaTeks("Password (min 6 karakter): ");
        while (pass.length() < 6) {
            System.out.println("Password minimal 6 karakter.");
            pass = bacaTeks("Password (min 6 karakter): ");
        }
        sistem.tambahPengguna(new Petugas(id, nama, user, pass));
        System.out.println("Petugas ditambahkan.");
    }
}