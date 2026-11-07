package com.mycompany.roadwatch;
import java.time.LocalDate;
import java.util.ArrayList;

public class Laporan {
    private String kodeLaporan;
    private String namaJalan;
    private String kecamatan;
    private String tingkatKerusakan;
    private String foto;
    private String status;
    private int jumlahPelapor;
    private LocalDate tanggalLaporan;
    private ArrayList<TindakLanjut> daftarTindakLanjut;
    
    public Laporan(String kodeLaporan, String namaJalan, String kecamatan, String tingkatKerusakan, String foto){
        this.kodeLaporan = kodeLaporan;
        this.namaJalan = namaJalan;
        this.kecamatan = kecamatan;
        this.tingkatKerusakan = tingkatKerusakan;
        this.foto = foto;
    }
    
    public void setTingkatKerusakan(String tingkatKerusakan) {
        String tingkat = tingkatKerusakan.trim().toLowerCase();
        switch (tingkat) {
            case "ringan":
                this.tingkatKerusakan = "Ringan";
                break;
            case "sedang":
                this.tingkatKerusakan = "Sedang";
                break;
            case "berat":
                this.tingkatKerusakan = "Berat";
                break;
            default:
                System.out.println("Tingkat kerusakan harus ringan, sedang, atau berat.");
        }
    }
    
    public void setFoto(String foto){
        String gambar = foto.trim().toLowerCase();
        if (gambar.isEmpty()) {
            System.out.println("Nama file foto tidak boleh kosong.");
        } else if (gambar.endsWith(".jpg") || gambar.endsWith(".jpeg") || gambar.endsWith(".png")) {
            this.foto = foto.trim();
        } else {
            System.out.println("Format harus .jpg, .jpeg, atau .png");
        }
    }
    
    public void setStatus(String status){
        if (status.equals("Baru") || status.equals("Terverifikasi") || status.equals("Dalam Penanganan")){
            this.status = status;
        } else {
            System.out.println("Status tidak diketahui!!");
        }
    }
    
    public void tambahPelapor(){
        this.jumlahPelapor++;
    }
    
    public boolean cocokDenganLokasi(String jalan, String kec){
        return this.namaJalan.equalsIgnoreCase(jalan.trim()) && this.kecamatan.equalsIgnoreCase(kec.trim());
    }

    public int bobotKerusakan(){
        switch (this.tingkatKerusakan){
            case "Ringan":
                return 1;
            case "Sedang":
                return 2;
            case "Berat":
                return 3;
            default:
                return 0;
        }
    }

    public int hitungSkorPrioritas(){
        return bobotKerusakan() * 10 + this.jumlahPelapor;
    }

    public void tambahTindakLanjut(TindakLanjut tindak){
        this.daftarTindakLanjut.add(tindak);
    }

    public void tampilkanRingkas(){
        System.out.println(kodeLaporan + " | " + namaJalan + " | " + kecamatan + " | " + tingkatKerusakan + " | " + status + " | pelapor: " + jumlahPelapor);
    }

    public void tampilkanDetail(){
        System.out.println("Kode laporan   : " + kodeLaporan);
        System.out.println("Jalan          : " + namaJalan);
        System.out.println("Kecamatan      : " + kecamatan);
        System.out.println("Tingkat        : " + tingkatKerusakan);
        System.out.println("Foto           : " + foto);
        System.out.println("Status         : " + status);
        System.out.println("Jumlah pelapor : " + jumlahPelapor);
        System.out.println("Tanggal lapor  : " + tanggalLaporan);
        if (daftarTindakLanjut.isEmpty()){
            System.out.println("Tindak lanjut  : belum ada");
        } else {
            System.out.println("Riwayat tindak lanjut:");
            for (int i = 0; i < daftarTindakLanjut.size(); i++){
                System.out.println("  " + (i + 1) + ".");
                daftarTindakLanjut.get(i).tampilkan();
            }
        }
    }
}
