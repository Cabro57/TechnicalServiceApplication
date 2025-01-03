package tr.technicalserviceapp.customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer {
    private final int ID;
    private String name;
    private String id_number;
    private String email;
    private String phone_number;
    private String status;
    private Double price;
    private String address;
    private String note;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Customer (int ID, String name) {
        this.ID = ID;
        this.name = name;
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

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void addPrice(Double price) {
        this.price += price;
    }

    public void removePrice(Double price) {
        this.price -= price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        this.created_at  = LocalDateTime.parse(created_at, formatter);
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = LocalDateTime.parse(updated_at, formatter);
    }

    @Override
    public String toString() {
        return name;
    }

    public String toCsv() {
        return escapeForCsv(String.valueOf(ID)) + "," +
                escapeForCsv(name) + "," +
                escapeForCsv(id_number) + "," +
                escapeForCsv(email) + "," +
                escapeForCsv(phone_number) + "," +
                escapeForCsv(status) + "," +
                escapeForCsv(String.valueOf(price)) + "," +
                escapeForCsv(address) + "," +
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
