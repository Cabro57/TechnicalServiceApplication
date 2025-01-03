package tr.technicalserviceapp.device;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceRecord {

    private final int ID;
    private int customer;
    private String name;
    private String brand;
    private String model;
    private String serial_number;
    private String password;
    private String urgency;
    private String accessory;
    private String reported_fault;
    private String detected_fault;
    private double service_fee;
    private String payment_status;
    private String service_status;
    private String ordered_part;
    private String operation_made;
    private boolean warranty_status;
    private double shipping_fee;
    private String delivery_type;
    private LocalDate warranty_end_date;
    private LocalDateTime delivery_date;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ServiceRecord(int id, int customer, String name) {
        this.ID = id;
        this.customer = customer;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getReported_fault() {
        return reported_fault;
    }

    public void setReported_fault(String reported_fault) {
        this.reported_fault = reported_fault;
    }

    public String getDetected_fault() {
        return detected_fault;
    }

    public void setDetected_fault(String detected_fault) {
        this.detected_fault = detected_fault;
    }

    public double getService_fee() {
        return service_fee;
    }

    public void setService_fee(double service_fee) {
        this.service_fee = service_fee;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getOrdered_part() {
        return ordered_part;
    }

    public void setOrdered_part(String ordered_part) {
        this.ordered_part = ordered_part;
    }

    public String getOperation_made() {
        return operation_made;
    }

    public void setOperation_made(String operation_made) {
        this.operation_made = operation_made;
    }

    public boolean isWarranty_status() {
        return warranty_status;
    }

    public void setWarranty_status(boolean warranty_status) {
        this.warranty_status = warranty_status;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public LocalDate getWarranty_end_date() {
        return warranty_end_date;
    }

    public void setWarranty_end_date(LocalDate warranty_end_date) {
        this.warranty_end_date = warranty_end_date;
    }

    public void setWarranty_end_date(String warranty_end_date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.warranty_end_date = LocalDate.parse(warranty_end_date, formatter);
    }

    public LocalDateTime getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDateTime delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.delivery_date = LocalDateTime.parse(delivery_date, formatter);
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

    public String toCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String warrantyEndDateStr = (warranty_end_date != null) ? warranty_end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
        String deliveryDateStr = (delivery_date != null) ? delivery_date.format(formatter) : "";
        String createdAtStr = (created_at != null) ? created_at.format(formatter) : "";
        String updatedAtStr = (updated_at != null) ? updated_at.format(formatter) : "";

        return escapeForCsv(String.valueOf(ID)) + "," +
                escapeForCsv(String.valueOf(customer)) + "," +
                escapeForCsv(name) + "," +
                escapeForCsv(brand) + "," +
                escapeForCsv(model) + "," +
                escapeForCsv(serial_number) + "," +
                escapeForCsv(password) + "," +
                escapeForCsv(urgency) + "," +
                escapeForCsv(accessory) + "," +
                escapeForCsv(reported_fault) + "," +
                escapeForCsv(detected_fault) + "," +
                escapeForCsv(String.valueOf(service_fee)) + "," +
                escapeForCsv(payment_status) + "," +
                escapeForCsv(service_status) + "," +
                escapeForCsv(ordered_part) + "," +
                escapeForCsv(operation_made) + "," +
                escapeForCsv(String.valueOf(warranty_status)) + "," +
                escapeForCsv(String.valueOf(shipping_fee)) + "," +
                escapeForCsv(delivery_type) + "," +
                escapeForCsv(warrantyEndDateStr) + "," +
                escapeForCsv(deliveryDateStr) + "," +
                escapeForCsv(createdAtStr) + "," +
                escapeForCsv(updatedAtStr);
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
