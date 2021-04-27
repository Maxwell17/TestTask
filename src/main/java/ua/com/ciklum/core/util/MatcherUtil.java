package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.core.MatcherThread;
import ua.com.ciklum.domain.FoundText;
import ua.com.ciklum.domain.TextOffset;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class MatcherUtil {

    private final Logger LOGGER = LogManager.getLogger();

    public SortedSet<FoundText> runMatchers(final List<TextOffset> textOffsets, final String toFind) {

        final SortedSet<FoundText> foundTexts = Collections.synchronizedSortedSet(new TreeSet<>());

        final CountDownLatch matcherSynchronizer = new CountDownLatch(textOffsets.size());
        final List<Thread> matcherThreadPool = textOffsets.stream()
                .map((Function<TextOffset, Thread>) t -> new MatcherThread(toFind, t, foundTexts, matcherSynchronizer))
                .collect(Collectors.toUnmodifiableList());

        matcherThreadPool.forEach(Thread::start);
        try {
            LOGGER.debug("Matchers ready to start");
            matcherSynchronizer.await();
            LOGGER.debug("Matchers completed");
        } catch (InterruptedException exc) {
            throw new IllegalStateException("Matcher threads are waiting or interrupted", exc);
        }
        return Collections.unmodifiableSortedSet(foundTexts);
    }

}
