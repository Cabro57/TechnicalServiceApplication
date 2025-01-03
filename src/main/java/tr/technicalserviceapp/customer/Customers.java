package tr.technicalserviceapp.customer;

import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.StorageFile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Customers {
    private static final Path file_path = Manager.getPath("storage.customers_path;storage.customers_file_name");
    private static ArrayList<Customer> customers;
    private static int ID;

    public static int getSafeID() {
        while (customers.stream().anyMatch(c -> c.getID() == ID)) {
            ID++;
        }
        return ID;
    }

    public static void load() {
        customers = new ArrayList<>();
        try (BufferedReader reader = StorageFile.getReader(file_path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCsv(line);
                if (data.size() == 11) {
                    Customer task = new Customer(Integer.parseInt(data.get(0)), data.get(1));
                    task.setId_number(data.get(2));
                    task.setEmail(data.get(3));
                    task.setPhone_number(data.get(4));
                    task.setStatus(data.get(5));
                    task.setPrice(Double.parseDouble(data.get(6)));
                    task.setAddress(data.get(7));
                    task.setNote(data.get(8));
                    task.setCreated_at(data.get(9));
                    task.setUpdated_at(data.get(10));
                    customers.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("[Customers] ERROR | Dosya okunurken hata oluştu.");
        }
    }

    public static void save() {
        try (BufferedWriter writer = StorageFile.getWriter(file_path)) {
            for (Customer customer : customers) {
                writer.write(customer.toCsv());
                writer.newLine();
            }
            System.out.println("[Customers] INFO | Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("[Customers] ERROR | Dosya yazılırken hata oluştu.");
        }
    }

    public static void add(Customer customer) {
        if (customers.stream().noneMatch(c -> c.getID() == customer.getID())) {
            customers.add(customer);
            save(); // Ekledikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[Customers] ERROR | Aynı ID'ye sahip müşteri zaten var.");
        }
    }

    public static void remove(Customer customer) {
        if (customers.remove(customer)) {
            System.out.println("[Customers] INFO | Silindi");
            save(); // Silindikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[Customers] ERROR | Müşteri bulunamadı.");
        }
    }

    public static void remove(int ID) {
        Customer customerToRemove = customers.stream()
                .filter(c -> c.getID() == ID)
                .findFirst()
                .orElse(null);
        if (customerToRemove != null) {
            remove(customerToRemove); // Diğer `remove` metodunu çağırıyoruz
        } else {
            System.out.println("[Customers] ERROR | Belirtilen ID ile müşteri bulunamadı.");
        }
    }

    public static void update(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID() == customer.getID()) {
                customers.set(i, customer);
                save(); // Güncellemeden sonra dosyayı kaydediyoruz
            } else {
                System.out.println("[Customers] ERROR | Güncellenecek müşteri bulunamadı.");
            }
        }
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public static Customer getCustomer(int id) {
        return customers.stream()
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
