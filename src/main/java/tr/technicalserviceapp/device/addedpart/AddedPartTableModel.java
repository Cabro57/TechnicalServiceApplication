package tr.technicalserviceapp.device.addedpart;

import tr.technicalserviceapp.devicepart.DevicePart;
import tr.technicalserviceapp.devicepart.DeviceParts;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;

import java.util.stream.Collectors;
public class AddedPartTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "Ürün Adı", "Ürün Kodu", "Marka", "Alış Fiyatı", "Satış Fiyatı", "Ürün Garanti", "Ürün Tür" };
    private final List<AddedPart> added_parts;


    public AddedPartTableModel(List<AddedPart> added_parts, int device_id) {
        this.added_parts = added_parts.stream()
                .filter(part -> part.getDeviceID() == device_id)
                .collect(Collectors.toList());
    }


    @Override
    public int getRowCount() {
        return added_parts.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AddedPart added_part = added_parts.get(rowIndex);
        DevicePart device_part = DeviceParts.getDevicePart(added_part.device_part_ID);

        switch (columnIndex) {
            case 0: return device_part.getName();
            case 1: return device_part.getBarcode();
            case 2: return device_part.getBrand();
            case 3: return formatPrice(added_part.getPurchasePrice());
            case 4: return formatPrice(added_part.getSalePrice());
            case 5: return device_part.getWarranty();
            case 6: return device_part.getType();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    public int getAddedPartID(int rowIndex) {
        return getAddedPart(rowIndex).getID();
    }

    public AddedPart getAddedPart(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < added_parts.size()) {
            return added_parts.get(rowIndex); // Belirtilen satırı döndür
        } else {
            throw new IndexOutOfBoundsException("Geçersiz satır indeksi" + rowIndex);
        }
    }

    private static String formatPrice(double price) {
        Locale turkishLocale = new Locale("tr", "TR"); // Türkçe yerel ayar
        return String.format(turkishLocale, "%,.2f ₺", price);
    }
}
