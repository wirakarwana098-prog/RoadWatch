package com.mycompany.roadwatch;

public class TindakLanjut {
    private String catatan;
    private String penanggungJawab;
    private String tanggalRencana;
    private String tanggalSelesai;
    private String fotoSesudah;

    public TindakLanjut(String catatan, String penanggungJawab, String tanggalRencana) {
        this.catatan = catatan;
        this.penanggungJawab = penanggungJawab;
        this.tanggalRencana = tanggalRencana;
        this.tanggalSelesai = "-";
        this.fotoSesudah = "-";
    }

    public void tandaiSelesai(String tanggal, String fotoSesudah) {
        String nama = fotoSesudah.trim().toLowerCase();
        if (nama.endsWith(".jpg") || nama.endsWith(".jpeg") || nama.endsWith(".png")) {
            this.tanggalSelesai = tanggal;
            this.fotoSesudah = fotoSesudah.trim();
        } else {
            System.out.println("Foto sesudah harus berakhiran .jpg, .jpeg, atau .png.");
        }
    }

    public String getCatatan() {
        return catatan;
    }

    public String getPenanggungJawab() {
        return penanggungJawab;
    }

    public String getTanggalRencana() {
        return tanggalRencana;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public String getFotoSesudah() {
        return fotoSesudah;
    }

    public void tampilkan() {
        System.out.println("     Catatan          : " + catatan);
        System.out.println("     Penanggung jawab : " + penanggungJawab);
        System.out.println("     Tanggal rencana  : " + tanggalRencana);
        System.out.println("     Tanggal selesai  : " + tanggalSelesai);
        System.out.println("     Foto sesudah     : " + fotoSesudah);
    }
}