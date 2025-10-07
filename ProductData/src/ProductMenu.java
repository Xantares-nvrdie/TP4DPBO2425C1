import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;


public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();
        // atur ukuran window
        menu.setSize(700, 500);
        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);
        // isi window
        menu.setContentPane(menu.mainPanel);
        // ubah warna background
        menu.getContentPane().setBackground(Color.lightGray);
        // tampilkan window
        menu.setVisible(true);
        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private ArrayList<Product> listProduct;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JSlider ratingSlider;
    private JLabel ratingLabel;

    // constructor
    public ProductMenu() {
        // inisialisasi listProduct
        listProduct = new ArrayList<>();
        // isi listProduct
        populateList();
        // isi tabel produk
        productTable.setModel(setTable());
        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 25f));
        // atur isi combo box (gunakan daftar kategori)
        String[] kategoriList = {"Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriList));

        // rating slider setup
        ratingLabel.setText("Rating");
        ratingSlider.setMinimum(1);
        ratingSlider.setMaximum(5);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setValue(3);

        // sembunyikan button delete
        deleteButton.setVisible(false);
        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1){
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = productTable.getSelectedRow();
                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                double curHarga = listProduct.get(selectedIndex).getHarga();
                hargaField.setText(String.valueOf(curHarga));
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curRating = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(String.valueOf(curHarga));
                kategoriComboBox.setSelectedItem(curKategori);
                ratingSlider.setValue(Integer.parseInt(curRating));
                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = {"No", "ID Produk", "Nama Produk", "Harga", "Kategori", "Rating"};
        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        // isi tabel dengan listProduct
        int no = 1;
        for (Product p : listProduct) {
            Object[] row = {
                    no++,
                    p.getId(),
                    p.getNama(),
                    rupiahFormat.format(p.getHarga()),
                    p.getKategori(),
                    p.getRating()
            };

            tmp.addRow(row);
        }

        return tmp;
    }

    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();
            // tambahkan data ke dalam list
            listProduct.add(new Product(id, nama, harga, kategori, rating));
            // update tabel
            productTable.setModel(setTable());
            // bersihkan form
            clearForm();
            // feedback
            System.out.println("Insert Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();
            // ubah data produk di list
            listProduct.get(selectedIndex).setId(id);
            listProduct.get(selectedIndex).setNama(nama);
            listProduct.get(selectedIndex).setHarga(harga);
            listProduct.get(selectedIndex).setKategori(kategori);
            listProduct.get(selectedIndex).setRating(rating);

            // update tabel
            productTable.setModel(setTable());
            // bersihkan form
            clearForm();
            // feedback
            System.out.println("Update Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin menghapus produk ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            listProduct.remove(selectedIndex);
            productTable.setModel(setTable());
            clearForm();
            System.out.println("Delete Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        ratingSlider.setValue(3);
        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex= -1;
    }

    // panggil prosedur ini untuk mengisi list produk
    private void populateList() {
        listProduct.add(new Product("P001", "Laptop Asus", 8500000.0, "Elektronik", 5));
        listProduct.add(new Product("P002", "Mouse Logitech", 350000.0, "Elektronik", 4));
        listProduct.add(new Product("P003", "Keyboard Mechanical", 750000.0, "Elektronik", 5));
        listProduct.add(new Product("P004", "Roti Tawar", 15000.0, "Makanan", 3));
        listProduct.add(new Product("P005", "Susu UHT", 12000.0, "Minuman", 4));
        listProduct.add(new Product("P006", "Kemeja Putih", 125000.0, "Pakaian", 5));
        listProduct.add(new Product("P007", "Celana Jeans", 200000.0, "Pakaian", 4));
        listProduct.add(new Product("P008", "Pensil 2B", 3000.0, "Alat Tulis", 3));
        listProduct.add(new Product("P009", "Buku Tulis", 8000.0, "Alat Tulis", 4));
        listProduct.add(new Product("P010", "Air Mineral", 5000.0, "Minuman", 3));
        listProduct.add(new Product("P011", "Smartphone Samsung", 4500000.0, "Elektronik", 5));
        listProduct.add(new Product("P012", "Kue Brownies", 25000.0, "Makanan", 4));
        listProduct.add(new Product("P013", "Jaket Hoodie", 180000.0, "Pakaian", 5));
        listProduct.add(new Product("P014", "Pulpen Gel", 5000.0, "Alat Tulis", 4));
        listProduct.add(new Product("P015", "Teh Botol", 8000.0, "Minuman", 4));
    }
}
