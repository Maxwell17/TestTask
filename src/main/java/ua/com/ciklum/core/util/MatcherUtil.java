package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.domain.FoundText;
import ua.com.ciklum.domain.TextOffset;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@UtilityClass
public class MatcherUtil {

    private final Logger LOGGER = LogManager.getLogger();

    public CompletableFuture<Map<String, SortedSet<FoundText>>>[] runMatchers(final List<TextOffset> textOffsets,
                                                                              final Set<String> wordsToFind) {

        final CompletableFuture<Map<String, SortedSet<FoundText>>>[] matcherCompletableFuture =
                new CompletableFuture[textOffsets.size()];
        textOffsets.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> {
                    LOGGER.debug("Start working thread {}, string to find {}",
                            Thread.currentThread().getName(), wordsToFind);
                    int lineOffset = t.getOffset();
                    final Map<String, SortedSet<FoundText>> map = new HashMap<>();
                    for (final String content : t.getContent()) {
                        for (String word : wordsToFind) {
                            try {
                                int charOffset = SearchUtil.knuthMorrisPrattSearch(word.toCharArray(), content.toCharArray());
                                if (charOffset != -1) {
                                    final SortedSet<FoundText> setWords = map.getOrDefault(word, new TreeSet<>());
                                    setWords.add(new FoundText(lineOffset + 1, charOffset));
                                    map.put(word, setWords);
                                }
                            } catch (Exception exc) {
                                LOGGER.warn("Error occurred during finding world: " + word, exc);
                            }
                        }
                        lineOffset++;
                    }
                    return map;
                }))
                .collect(Collectors.toList())
                .toArray(matcherCompletableFuture);

        return matcherCompletableFuture;
    }

}
