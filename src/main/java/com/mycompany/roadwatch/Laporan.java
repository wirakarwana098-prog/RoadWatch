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
}
