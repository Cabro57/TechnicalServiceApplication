package tr.technicalserviceapp.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvManager {
    private final String[] header;
    private final Path filePath;

    public CsvManager(String[] header, String fileName) throws IOException {
        if (!header[0].equalsIgnoreCase("ID")) {
            // Eğer başlıkta "ID" yoksa başa ekle
            String[] updatedHeader = new String[header.length + 1];
            updatedHeader[0] = "ID";
            System.arraycopy(header, 0, updatedHeader, 1, header.length);
            this.header = updatedHeader;
        } else {
            this.header = header;
        }

        this.filePath = Paths.get(fileName);

        if (Files.notExists(filePath)) {
            createFile();
        }
    }

    private void createFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.join(",", header)); // Başlık satırını dosyaya yaz
            writer.newLine();
        }
    }

    public void add(String[] row) throws IOException {
        List<String[]> allRows = loadAll();
        int newId = allRows.size(); // ID mevcut satır sayısına göre belirleniyor
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            writer.write(newId + "," + String.join(",", row));
            writer.newLine();
        }
    }

    public void update(int ID, String[] new_row) throws IOException {
        List<String[]> allRows = loadAll();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.join(",", header));
            writer.newLine();
            for (int i = 0; i < allRows.size(); i++) {
                if (i == ID) {
                    writer.write(ID + "," + String.join(",", new_row));
                } else {
                    writer.write(String.join(",", allRows.get(i)));
                }
                writer.newLine();
            }
        }
    }

    public void remove(int ID) throws IOException {
        List<String[]> allRows = loadAll();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.join(",", header));
            writer.newLine();
            for (int i = 0; i < allRows.size(); i++) {
                if (i != ID) {
                    writer.write(String.join(",", allRows.get(i)));
                    writer.newLine();
                }
            }
        }
    }

    public String[] load(int ID) throws IOException {
        List<String[]> allRows = loadAll();
        if (ID < 0 || ID >= allRows.size()) {
            throw new IndexOutOfBoundsException("Invalid ID: " + ID);
        }
        return Arrays.copyOfRange(allRows.get(ID), 1, allRows.get(ID).length); // ID kısmını hariç tut
    }

    public List<String[]> loadAll() throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                rows.add(line.split(","));
            }
        }
        return rows;
    }

    public List<String> loadAll(String columnName) throws IOException {
        List<String> columnData = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            String[] fileHeader = reader.readLine().split(","); // İlk satır başlık
            int columnIndex = -1;

            // Verilen başlık ismini kontrol et
            for (int i = 0; i < fileHeader.length; i++) {
                if (fileHeader[i].equalsIgnoreCase(columnName)) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex == -1) {
                throw new IllegalArgumentException("Column not found: " + columnName);
            }

            // Verilen sütundaki verileri oku
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length > columnIndex) {
                    columnData.add(row[columnIndex]);
                }
            }
        }
        return columnData;
    }

}
