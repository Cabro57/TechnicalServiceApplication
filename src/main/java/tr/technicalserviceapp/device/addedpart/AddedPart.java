package tr.technicalserviceapp.device.addedpart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddedPart {
    int ID;
    int device_ID;
    int device_part_ID;
    double purchase_price;
    double sale_price;
    LocalDateTime date;

    public AddedPart(int ID, int device_ID, int device_part_ID) {
        this.ID = ID;
        this.device_ID = device_ID;
        this.device_part_ID = device_part_ID;
    }

    public int getID() {
        return ID;
    }

    public int getDeviceID() {
        return device_ID;
    }

    public void setDeviceID(int device_ID) {
        this.device_ID = device_ID;
    }

    public int getDevicePartID() {
        return device_part_ID;
    }

    public void setDevicePartID(int device_part_ID) {
        this.device_part_ID = device_part_ID;
    }

    public double getPurchasePrice() {
        return purchase_price;
    }

    public void setPurchasePrice(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getSalePrice() {
        return sale_price;
    }

    public void setSalePrice(double sale_price) {
        this.sale_price = sale_price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.date = LocalDateTime.parse(date, formatter);
    }

    public String toCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return escapeForCsv(String.valueOf(ID)) + "," +
                escapeForCsv(String.valueOf(device_ID)) + "," +
                escapeForCsv(String.valueOf(device_part_ID)) + "," +
                escapeForCsv(String.valueOf(purchase_price)) + "," +
                escapeForCsv(String.valueOf(sale_price)) + "," +
                escapeForCsv(date.format(formatter));
    }


    private static String escapeForCsv(String value) {
        if (value == null) {
            return "";
        }
        // Eğer değer içinde tırnak ya da virgül varsa, o zaman bu karakterleri işlemek gerek
        value = value.replace("\"", "\"\"");  // Tırnağı iki katına çıkar

        // Eğer değer içinde virgül, yeni satır ya da tırnak varsa, değeri tırnak içine al
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return value;
    }

}
