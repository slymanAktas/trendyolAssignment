package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class CsvUtils {
    private static final Logger LOGGER = LogManager.getLogger(CsvUtils.class);

    private CsvUtils() {
    }

    public static void writeResponseCodesToCsv(List<Map<String, Integer>> responseList) {
        try (PrintWriter writer = new PrintWriter("boutiquesResponseCodes.csv")) {

            StringBuilder sb = new StringBuilder();
            sb.append("STATUS CODE");
            sb.append(',');
            sb.append("URL");
            sb.append('\n');

            for (Map<String, Integer> response : responseList) {
                for (Map.Entry<String, Integer> entry : response.entrySet()) {
                    sb.append(entry.getValue());
                    sb.append(',');
                    sb.append(entry.getKey());
                    sb.append('\n');
                }
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void writeResponseTimesToCsv(List<Map<String, String>> boutiquesImages) {
        try (PrintWriter writer = new PrintWriter("boutiqueImagesResponseTime.csv")) {

            StringBuilder sb = new StringBuilder();
            sb.append("LOADING TIME");
            sb.append(',');
            sb.append("STATUS CODE");
            sb.append(',');
            sb.append("URL");
            sb.append('\n');

            for (Map<String, String> boutiquesImage : boutiquesImages) {
                sb.append(boutiquesImage.get("Loading Time"));
                sb.append(',');
                sb.append(boutiquesImage.get("Response Code"));
                sb.append(',');
                sb.append(boutiquesImage.get("URL"));
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
