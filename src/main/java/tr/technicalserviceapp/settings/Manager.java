package tr.technicalserviceapp.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tr.technicalserviceapp.App;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {

    private static String file_path;
    private static final Logger logger = Logger.getLogger(Manager.class.getName());

    public static void setup () {
        String appdata = System.getenv("APPDATA");
        file_path = appdata.replace("\\", "/") + "/.technical-service-application/settings.json";

        Path path = Paths.get(file_path);

        if (!Files.exists(path)) {
            try (InputStream input_stream = App.class.getResourceAsStream("/settings.json")) {
                if (input_stream != null) {
                    Files.createDirectories(path.getParent());
                    Files.copy(input_stream, path);
                    logger.info("[Manager] INFO | Settings dosyası başarıyla oluşturuldu: " + file_path);
                } else {
                    logger.warning("[Manager] WARN | Kopyalanacak settings dosyası bulunamadı.");
                }
            } catch (IOException e) {
                logger.severe("[Manager] ERROR | Settings dosyası oluşturulurken hata oluştu.\n" + e);
            }
        } else {
            logger.warning("[Manager] WARN | Settings dosyası zaten mevcut");
        }
    }

    public static Object getSetting(String key_chain) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            // JSON dosyasını yükle ve kök elemanını al
            JsonElement rootElement = JsonParser.parseReader(reader);

            // JSON nesnesi olarak kontrol et
            if (rootElement.isJsonObject()) {
                JsonObject currentObject = rootElement.getAsJsonObject();

                // Anahtar zincirini "." ile böl
                String[] keys = key_chain.split("\\.");

                // Zincir boyunca JSON'u gez
                for (int i = 0; i < keys.length; i++) {
                    if (currentObject.has(keys[i])) {
                        JsonElement nextElement = currentObject.get(keys[i]);

                        // Son anahtar mı? String olarak döndür
                        if (i == keys.length - 1) {
                            if (nextElement.isJsonPrimitive()) {
                                String value = nextElement.getAsString();
                                return value.isEmpty() ? null : value; // Boş string kontrolü
                            } else {
                                return nextElement;
                            }
                        }

                        // Sonraki eleman bir nesne mi? Öyleyse ilerle
                        if (nextElement.isJsonObject()) {
                            currentObject = nextElement.getAsJsonObject();
                        } else {
                            return null; // Ara adımda bir nesne bulunmazsa null
                        }
                    } else {
                        logger.warning("[Manager] WARN | Anahtar bulunamadı: " + keys[i]);
                        return null; // Anahtar bulunamazsa null
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("[Manager] ERROR | JSON dosyası okunamadı: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("[Manager] ERROR | Beklenmeyen hata: " + e.getMessage());
        }

        return null;
    }


    public static void setSetting(String key_chain, String value) {
        try {
            // JSON'u yükle
            File file = new File(file_path);
            JsonObject rootObject;
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    rootObject = JsonParser.parseReader(reader).getAsJsonObject();
                }
            } else {
                rootObject = new JsonObject();
            }

            // Anahtar zincirini "." ile böl
            String[] keys = key_chain.split("\\.");
            JsonObject currentObject = rootObject;

            // Zincir boyunca dolaş ve gerekli JSON yapısını oluştur
            for (int i = 0; i < keys.length - 1; i++) {
                if (!currentObject.has(keys[i]) || !currentObject.get(keys[i]).isJsonObject()) {
                    currentObject.add(keys[i], new JsonObject());
                }
                currentObject = currentObject.getAsJsonObject(keys[i]);
            }

            // Son anahtarı güncelle
            currentObject.addProperty(keys[keys.length - 1], value);

            // Güncellenmiş JSON'u dosyaya yaz
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(rootObject.toString());
            }

            logger.info("[Manager] INFO | Ayar başarıyla güncellendi: " + key_chain);
        } catch (IOException e) {
            logger.severe("[Manager] ERROR | Ayar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    public static Path getPath(String combined_key_chain) {
        String[] key_chains = combined_key_chain.split(";");
        StringBuilder combined_path = new StringBuilder();

        for (String key_chain : key_chains) {
            String setting = (String) getSetting(key_chain);

            // Eğer ayar boşsa null döndür
            if (setting == null || setting.isEmpty()) {
                logger.warning("[Manager] WARN | Girilen anahtar bulunamadı veya boş: " + key_chain);
                return null;
            }

            String regex = "\\{(.*?)\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(setting);

            StringBuffer resolved_path = new StringBuffer();

            while (matcher.find()) {
                String variableName = matcher.group(1); // {} içindeki değişken adı
                String replacement = System.getenv(variableName); // Çevre değişkeninden değeri al
                if (replacement == null) {
                    replacement = ""; // Eğer değişken bulunamazsa boş string ile değiştir
                }
                matcher.appendReplacement(resolved_path, replacement.replace("\\", "/"));
            }
            matcher.appendTail(resolved_path);

            if (resolved_path.length() == 0 && key_chain.contains("RESOURCES")) {
                URL resourceUrl = Manager.class.getResource("/" + setting); // Kaynağı çözümleme
                if (resourceUrl != null) {
                    resolved_path.append(resourceUrl.getPath()); // Kaynağın yolunu al
                } else {
                    logger.warning("[Manager] WARN | Kaynak bulunamadı: " + setting);
                    return null;
                }
            }

            if (combined_path.length() > 0) {
                combined_path.append(File.separator);
            }
            combined_path.append(resolved_path);
        }
        return Paths.get(combined_path.toString());
    }
}
