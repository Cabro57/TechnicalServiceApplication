package tr.technicalserviceapp.device;

import tr.technicalserviceapp.customer.Customer;
import tr.technicalserviceapp.customer.Customers;
import tr.technicalserviceapp.device.addedpart.AddedPart;
import tr.technicalserviceapp.device.addedpart.AddedParts;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DeviceInfoTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "Müşteri", "Cihaz", "Alış Tarihi", "Ücret", "Ödeme Durumu", "Durum", "Aciliyet" };
    private final List<ServiceRecord> service_records;

    public DeviceInfoTableModel(List<ServiceRecord> operations) {
        this.service_records = operations;
    }

    @Override
    public int getRowCount() {
        return service_records.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ServiceRecord service_record = service_records.get(rowIndex);
        Customer customer = Customers.getCustomer(service_record.getCustomer());

        switch (columnIndex) {
            case 0: return customer.toString();
            case 1: return service_record.getName();
            case 2: return formatDate(service_record.getCreated_at());
            case 3: return formatPrice(getTotalFee(service_record));
            case 4: return service_record.getPayment_status();
            case 5: return service_record.getService_status();
            case 6: return service_record.getUrgency();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    public int getServiceRecordID(int rowIndex) {
        return getServiceRecord(rowIndex).getID();
    }

    public ServiceRecord getServiceRecord(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < service_records.size()) {
            return service_records.get(rowIndex); // Belirtilen satırın ID'sini döndür
        } else {
            throw new IndexOutOfBoundsException("Geçersiz satır indeksi" + rowIndex);
        }
    }

    private Double getTotalFee(ServiceRecord serviceRecord) {
        double totalTrackFee = 0.0;

        List<AddedPart> added_parts = AddedParts.getAddedParts();
        for (AddedPart added_part : added_parts) {
            if (added_part.getDeviceID()==serviceRecord.getID()) {
                totalTrackFee += added_part.getSalePrice();
            }
        }

        return serviceRecord.getService_fee() + serviceRecord.getShipping_fee() + totalTrackFee;
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
