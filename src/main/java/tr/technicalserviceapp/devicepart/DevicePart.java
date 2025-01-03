package tr.technicalserviceapp.devicepart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DevicePart {
    private final int ID;
    private String name;
    private String barcode;
    private String type;
    private String supplier;
    private String brand;
    private String model;
    private String category;
    private String warranty;
    private double purchase_price;
    private double sale_price;
    private int stock;
    private String note;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public DevicePart(int ID, String name, String barcode, String type) {
        this.ID = ID;
        this.name = name;
        this.barcode = barcode;
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setCreated_at(String created_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.created_at  = LocalDateTime.parse(created_at, formatter);
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setUpdated_at(String updated_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.updated_at = LocalDateTime.parse(updated_at, formatter);
    }

    @Override
    public String toString() {
        return name + " (" + brand + " " + model + ")";
    }

    public String toCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return escapeForCsv(String.valueOf(ID)) + "," +
                escapeForCsv(name) + "," +
                escapeForCsv(barcode) + "," +
                escapeForCsv(type) + "," +
                escapeForCsv(supplier) + "," +
                escapeForCsv(brand) + "," +
                escapeForCsv(model) + "," +
                escapeForCsv(category) + "," +
                escapeForCsv(warranty) + "," +
                escapeForCsv(String.valueOf(purchase_price)) + "," +
                escapeForCsv(String.valueOf(sale_price)) + "," +
                escapeForCsv(String.valueOf(stock)) + "," +
                escapeForCsv(note) + "," +
                escapeForCsv(created_at.format(formatter)) + "," +
                escapeForCsv(updated_at.format(formatter));
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
