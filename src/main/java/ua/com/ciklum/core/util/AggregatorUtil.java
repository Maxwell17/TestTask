package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.core.exception.AlgoException;
import ua.com.ciklum.domain.FoundText;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@UtilityClass
public class AggregatorUtil {

    private final Logger LOGGER = LogManager.getLogger();

    public void runAggregator(final CompletableFuture<Map<String, SortedSet<FoundText>>>[] matcherCompletableFuture) {

        final Map<String, SortedSet<FoundText>> foundWordResult = new ConcurrentSkipListMap<>();
        try {
            Arrays.stream(matcherCompletableFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.toUnmodifiableList())
                    .forEach(m -> m.forEach((k, v) -> {
                        if (!foundWordResult.containsKey(k)) {
                            foundWordResult.put(k, v);
                        } else {
                            final SortedSet<FoundText> foundTexts = foundWordResult.get(k);
                            foundTexts.addAll(v);
                            foundWordResult.put(k, foundTexts);
                        }
                    }));
        } catch (Exception exc) {
            throw new AlgoException("Cannot aggregate results!", exc);
        }
        foundWordResult.forEach((toFind, foundTexts) -> {
            final StringBuilder print = new StringBuilder(toFind + "-->[");
            for (final FoundText foundText : foundTexts) {
                print
                        .append("[lineOffset=")
                        .append(foundText.getLineOffset())
                        .append(",charOffset=")
                        .append(foundText.getCharOffset())
                        .append("]");
            }
            print.append("]");
            LOGGER.info(print);
        });
    }
}
