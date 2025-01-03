package tr.technicalserviceapp.customer;

import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.StorageFile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PaymentTransactions {
    private static final Path file_path = Manager.getPath("storage.payment_transactions_path;storage.payment_transactions_file_name");
    private static ArrayList<PaymentTransaction> payment_transactions;
    private static int ID;

    public static int getSafeID() {
        while (payment_transactions.stream().anyMatch(c -> c.getID() == ID)) {
            ID++;
        }
        return ID;
    }

    public static void load() {
        payment_transactions = new ArrayList<>();
        try (BufferedReader reader = StorageFile.getReader(file_path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCsv(line);
                if (data.size() == 6) {
                    PaymentTransaction task = new PaymentTransaction(Integer.parseInt(data.get(0)));
                    task.setCustomer_id(Integer.parseInt(data.get(1)));
                    task.setAmount(Double.parseDouble(data.get(2)));
                    task.setPayment_type(data.get(3));
                    task.setNote(data.get(4));
                    task.setDate(data.get(5));
                    payment_transactions.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("[Customers] ERROR | Dosya okunurken hata oluştu.");
        }
    }

    public static void save() {
        try (BufferedWriter writer = StorageFile.getWriter(file_path)) {
            for (PaymentTransaction payment_transaction : payment_transactions) {
                writer.write(payment_transaction.toCsv());
                writer.newLine();
            }
            System.out.println("[PaymentTransactions] INFO | Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("[PaymentTransactions] ERROR | Dosya yazılırken hata oluştu.");
        }
    }

    public static void add(PaymentTransaction payment_transaction) {
        if (payment_transactions.stream().noneMatch(c -> c.getID() == payment_transaction.getID())) {
            payment_transactions.add(payment_transaction);
            save(); // Ekledikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[PaymentTransactions] ERROR | Aynı ID'ye sahip müşteri zaten var.");
        }
    }

    public static void remove(PaymentTransaction payment_transaction) {
        if (payment_transactions.remove(payment_transaction)) {
            System.out.println("[PaymentTransactions] INFO | Silindi");
            save(); // Silindikten sonra dosyayı kaydediyoruz
        } else {
            System.out.println("[PaymentTransactions] ERROR | Müşteri bulunamadı.");
        }
    }

    public static void remove(int ID) {
        PaymentTransaction paymentTransactionToRemove = payment_transactions.stream()
                .filter(c -> c.getID() == ID)
                .findFirst()
                .orElse(null);
        if (paymentTransactionToRemove != null) {
            remove(paymentTransactionToRemove); // Diğer `remove` metodunu çağırıyoruz
        } else {
            System.out.println("[PaymentTransactions] ERROR | Belirtilen ID ile müşteri bulunamadı.");
        }
    }

    public static void update(PaymentTransaction payment_transaction) {
        for (int i = 0; i < payment_transactions.size(); i++) {
            if (payment_transactions.get(i).getID() == payment_transaction.getID()) {
                payment_transactions.set(i, payment_transaction);
                save(); // Güncellemeden sonra dosyayı kaydediyoruz
            } else {
                System.out.println("[PaymentTransactions] ERROR | Güncellenecek müşteri bulunamadı.");
            }
        }
    }

    public static ArrayList<PaymentTransaction> getPayment_transactions() {
        return payment_transactions;
    }

    public static PaymentTransaction getPaymentTransaction(int id) {
        return payment_transactions.stream()
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
                insideQuote = !insideQuote;
            } else if (currentChar == ',' && !insideQuote) {
                result.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(currentChar);
            }
        }
        result.add(currentValue.toString().trim());

        return result;
    }



}
