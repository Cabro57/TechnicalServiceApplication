package tr.technicalserviceapp.device.addedpart;

import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.StorageFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AddedParts {
    private static final Path file_path = Manager.getPath("storage.added_parts_path;storage.added_parts_file_name");
    private static ArrayList<AddedPart> added_parts;
    private static int ID;

    public static int getSafeID() {
        int maxID = added_parts.stream()
                .mapToInt(AddedPart::getID)
                .max()
                .orElse(0);
        return maxID + 1;
    }

    public static void load() {
        added_parts = new ArrayList<>();
        try (BufferedReader reader = StorageFile.getReader(file_path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCsv(line);
                if (data.size() == 6) {
                    AddedPart task = new AddedPart(Integer.parseInt(data.get(0)), Integer.parseInt(data.get(1)), Integer.parseInt(data.get(2)));
                    task.setPurchasePrice(Double.parseDouble(data.get(3)));
                    task.setSalePrice(Double.parseDouble(data.get(4)));
                    task.setDate(data.get(5));
                    added_parts.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("[AddedParts] ERROR | Dosya okunurken hata oluştu.");
        }
    }

    public static void save() {
        try (BufferedWriter writer = StorageFile.getWriter(file_path)) {
            for (AddedPart added_part : added_parts) {
                writer.write(added_part.toCsv());
                writer.newLine();
            }
            System.out.println("[AddedParts] INFO | Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("[AddedParts] ERROR | Dosya yazılırken hata oluştu.");
        }

    }

    public static void add(AddedPart added_part) {
        if (added_parts.stream().noneMatch(c -> c.getID() == added_part.getID())) {
            added_parts.add(added_part);
            save(); // Ekledikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[AddedParts] ERROR | Aynı ID'ye sahip işlem zaten var.");
        }
    }

    public static void remove(AddedPart added_part) {
        if (added_parts.remove(added_part)) {
            save(); // Silindikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[AddedParts] ERROR | İşlem bulunamadı.");
        }
    }

    public static void remove(int ID) {
        AddedPart operationToRemove = added_parts.stream()
                .filter(c -> c.getID() == ID)
                .findFirst()
                .orElse(null);
        if (operationToRemove != null) {
            remove(operationToRemove); // Diğer `remove` metodunu çağırıyoruz
        } else {
            System.out.println("[AddedParts] ERROR | Belirtilen ID ile işlem bulunamadı.");
        }
    }

    public static void update(AddedPart added_part) {
        boolean updated = false;
        for (int i = 0; i < added_parts.size(); i++) {
            if (added_parts.get(i).getID() == added_part.getID()) {
                added_parts.set(i, added_part);
                updated = true;
                break; // Güncelleme işlemi tamamlandıktan sonra döngüden çıkıyoruz
            }
        }
        if (updated) {
            save(); // Güncellemeden sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[AddedParts] ERROR | Güncellenecek işlem bulunamadı.");
        }
    }

    public static ArrayList<AddedPart> getAddedParts() {
        return added_parts;
    }

    public static AddedPart getAddedPart(int id) {
        return added_parts.stream()
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
