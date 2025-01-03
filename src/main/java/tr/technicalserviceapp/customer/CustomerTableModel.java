package tr.technicalserviceapp.customer;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CustomerTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "ID", "Müşteri", "Telefon", "E-Posta", "Borç", "Son İşlem Tarihi" };
    private final List<Customer> customers;

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }


    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);

        switch (columnIndex) {
            case 0: return customer.getID();
            case 1: return customer.getName();
            case 2: return formatPhoneNumber(customer.getPhone_number());
            case 3: return customer.getEmail();
            case 4: return formatPrice(customer.getPrice());
            case 5: return formatDate(customer.getUpdated_at());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 0) ? Integer.class : String.class;
    }

    public int getCustomerID(int rowIndex) {
        return getCustomer(rowIndex).getID();
    }

    public Customer getCustomer(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < customers.size()) {
            return customers.get(rowIndex); // Belirtilen satırı döndür
        } else {
            throw new IndexOutOfBoundsException("Geçersiz satır indeksi" + rowIndex);
        }
    }

    private static String formatPhoneNumber(String phoneNumber) {
        return String.format("%s %s %s %s",
                phoneNumber.substring(0, 4),
                phoneNumber.substring(4, 7),
                phoneNumber.substring(7, 9),
                phoneNumber.substring(9, 11));
    }

    private static String formatPrice(double price) {
        Locale turkishLocale = new Locale("tr", "TR"); // Türkçe yerel ayar
        return String.format(turkishLocale, "%,.2f ₺", price);
    }

    private static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }
}
