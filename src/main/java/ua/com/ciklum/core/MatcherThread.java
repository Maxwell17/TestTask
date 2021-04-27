package ua.com.ciklum.core;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.core.exception.AlgoException;
import ua.com.ciklum.core.util.SearchUtil;
import ua.com.ciklum.domain.FoundText;
import ua.com.ciklum.domain.TextOffset;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
public class MatcherThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String toFind;

    private final TextOffset textOffset;

    private final Set<FoundText> foundTexts;

    private final CountDownLatch matcherSynchronizer;

    @Override
    public void run() {
        try {
            LOGGER.debug("Start working thread {}, string to find {}", Thread.currentThread().getName(), this.toFind);
            int lineOffset = textOffset.getOffset();
            for (final String content : this.textOffset.getContent()) {
                int charOffset = SearchUtil.knuthMorrisPrattSearch(toFind.toCharArray(), content.toCharArray());
                if (charOffset != -1) {
                    this.foundTexts.add(new FoundText(lineOffset + 1, charOffset));
                }
                lineOffset++;
            }
        } catch (Exception exc) {
            throw new AlgoException("Error occurred during finding world: " + toFind, exc);
        } finally {
            matcherSynchronizer.countDown();
        }
    }

}
