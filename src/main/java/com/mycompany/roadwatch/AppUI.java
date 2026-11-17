package com.mycompany.roadwatch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppUI extends Application {

    private SistemRoadWatch sistem = new SistemRoadWatch();
    private Dashboard dashboard = new Dashboard(sistem);
    private BorderPane root = new BorderPane();
    private boolean gelap = false;
    private int halamanAktif = 0;
    private Pengguna penggunaAktif = null;

    @Override
    public void start(Stage stage) {
        terapkanTema();
        tampilkan(0);

        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("RoadWatch Lampung");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void terapkanTema() {
        if (gelap) {
            root.setStyle("-fx-base: #2b2d31; -fx-background-color: -fx-background;");
        } else {
            root.setStyle("-fx-background-color: -fx-background;");
        }
    }

    private void ubahTema() {
        gelap = !gelap;
        terapkanTema();
        tampilkan(halamanAktif);
    }

    private void tampilkan(int halaman) {
        halamanAktif = halaman;
        if (penggunaAktif == null) {
            root.setTop(buatHeaderAnonim(halaman));
            root.setLeft(null);
            switch (halaman) {
                case 0:
                    root.setCenter(buatDashboard());
                    break;
                case 1:
                    root.setCenter(buatHalamanLapor());
                    break;
                case 2:
                    root.setCenter(buatHalamanCekStatus());
                    break;
                case 3:
                    root.setCenter(buatHalamanLogin());
                    break;
            }
        } else {
            root.setTop(buatHeaderStaf());
            root.setLeft(buatSidebar(halaman));
            switch (halaman) {
                case 0:
                    root.setCenter(buatDashboard());
                    break;
                case 1:
                    root.setCenter(buatHalamanCekLaporan());
                    break;
                case 2:
                    if (penggunaAktif instanceof Admin) {
                        root.setCenter(buatHalamanPetugas());
                    } else {
                        root.setCenter(buatHalamanProgres());
                    }
                    break;
            }
        }
    }

    private VBox buatHeaderAnonim(int aktif) {
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 15, 10, 15));

        Label nama = new Label("RoadWatch Lampung");
        nama.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox.setMargin(nama, new Insets(0, 20, 0, 0));
        header.getChildren().add(nama);

        String[] namaMenu = {"Dashboard", "Tambah Laporan", "Cek Status"};
        for (int i = 0; i < namaMenu.length; i++) {
            final int nomor = i;
            Button b = buatTombolMenu(namaMenu[i], i == aktif);
            b.setOnAction(e -> tampilkan(nomor));
            header.getChildren().add(b);
        }

        Region pengisi = new Region();
        HBox.setHgrow(pengisi, Priority.ALWAYS);

        Button btnLogin = buatTombolMenu("Login", aktif == 3);
        btnLogin.setOnAction(e -> tampilkan(3));
        Button btnTema = new Button(gelap ? "Mode Terang" : "Mode Gelap");
        btnTema.setOnAction(e -> ubahTema());

        header.getChildren().addAll(pengisi, btnLogin, btnTema);
        return new VBox(header, new Separator());
    }

    private VBox buatHeaderStaf() {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 15, 10, 15));

        Label nama = new Label("RoadWatch Lampung");
        nama.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region pengisi = new Region();
        HBox.setHgrow(pengisi, Priority.ALWAYS);

        Label info = new Label(penggunaAktif.getNama() + " (" + penggunaAktif.getPeran() + ")");
        Button btnTema = new Button(gelap ? "Mode Terang" : "Mode Gelap");
        btnTema.setOnAction(e -> ubahTema());
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> {
            penggunaAktif = null;
            tampilkan(0);
        });

        header.getChildren().addAll(nama, pengisi, info, btnTema, btnLogout);
        return new VBox(header, new Separator());
    }

    private VBox buatSidebar(int aktif) {
        String[] menu;
        if (penggunaAktif instanceof Admin) {
            menu = new String[]{"Dashboard", "Cek Laporan", "Petugas"};
        } else {
            menu = new String[]{"Dashboard", "Cek Laporan", "Update Progres"};
        }

        VBox sidebar = new VBox(8);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(180);
        sidebar.setMinWidth(180);
        sidebar.setStyle("-fx-border-color: -fx-box-border; -fx-border-width: 0 1 0 0;");
        for (int i = 0; i < menu.length; i++) {
            final int nomor = i;
            Button b = buatTombolMenu(menu[i], i == aktif);
            b.setMaxWidth(Double.MAX_VALUE);
            b.setAlignment(Pos.CENTER_LEFT);
            b.setOnAction(e -> tampilkan(nomor));
            sidebar.getChildren().add(b);
        }
        return sidebar;
    }

    private Button buatTombolMenu(String isi, boolean aktif) {
        Button b = new Button(isi);
        if (aktif) {
            b.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        return b;
    }

    private Label buatJudul(String isi) {
        Label l = new Label(isi);
        l.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        return l;
    }

    private void tampilAlert(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    private boolean formatFotoValid(String foto) {
        String f = foto.trim().toLowerCase();
        return f.endsWith(".jpg") || f.endsWith(".jpeg") || f.endsWith(".png");
    }

    private boolean tanggalValid(String teks) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(teks, format);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Petugas petugasAktif() {
        return (Petugas) penggunaAktif;
    }

    private VBox buatDashboard() {
        int total = sistem.getDaftarLaporan().size();
        int baru = dashboard.hitungByStatus("Baru");
        int proses = dashboard.hitungByStatus("Dalam Penanganan");
        int selesai = dashboard.hitungByStatus("Selesai");
        int persen = 0;
        if (total > 0) {
            persen = (int) Math.round(selesai * 100.0 / total);
        }

        HBox barisAngka = new HBox(15,
                kotakAngka(String.valueOf(total), "Total laporan"),
                kotakAngka(String.valueOf(baru), "Menunggu verifikasi"),
                kotakAngka(String.valueOf(proses), "Dalam penanganan"),
                kotakAngka(selesai + " (" + persen + "%)", "Selesai diperbaiki"));

        HBox barisGrafik = new HBox(15, buatGrafikStatus(), buatGrafikTingkat(), buatTop5());
        VBox.setVgrow(barisGrafik, Priority.ALWAYS);

        VBox halaman = new VBox(15);
        halaman.setPadding(new Insets(15));
        halaman.getChildren().addAll(buatJudul("Dashboard Kondisi Jalan"), barisAngka, barisGrafik);
        return halaman;
    }

    private VBox kotakAngka(String angka, String keterangan) {
        Label lblAngka = new Label(angka);
        lblAngka.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        VBox kotak = new VBox(2, lblAngka, new Label(keterangan));
        kotak.setPadding(new Insets(12));
        kotak.setStyle("-fx-border-color: -fx-box-border;");
        kotak.setPrefWidth(100);
        kotak.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(kotak, Priority.ALWAYS);
        return kotak;
    }

    private PieChart buatGrafikStatus() {
        PieChart pie = new PieChart();
        pie.setTitle("Status Laporan");
        pie.setAnimated(false);
        pie.setPrefWidth(300);
        pie.setMaxWidth(Double.MAX_VALUE);
        pie.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(pie, Priority.ALWAYS);

        String[] status = {"Baru", "Terverifikasi", "Dalam Penanganan", "Selesai", "Ditolak"};
        for (int i = 0; i < status.length; i++) {
            int jumlah = dashboard.hitungByStatus(status[i]);
            if (jumlah > 0) {
                pie.getData().add(new PieChart.Data(status[i] + " (" + jumlah + ")", jumlah));
            }
        }
        return pie;
    }

    private BarChart<String, Number> buatGrafikTingkat() {
        int ringan = dashboard.hitungByTingkat("Ringan");
        int sedang = dashboard.hitungByTingkat("Sedang");
        int berat = dashboard.hitungByTingkat("Berat");
        int terbesar = Math.max(ringan, Math.max(sedang, berat));

        CategoryAxis sumbuX = new CategoryAxis();
        NumberAxis sumbuY = new NumberAxis();
        sumbuY.setAutoRanging(false);
        sumbuY.setLowerBound(0);
        sumbuY.setUpperBound(terbesar + 1);
        sumbuY.setTickUnit(1);
        sumbuY.setMinorTickCount(0);

        BarChart<String, Number> grafik = new BarChart<>(sumbuX, sumbuY);
        grafik.setTitle("Tingkat Kerusakan");
        grafik.setLegendVisible(false);
        grafik.setAnimated(false);
        grafik.setPrefWidth(300);
        grafik.setMaxWidth(Double.MAX_VALUE);
        grafik.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(grafik, Priority.ALWAYS);

        XYChart.Series<String, Number> seri = new XYChart.Series<>();
        seri.getData().add(new XYChart.Data<>("Ringan", ringan));
        seri.getData().add(new XYChart.Data<>("Sedang", sedang));
        seri.getData().add(new XYChart.Data<>("Berat", berat));
        grafik.getData().add(seri);
        return grafik;
    }

    private VBox buatTop5() {
        VBox kotak = new VBox(8);
        kotak.setPadding(new Insets(12));
        kotak.setStyle("-fx-border-color: -fx-box-border;");
        kotak.setPrefWidth(300);
        kotak.setMaxWidth(Double.MAX_VALUE);
        kotak.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(kotak, Priority.ALWAYS);

        Label judul = new Label("Top 5 Jalan Prioritas");
        judul.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        kotak.getChildren().add(judul);

        ArrayList<Laporan> urut = sistem.urutkanPrioritas();
        if (urut.isEmpty()) {
            kotak.getChildren().add(new Label("Belum ada laporan."));
            return kotak;
        }
        int batas = Math.min(5, urut.size());
        for (int i = 0; i < batas; i++) {
            Laporan l = urut.get(i);
            Label nama = new Label((i + 1) + ". " + l.getNamaJalan() + " (" + l.getKecamatan() + ")");
            nama.setStyle("-fx-font-weight: bold;");
            Label info = new Label("    " + l.getTingkatKerusakan() + " - "
                    + l.getJumlahPelapor() + " pelapor - " + l.getStatus());
            kotak.getChildren().addAll(nama, info);
        }
        return kotak;
    }

    private VBox buatHalamanLapor() {
        TextField txtJalan = new TextField();
        txtJalan.setPrefWidth(300);
        TextField txtKecamatan = new TextField();
        ComboBox<String> cmbTingkat = new ComboBox<>();
        cmbTingkat.getItems().addAll("ringan", "sedang", "berat");
        cmbTingkat.setValue("ringan");
        TextField txtFoto = new TextField();
        txtFoto.setPromptText("contoh: lubang1.jpg");
        Label lblHasil = new Label();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Nama jalan"), 0, 0);
        form.add(txtJalan, 1, 0);
        form.add(new Label("Kecamatan"), 0, 1);
        form.add(txtKecamatan, 1, 1);
        form.add(new Label("Tingkat kerusakan"), 0, 2);
        form.add(cmbTingkat, 1, 2);
        form.add(new Label("Nama file foto"), 0, 3);
        form.add(txtFoto, 1, 3);

        Button btnKirim = new Button("Kirim Laporan");
        btnKirim.setOnAction(e -> {
            String jalan = txtJalan.getText().trim();
            String kec = txtKecamatan.getText().trim();
            String tingkat = cmbTingkat.getValue();
            String foto = txtFoto.getText().trim();

            if (jalan.isEmpty() || kec.isEmpty() || tingkat == null || foto.isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Semua kolom wajib diisi.");
                return;
            }
            if (!formatFotoValid(foto)) {
                tampilAlert(Alert.AlertType.WARNING, "Format foto salah",
                        "Foto harus berakhiran .jpg, .jpeg, atau .png.");
                return;
            }
            try {
                boolean sudahAda = sistem.cariLaporanByLokasi(jalan, kec) != null;
                String kode = sistem.tambahLaporan(jalan, kec, tingkat, foto);
                String pesan = sudahAda
                        ? "Lokasi ini sudah pernah dilaporkan. Jumlah pelapor ditambah.\n"
                        : "Laporan diterima.\n";
                tampilAlert(Alert.AlertType.INFORMATION, "Berhasil",
                        pesan + "Kode laporan Anda: " + kode + "\nSimpan kode ini untuk mengecek status.");
                lblHasil.setText("Kode laporan terakhir: " + kode);
                txtJalan.clear();
                txtKecamatan.clear();
                txtFoto.clear();
                cmbTingkat.setValue("ringan");
            } catch (Exception ex) {
                tampilAlert(Alert.AlertType.ERROR, "Terjadi kesalahan",
                        "Laporan gagal diproses: " + ex.getMessage());
            }
        });

        VBox halaman = new VBox(15, buatJudul("Lapor Jalan Rusak"), form, btnKirim, lblHasil);
        halaman.setPadding(new Insets(30));
        halaman.setAlignment(Pos.TOP_CENTER);
        return halaman;
    }

    private VBox buatHalamanCekStatus() {
        TextField txtKode = new TextField();
        txtKode.setPromptText("contoh: RW-0001");
        Button btnCek = new Button("Cek Status");
        TextArea areaHasil = new TextArea();
        areaHasil.setEditable(false);
        areaHasil.setPrefRowCount(14);

        btnCek.setOnAction(e -> {
            String kode = txtKode.getText().trim();
            if (kode.isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Kode kosong",
                        "Masukkan kode laporan dulu, contoh: RW-0001.");
                return;
            }
            Laporan l = sistem.cariLaporanByKode(kode);
            if (l == null) {
                tampilAlert(Alert.AlertType.ERROR, "Tidak ditemukan",
                        "Laporan dengan kode " + kode + " tidak ada.");
                areaHasil.clear();
            } else {
                areaHasil.setText(detailLaporan(l));
            }
        });

        HBox baris = new HBox(10, new Label("Kode laporan"), txtKode, btnCek);
        baris.setAlignment(Pos.CENTER_LEFT);

        VBox halaman = new VBox(15, buatJudul("Cek Status Laporan"), baris, areaHasil);
        halaman.setPadding(new Insets(30));
        return halaman;
    }

    private VBox buatHalamanLogin() {
        TextField txtUser = new TextField();
        txtUser.setPrefWidth(250);
        PasswordField txtPass = new PasswordField();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Username"), 0, 0);
        form.add(txtUser, 1, 0);
        form.add(new Label("Password"), 0, 1);
        form.add(txtPass, 1, 1);

        Button btnMasuk = new Button("Masuk");
        btnMasuk.setOnAction(e -> prosesLogin(txtUser.getText(), txtPass.getText()));
        txtPass.setOnAction(e -> prosesLogin(txtUser.getText(), txtPass.getText()));

        Label catatan = new Label("Khusus petugas dan admin PUPR. Dashboard dapat dilihat tanpa login.");

        VBox halaman = new VBox(15, buatJudul("Login Petugas / Admin"), form, btnMasuk, catatan);
        halaman.setPadding(new Insets(30));
        halaman.setAlignment(Pos.TOP_CENTER);
        return halaman;
    }

    private void prosesLogin(String user, String pass) {
        if (user.trim().isEmpty() || pass.isEmpty()) {
            tampilAlert(Alert.AlertType.WARNING, "Data belum lengkap",
                    "Username dan password wajib diisi.");
            return;
        }
        Pengguna p = sistem.login(user.trim(), pass);
        if (p == null) {
            tampilAlert(Alert.AlertType.ERROR, "Login gagal", "Username atau password salah.");
            return;
        }
        penggunaAktif = p;
        tampilkan(0);
    }

    private void isiDaftar(ListView<String> daftar, ArrayList<Laporan> tampil, String filter) {
        daftar.getItems().clear();
        tampil.clear();
        ArrayList<Laporan> semua = sistem.urutkanPrioritas();
        for (int i = 0; i < semua.size(); i++) {
            Laporan l = semua.get(i);
            boolean cocok = filter.equals("Semua") || l.getStatus().equals(filter)
                    || (filter.equals("Siap Ditangani")
                    && (l.getStatus().equals("Terverifikasi") || l.getStatus().equals("Dalam Penanganan")));
            if (cocok) {
                tampil.add(l);
                daftar.getItems().add(l.getKodeLaporan() + " | " + l.getNamaJalan() + " | "
                        + l.getTingkatKerusakan() + " | " + l.getStatus()
                        + " | skor " + l.hitungSkorPrioritas());
            }
        }
    }

    private Laporan ambilTerpilih(ListView<String> daftar, ArrayList<Laporan> tampil) {
        int urutan = daftar.getSelectionModel().getSelectedIndex();
        if (urutan < 0 || urutan >= tampil.size()) {
            return null;
        }
        return tampil.get(urutan);
    }

    private HBox buatHalamanCekLaporan() {
        ComboBox<String> cmbFilter = new ComboBox<>();
        cmbFilter.getItems().addAll("Semua", "Baru", "Terverifikasi", "Dalam Penanganan", "Selesai", "Ditolak");
        cmbFilter.setValue("Semua");
        ListView<String> daftar = new ListView<>();
        ArrayList<Laporan> tampil = new ArrayList<>();
        isiDaftar(daftar, tampil, "Semua");

        TextArea areaDetail = new TextArea();
        areaDetail.setEditable(false);
        Button btnVerifikasi = new Button("Verifikasi (valid)");
        Button btnTolak = new Button("Tolak");

        cmbFilter.setOnAction(e -> {
            isiDaftar(daftar, tampil, cmbFilter.getValue());
            areaDetail.clear();
        });

        daftar.getSelectionModel().selectedIndexProperty().addListener((obs, lama, baru) -> {
            int urutan = baru.intValue();
            if (urutan >= 0 && urutan < tampil.size()) {
                areaDetail.setText(detailLaporan(tampil.get(urutan)));
            }
        });

        btnVerifikasi.setOnAction(e -> prosesVerifikasi(daftar, tampil, cmbFilter, areaDetail, true));
        btnTolak.setOnAction(e -> prosesVerifikasi(daftar, tampil, cmbFilter, areaDetail, false));

        HBox barisFilter = new HBox(10, new Label("Filter status"), cmbFilter);
        barisFilter.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(daftar, Priority.ALWAYS);
        VBox kiri = new VBox(10, buatJudul("Cek Laporan"), barisFilter, daftar);
        kiri.setPrefWidth(500);
        HBox.setHgrow(kiri, Priority.ALWAYS);

        VBox.setVgrow(areaDetail, Priority.ALWAYS);
        HBox barisTombol = new HBox(10, btnVerifikasi, btnTolak);
        VBox kanan = new VBox(10, new Label("Detail laporan terpilih"), areaDetail, barisTombol);
        kanan.setPrefWidth(500);
        HBox.setHgrow(kanan, Priority.ALWAYS);

        HBox halaman = new HBox(15, kiri, kanan);
        halaman.setPadding(new Insets(15));
        return halaman;
    }

    private void prosesVerifikasi(ListView<String> daftar, ArrayList<Laporan> tampil,
            ComboBox<String> cmbFilter, TextArea areaDetail, boolean valid) {
        Laporan l = ambilTerpilih(daftar, tampil);
        if (l == null) {
            tampilAlert(Alert.AlertType.WARNING, "Belum memilih", "Pilih laporan dari daftar dulu.");
            return;
        }
        if (!l.getStatus().equals("Baru")) {
            tampilAlert(Alert.AlertType.INFORMATION, "Sudah diverifikasi",
                    "Laporan ini sudah diverifikasi (status: " + l.getStatus() + ").");
            return;
        }
        petugasAktif().verifikasiLaporan(l, valid);
        String hasil = valid ? "terverifikasi." : "ditolak.";
        tampilAlert(Alert.AlertType.INFORMATION, "Berhasil",
                "Laporan " + l.getKodeLaporan() + " " + hasil);
        isiDaftar(daftar, tampil, cmbFilter.getValue());
        areaDetail.clear();
    }

    private HBox buatHalamanProgres() {
        ListView<String> daftar = new ListView<>();
        ArrayList<Laporan> tampil = new ArrayList<>();
        isiDaftar(daftar, tampil, "Siap Ditangani");

        Label lblInfo = new Label("Pilih laporan di sebelah kiri.");
        lblInfo.setStyle("-fx-font-weight: bold;");

        TextField txtCatatan = new TextField();
        txtCatatan.setPrefWidth(300);
        TextField txtPenanggungJawab = new TextField();
        TextField txtTglRencana = new TextField();
        txtTglRencana.setPromptText("dd-MM-yyyy, contoh: 20-11-2026");
        TextField txtTglSelesai = new TextField();
        txtTglSelesai.setPromptText("dd-MM-yyyy");
        TextField txtFotoSesudah = new TextField();
        txtFotoSesudah.setPromptText("contoh: sesudah1.jpg");

        GridPane form1 = new GridPane();
        form1.setHgap(10);
        form1.setVgap(10);
        form1.add(new Label("Catatan tindakan"), 0, 0);
        form1.add(txtCatatan, 1, 0);
        form1.add(new Label("Penanggung jawab"), 0, 1);
        form1.add(txtPenanggungJawab, 1, 1);
        form1.add(new Label("Tanggal rencana"), 0, 2);
        form1.add(txtTglRencana, 1, 2);

        GridPane form2 = new GridPane();
        form2.setHgap(10);
        form2.setVgap(10);
        form2.add(new Label("Tanggal selesai"), 0, 0);
        form2.add(txtTglSelesai, 1, 0);
        form2.add(new Label("Foto sesudah"), 0, 1);
        form2.add(txtFotoSesudah, 1, 1);

        Button btnCatat = new Button("Catat Tindak Lanjut");
        Button btnSelesai = new Button("Tandai Selesai");

        daftar.getSelectionModel().selectedIndexProperty().addListener((obs, lama, baru) -> {
            int urutan = baru.intValue();
            if (urutan >= 0 && urutan < tampil.size()) {
                Laporan terpilih = tampil.get(urutan);
                lblInfo.setText(terpilih.getKodeLaporan() + " - " + terpilih.getNamaJalan()
                        + " (" + terpilih.getStatus() + ")");
            }
        });

        btnCatat.setOnAction(e -> {
            Laporan l = ambilTerpilih(daftar, tampil);
            if (l == null) {
                tampilAlert(Alert.AlertType.WARNING, "Belum memilih", "Pilih laporan dari daftar dulu.");
                return;
            }
            String catatan = txtCatatan.getText().trim();
            String pj = txtPenanggungJawab.getText().trim();
            String tgl = txtTglRencana.getText().trim();
            if (catatan.isEmpty() || pj.isEmpty() || tgl.isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Data belum lengkap",
                        "Catatan, penanggung jawab, dan tanggal rencana wajib diisi.");
                return;
            }
            if (!tanggalValid(tgl)) {
                tampilAlert(Alert.AlertType.WARNING, "Format tanggal salah",
                        "Gunakan format dd-MM-yyyy, contoh: 20-11-2026.");
                return;
            }
            petugasAktif().catatTindakLanjut(l, new TindakLanjut(catatan, pj, tgl));
            if (l.getStatus().equals("Terverifikasi")) {
                petugasAktif().ubahStatus(l, "Dalam Penanganan");
            }
            tampilAlert(Alert.AlertType.INFORMATION, "Berhasil",
                    "Tindak lanjut dicatat. Status " + l.getKodeLaporan() + ": " + l.getStatus());
            txtCatatan.clear();
            txtPenanggungJawab.clear();
            txtTglRencana.clear();
            isiDaftar(daftar, tampil, "Siap Ditangani");
            lblInfo.setText("Pilih laporan di sebelah kiri.");
        });

        btnSelesai.setOnAction(e -> {
            Laporan l = ambilTerpilih(daftar, tampil);
            if (l == null) {
                tampilAlert(Alert.AlertType.WARNING, "Belum memilih", "Pilih laporan dari daftar dulu.");
                return;
            }
            if (!l.getStatus().equals("Dalam Penanganan")) {
                tampilAlert(Alert.AlertType.WARNING, "Status belum sesuai",
                        "Status Selesai hanya bisa dari Dalam Penanganan.");
                return;
            }
            if (l.getDaftarTindakLanjut().isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Belum ada tindak lanjut",
                        "Catat tindak lanjut dulu.");
                return;
            }
            String tgl = txtTglSelesai.getText().trim();
            String foto = txtFotoSesudah.getText().trim();
            if (tgl.isEmpty() || foto.isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Data belum lengkap",
                        "Tanggal selesai dan foto sesudah wajib diisi.");
                return;
            }
            if (!tanggalValid(tgl)) {
                tampilAlert(Alert.AlertType.WARNING, "Format tanggal salah",
                        "Gunakan format dd-MM-yyyy, contoh: 25-11-2026.");
                return;
            }
            if (!formatFotoValid(foto)) {
                tampilAlert(Alert.AlertType.WARNING, "Format foto salah",
                        "Foto harus berakhiran .jpg, .jpeg, atau .png.");
                return;
            }
            int terakhir = l.getDaftarTindakLanjut().size() - 1;
            l.getDaftarTindakLanjut().get(terakhir).tandaiSelesai(tgl, foto);
            petugasAktif().ubahStatus(l, "Selesai");
            tampilAlert(Alert.AlertType.INFORMATION, "Berhasil",
                    "Laporan " + l.getKodeLaporan() + " ditandai selesai.");
            txtTglSelesai.clear();
            txtFotoSesudah.clear();
            isiDaftar(daftar, tampil, "Siap Ditangani");
            lblInfo.setText("Pilih laporan di sebelah kiri.");
        });

        VBox.setVgrow(daftar, Priority.ALWAYS);
        VBox kiri = new VBox(10, buatJudul("Update Progres Perbaikan"),
                new Label("Laporan siap ditangani"), daftar);
        kiri.setPrefWidth(500);
        HBox.setHgrow(kiri, Priority.ALWAYS);

        VBox kanan = new VBox(12, lblInfo, form1, btnCatat, new Separator(), form2, btnSelesai);
        kanan.setPrefWidth(500);
        kanan.setPadding(new Insets(40, 0, 0, 0));
        HBox.setHgrow(kanan, Priority.ALWAYS);

        HBox halaman = new HBox(15, kiri, kanan);
        halaman.setPadding(new Insets(15));
        return halaman;
    }

    private void isiDaftarPengguna(ListView<String> daftar, ArrayList<Pengguna> tampil) {
        daftar.getItems().clear();
        tampil.clear();
        ArrayList<Pengguna> semua = sistem.getDaftarPengguna();
        for (int i = 0; i < semua.size(); i++) {
            Pengguna p = semua.get(i);
            tampil.add(p);
            daftar.getItems().add(p.getIdPengguna() + " | " + p.getNama() + " | "
                    + p.getUsername() + " | " + p.getPeran());
        }
    }

    private boolean idSudahDipakai(String id) {
        ArrayList<Pengguna> semua = sistem.getDaftarPengguna();
        for (int i = 0; i < semua.size(); i++) {
            if (semua.get(i).getIdPengguna().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private HBox buatHalamanPetugas() {
        ListView<String> daftar = new ListView<>();
        ArrayList<Pengguna> tampil = new ArrayList<>();
        isiDaftarPengguna(daftar, tampil);

        Button btnHapus = new Button("Hapus Petugas Terpilih");
        Button btnReset = new Button("Reset Password Terpilih");

        btnHapus.setOnAction(e -> {
            int urutan = daftar.getSelectionModel().getSelectedIndex();
            if (urutan < 0 || urutan >= tampil.size()) {
                tampilAlert(Alert.AlertType.WARNING, "Belum memilih", "Pilih petugas dari daftar dulu.");
                return;
            }
            Pengguna p = tampil.get(urutan);
            if (p instanceof Admin) {
                tampilAlert(Alert.AlertType.WARNING, "Tidak bisa dihapus",
                        "Akun admin tidak dapat dihapus.");
                return;
            }
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus petugas " + p.getNama() + "?", ButtonType.OK, ButtonType.CANCEL);
            konfirmasi.setTitle("Konfirmasi");
            konfirmasi.setHeaderText(null);
            Optional<ButtonType> jawaban = konfirmasi.showAndWait();
            if (jawaban.isPresent() && jawaban.get() == ButtonType.OK) {
                sistem.hapusPetugas(p.getUsername());
                isiDaftarPengguna(daftar, tampil);
            }
        });

        btnReset.setOnAction(e -> {
            int urutan = daftar.getSelectionModel().getSelectedIndex();
            if (urutan < 0 || urutan >= tampil.size()) {
                tampilAlert(Alert.AlertType.WARNING, "Belum memilih", "Pilih petugas dari daftar dulu.");
                return;
            }
            Pengguna p = tampil.get(urutan);
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Reset Password");
            dialog.setHeaderText("Password baru untuk " + p.getNama());
            dialog.setContentText("Password (min 6 karakter):");
            Optional<String> hasil = dialog.showAndWait();
            if (hasil.isPresent()) {
                String passBaru = hasil.get().trim();
                if (passBaru.length() < 6) {
                    tampilAlert(Alert.AlertType.WARNING, "Password terlalu pendek",
                            "Password minimal 6 karakter.");
                } else {
                    p.setPassword(passBaru);
                    tampilAlert(Alert.AlertType.INFORMATION, "Berhasil",
                            "Password " + p.getNama() + " sudah diganti.");
                }
            }
        });

        TextField txtId = new TextField();
        txtId.setPrefWidth(200);
        TextField txtNama = new TextField();
        TextField txtUser = new TextField();
        PasswordField txtPass = new PasswordField();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("ID petugas"), 0, 0);
        form.add(txtId, 1, 0);
        form.add(new Label("Nama"), 0, 1);
        form.add(txtNama, 1, 1);
        form.add(new Label("Username"), 0, 2);
        form.add(txtUser, 1, 2);
        form.add(new Label("Password"), 0, 3);
        form.add(txtPass, 1, 3);

        Button btnTambah = new Button("Tambah Petugas");
        btnTambah.setOnAction(e -> {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String user = txtUser.getText().trim();
            String pass = txtPass.getText();
            if (id.isEmpty() || nama.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                tampilAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Semua kolom wajib diisi.");
                return;
            }
            if (pass.length() < 6) {
                tampilAlert(Alert.AlertType.WARNING, "Password terlalu pendek",
                        "Password minimal 6 karakter.");
                return;
            }
            if (sistem.usernameSudahDipakai(user)) {
                tampilAlert(Alert.AlertType.ERROR, "Username dipakai", "Username sudah dipakai.");
                return;
            }
            if (idSudahDipakai(id)) {
                tampilAlert(Alert.AlertType.ERROR, "ID dipakai", "ID petugas sudah dipakai.");
                return;
            }
            sistem.tambahPengguna(new Petugas(id, nama, user, pass));
            tampilAlert(Alert.AlertType.INFORMATION, "Berhasil", "Petugas " + nama + " ditambahkan.");
            txtId.clear();
            txtNama.clear();
            txtUser.clear();
            txtPass.clear();
            isiDaftarPengguna(daftar, tampil);
        });

        VBox.setVgrow(daftar, Priority.ALWAYS);
        HBox barisTombol = new HBox(10, btnHapus, btnReset);
        VBox kiri = new VBox(10, buatJudul("Kelola Petugas"), daftar, barisTombol);
        kiri.setPrefWidth(550);
        HBox.setHgrow(kiri, Priority.ALWAYS);

        VBox kanan = new VBox(12, new Label("Tambah petugas baru"), form, btnTambah);
        kanan.setPrefWidth(400);
        kanan.setPadding(new Insets(40, 0, 0, 0));

        HBox halaman = new HBox(15, kiri, kanan);
        halaman.setPadding(new Insets(15));
        return halaman;
    }

    private String teksProgres(String status) {
        if (status.equals("Ditolak")) {
            return "Laporan ditolak oleh petugas.";
        }
        String[] urutan = {"Baru", "Terverifikasi", "Dalam Penanganan", "Selesai"};
        String teks = "";
        for (int i = 0; i < urutan.length; i++) {
            if (urutan[i].equals(status)) {
                teks += "[" + urutan[i] + "]";
            } else {
                teks += urutan[i];
            }
            if (i < urutan.length - 1) {
                teks += "  >  ";
            }
        }
        return teks;
    }

    private String detailLaporan(Laporan l) {
        String teks = "Progres        : " + teksProgres(l.getStatus()) + "\n\n"
                + "Kode laporan   : " + l.getKodeLaporan() + "\n"
                + "Jalan          : " + l.getNamaJalan() + "\n"
                + "Kecamatan      : " + l.getKecamatan() + "\n"
                + "Tingkat        : " + l.getTingkatKerusakan() + "\n"
                + "Foto           : " + l.getFoto() + "\n"
                + "Status         : " + l.getStatus() + "\n"
                + "Jumlah pelapor : " + l.getJumlahPelapor() + "\n"
                + "Tanggal lapor  : " + l.getTanggalLaporan() + "\n\n";
        ArrayList<TindakLanjut> riwayat = l.getDaftarTindakLanjut();
        if (riwayat.isEmpty()) {
            teks += "Tindak lanjut  : belum ada";
        } else {
            teks += "Riwayat tindak lanjut:\n";
            for (int i = 0; i < riwayat.size(); i++) {
                TindakLanjut t = riwayat.get(i);
                teks += "  " + (i + 1) + ". " + t.getCatatan() + " (PJ: " + t.getPenanggungJawab() + ")\n"
                        + "     Rencana: " + t.getTanggalRencana() + " | Selesai: " + t.getTanggalSelesai()
                        + " | Foto sesudah: " + t.getFotoSesudah() + "\n";
            }
        }
        return teks;
    }

    public static void main(String[] args) {
        launch();
    }
}