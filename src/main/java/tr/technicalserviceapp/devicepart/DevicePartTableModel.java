package tr.technicalserviceapp.devicepart;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;

public class DevicePartTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "ID", "Ürün Adı", "Ürün Kodu", "Marka", "Model", "Alış Fiyatı", "Satış Fiyatı", "Kategori", "Stok" };
    private final List<DevicePart> device_parts;

    public DevicePartTableModel(List<DevicePart> devices) {
        this.device_parts = devices;
    }

    @Override
    public int getRowCount() {
        return device_parts.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DevicePart device_part = device_parts.get(rowIndex);

        switch (columnIndex) {
            case 0: return device_part.getID();
            case 1: return device_part.getName();
            case 2: return device_part.getBarcode();
            case 3: return device_part.getBrand();
            case 4: return device_part.getModel();
            case 5: return formatPrice(device_part.getPurchase_price());
            case 6: return formatPrice(device_part.getSale_price());
            case 7: return device_part.getCategory();
            case 8: return device_part.getStock();
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

    public int getDevicePartID(int rowIndex) {
        return getDevicePart(rowIndex).getID();
    }

    public DevicePart getDevicePart(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < device_parts.size()) {
            return device_parts.get(rowIndex); // Belirtilen satırı döndür
        } else {
            throw new IndexOutOfBoundsException("Geçersiz satır indeksi" + rowIndex);
        }
    }

    private static String formatPrice(double price) {
        Locale turkishLocale = new Locale("tr", "TR"); // Türkçe yerel ayar
        return String.format(turkishLocale, "%,.2f ₺", price);
    }

}
