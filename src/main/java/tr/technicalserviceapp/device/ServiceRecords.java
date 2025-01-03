package tr.technicalserviceapp.device;

import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.StorageFile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ServiceRecords {
    private static final Path file_path = Manager.getPath("storage.service_records_path;storage.service_records_file_name");
    private static ArrayList<ServiceRecord> service_records;
    private static int ID;

    public static int getSafeID() {
        int maxID = service_records.stream()
                .mapToInt(ServiceRecord::getID)
                .max()
                .orElse(0);
        return maxID + 1;
    }

    public static void load() {
        service_records = new ArrayList<>();
        try (BufferedReader reader = StorageFile.getReader(file_path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCsv(line);
                if (data.size() == 23) {
                    ServiceRecord task = new ServiceRecord(Integer.parseInt(data.get(0)), Integer.parseInt(data.get(1)), data.get(2));
                    task.setBrand(data.get(3));
                    task.setModel(data.get(4));
                    task.setSerial_number(data.get(5));
                    task.setPassword(data.get(6));
                    task.setUrgency(data.get(7));
                    task.setAccessory(data.get(8));
                    task.setReported_fault(data.get(9));
                    task.setDetected_fault(data.get(10));
                    task.setService_fee(Double.parseDouble(data.get(11)));
                    task.setPayment_status(data.get(12));
                    task.setService_status(data.get(13));
                    task.setOrdered_part(data.get(14));
                    task.setOperation_made(data.get(15));
                    task.setWarranty_status(Boolean.parseBoolean(data.get(16)));
                    task.setShipping_fee(Double.parseDouble(data.get(17)));
                    task.setDelivery_type(data.get(18));
                    if (!data.get(19).trim().isEmpty()) {
                        task.setWarranty_end_date(data.get(19));
                    }
                    if (!data.get(20).trim().isEmpty()) {
                        task.setDelivery_date(data.get(20));
                    }
                    if (!data.get(21).trim().isEmpty()) {
                        task.setCreated_at(data.get(21));
                    }
                    if (!data.get(22).trim().isEmpty()) {
                        task.setUpdated_at(data.get(22));
                    }
                    service_records.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("[ServiceRecords] ERROR | Dosya okunurken hata oluştu.");
        }
    }


    public static void save() {
        try (BufferedWriter writer = StorageFile.getWriter(file_path)) {
            for (ServiceRecord Operation : service_records) {
                writer.write(Operation.toCsv());
                writer.newLine();
            }
            System.out.println("[ServiceRecords] INFO | Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("[ServiceRecords] ERROR | Dosya yazılırken hata oluştu.");
        }

    }

    public static void add(ServiceRecord operation) {
        if (service_records.stream().noneMatch(c -> c.getID() == operation.getID())) {
            service_records.add(operation);
            save(); // Ekledikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[ServiceRecords] ERROR | Aynı ID'ye sahip işlem zaten var.");
        }
    }

    public static void remove(ServiceRecord Operation) {
        if (service_records.remove(Operation)) {
            save(); // Silindikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[ServiceRecords] ERROR | İşlem bulunamadı.");
        }
    }

    public static void remove(int ID) {
        ServiceRecord operationToRemove = service_records.stream()
                .filter(c -> c.getID() == ID)
                .findFirst()
                .orElse(null);
        if (operationToRemove != null) {
            remove(operationToRemove); // Diğer `remove` metodunu çağırıyoruz
        } else {
            System.out.println("[ServiceRecords] ERROR | Belirtilen ID ile işlem bulunamadı.");
        }
    }

    public static void update(ServiceRecord operation) {
        boolean updated = false;
        for (int i = 0; i < service_records.size(); i++) {
            if (service_records.get(i).getID() == operation.getID()) {
                service_records.set(i, operation);
                updated = true;
                break; // Güncelleme işlemi tamamlandıktan sonra döngüden çıkıyoruz
            }
        }
        if (updated) {
            save(); // Güncellemeden sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[ServiceRecords] ERROR | Güncellenecek işlem bulunamadı.");
        }
    }

    public static ArrayList<ServiceRecord> getServiceRecords() {
        return service_records;
    }

    public static ServiceRecord getServiceRecord(int id) {
        return service_records.stream()
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
