package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.domain.TextOffset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@UtilityClass
public class FileUtil {

    private final Logger LOGGER = LogManager.getLogger();

    private final int CHUNK_SIZE = 1000;

    public List<TextOffset> readBigTxtFile() {
        final Properties config = readConfig();
        final Path tempBigTxt;
        try {
            LOGGER.info("Start reading file from: {}", config.getProperty("file.url"));
            tempBigTxt = Files.createTempFile("big", "txt");
            FileUtils.copyURLToFile(
                    new URL(config.getProperty("file.url")),
                    tempBigTxt.toFile(),
                    Integer.parseInt(config.getProperty("connection.timeout")),
                    Integer.parseInt(config.getProperty("read.timeout")));
            LOGGER.info("Successfully read file from: {}", config.getProperty("file.url"));
        } catch (IOException exc) {
            throw new IllegalStateException("Cannot read big.txt file!", exc);
        }

        final List<TextOffset> textOffsets = new ArrayList<>();
        try {
            final AtomicInteger offset = new AtomicInteger();
            Files.lines(tempBigTxt)
                    .filter(s -> !s.isEmpty())
                    .map(String::trim)
                    .collect(Collectors.groupingBy(e -> offset.getAndIncrement() / CHUNK_SIZE))
                    .forEach((o, c) -> textOffsets.add(new TextOffset(o * CHUNK_SIZE, c)));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot find file: " + tempBigTxt);
        }

        return textOffsets;
    }

    private Properties readConfig() {
        final Properties config = new Properties();
        String propFileName = "config.properties";

        try (InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(propFileName)) {
            config.load(is);
        } catch (IOException exc) {
            throw new IllegalStateException("Cannot read config file!", exc);
        }
        return config;
    }

}
