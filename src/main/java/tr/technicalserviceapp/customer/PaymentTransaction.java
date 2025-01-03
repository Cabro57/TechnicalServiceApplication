package tr.technicalserviceapp.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentTransaction {
    private int ID;
    private int customer_id;
    private double amount;
    private String payment_type;
    private String note;
    private LocalDate date;

    public PaymentTransaction(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date = LocalDate.parse(date, formatter);
    }

    public String toCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return escapeForCsv(String.valueOf(getID())) + "," +
                escapeForCsv(String.valueOf(customer_id)) + "," +
                escapeForCsv(String.valueOf(amount)) + "," +
                escapeForCsv(payment_type) + "," +
                escapeForCsv(note) + "," +
                escapeForCsv(date.format(formatter));
    }

    private static String escapeForCsv(String value) {
        if (value == null) {
            return "";
        }
        value = value.replace("\"", "\"\"");
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return value;
    }
}
