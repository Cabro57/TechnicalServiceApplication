package tr.technicalserviceapp.devicepart;

import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.StorageFile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DeviceParts {
    private static final Path file_path = Manager.getPath("storage.device_parts_path;storage.device_parts_file_name");
    private static ArrayList<DevicePart> device_parts;
    private static int ID;

    public static int getSafeID() {
        while (device_parts.stream().anyMatch(c -> c.getID() == ID)) {
            ID++;
        }
        return ID;
    }

    public static void load() {
        device_parts = new ArrayList<>();
        try (BufferedReader reader = StorageFile.getReader(file_path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCsv(line);
                if (data.size() == 15) {
                    DevicePart task = new DevicePart(Integer.parseInt(data.get(0)), data.get(1), data.get(2), data.get(3));
                    task.setSupplier(data.get(4));
                    task.setBrand(data.get(5));
                    task.setModel(data.get(6));
                    task.setCategory(data.get(7));
                    task.setWarranty(data.get(8));
                    task.setPurchase_price(Double.parseDouble(data.get(9)));
                    task.setSale_price(Double.parseDouble(data.get(10)));
                    task.setStock(Integer.parseInt(data.get(11)));
                    task.setNote(data.get(12));
                    task.setCreated_at(data.get(13));
                    task.setUpdated_at(data.get(14));
                    device_parts.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("[DeviceParts] ERROR | Dosya okunurken hata oluştu.");
        }
    }

    public static void save() {
        try (BufferedWriter writer = StorageFile.getWriter(file_path)) {
            for (DevicePart device : device_parts) {
                writer.write(device.toCsv());
                writer.newLine();
            }
            System.out.println("[DeviceParts] INFO | Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("[DeviceParts] ERROR | Dosya yazılırken hata oluştu.");
        }

    }

    public static void add(DevicePart device) {
        if (device_parts.stream().noneMatch(c -> c.getID() == device.getID())) {
            device_parts.add(device);
            save(); // Ekledikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[DeviceParts] ERROR | Aynı ID'ye sahip cihaz zaten var.");
        }
    }

    public static void remove(DevicePart device) {
        if (device_parts.remove(device)) {
            save(); // Silindikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[DeviceParts] ERROR | Cihaz bulunamadı.");
        }
    }

    public static void remove(int ID) {
        DevicePart deviceToRemove = device_parts.stream()
                .filter(c -> c.getID() == ID)
                .findFirst()
                .orElse(null);
        if (deviceToRemove != null) {
            remove(deviceToRemove); // Diğer `remove` metodunu çağırıyoruz
        } else {
            System.out.println("[DeviceParts] ERROR | Belirtilen ID ile cihaz bulunamadı.");
        }
    }

    public static void update(DevicePart device) {
        for (int i = 0; i < device_parts.size(); i++) {
            if (device_parts.get(i).getID() == device.getID()) {
                device_parts.set(i, device);
                save(); // Güncellemeden sonra dosyayı kaydediyoruz
            } else {
                System.out.println("[DeviceParts] ERROR | Güncellenecek cihaz bulunamadı.");
            }
        }
    }

    public static ArrayList<DevicePart> getDeviceParts() {
        return device_parts;
    }

    public static DevicePart getDevicePart(int id) {
        return device_parts.stream()
                .filter(c -> c.getID() == id)
                .findFirst()
                .orElse(null);
    }

    private static List<String> parseCsv(String csvLine) {
        List<String> result = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuote = false;

        for (int i = 0; i < csvLine.length(); i++) {
            char currentChar = csvLine.charAt(i);

            if (currentChar == '"' && (i == 0 || csvLine.charAt(i - 1) != '\\')) {
                // Eğer tırnak işareti varsa ve şu anda içinde tırnak olan bir değer varsa
                insideQuote = !insideQuote;  // Tırnaklar arasında geçiş yapıyoruz
            } else if (currentChar == ',' && !insideQuote) {
                // Virgül ve tırnak içinde değilsek, yeni bir değer başlatıyoruz
                result.add(currentValue.toString().trim());
                currentValue = new StringBuilder();  // Yeni değer için temizle
            } else {
                // Normalde değeri biriktiriyoruz
                currentValue.append(currentChar);
            }
        }
        // Son değeri de ekliyoruz
        result.add(currentValue.toString().trim());

        return result;
    }

}
