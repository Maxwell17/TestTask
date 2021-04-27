package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import ua.com.ciklum.domain.TextOffset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@UtilityClass
public class FileUtil {

    private final int CHUNK_SIZE = 1000;

    public List<TextOffset> readFile(final String fileName) {
        final List<TextOffset> textOffsets = new ArrayList<>();
        try {
            final AtomicInteger offset = new AtomicInteger();
            Files.lines(Path.of(fileName))
                    .filter(s -> !s.isEmpty())
                    .map(String::trim)
                    .collect(Collectors.groupingBy(e -> offset.getAndIncrement() / CHUNK_SIZE))
                    .forEach((o, c) -> textOffsets.add(new TextOffset(o * CHUNK_SIZE, c)));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot find file: " + fileName);
        }

        return textOffsets;
    }

}
