package tr.technicalserviceapp;

import tr.technicalserviceapp.customer.Customers;
import tr.technicalserviceapp.customer.PaymentTransactions;
import tr.technicalserviceapp.device.addedpart.AddedParts;
import tr.technicalserviceapp.devicepart.DeviceParts;
import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.device.ServiceRecords;
import tr.technicalserviceapp.util.StorageFile;
import tr.technicalserviceapp.settings.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) {

        Manager.setup();

        Customers.load();
        DeviceParts.load();
        ServiceRecords.load();
        AddedParts.load();
        PaymentTransactions.load();

        try {
            Path customer_path = Manager.getPath("storage.customers_path;storage.customers_file_name");
            StorageFile.createFile(customer_path);

            Path device_parts_path = Manager.getPath("storage.device_parts_path;storage.device_parts_file_name");
            StorageFile.createFile(device_parts_path);

            Path service_records_path = Manager.getPath("storage.service_records_path;storage.service_records_file_name");
            StorageFile.createFile(service_records_path);

            Path added_part_path = Manager.getPath("storage.added_parts_path;storage.added_parts_file_name");
            StorageFile.createFile(added_part_path);

            Path payment_transaction_path = Manager.getPath("storage.payment_transactions_path;storage.payment_transactions_file_name");
            StorageFile.createFile(payment_transaction_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Theme.apply(Theme.selected());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainUI();
                frame.setVisible(true);
                System.out.println("Version 2");
            }
        });
    }
}
