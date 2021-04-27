package ua.com.ciklum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoundText implements Comparable<FoundText> {

    private final int lineOffset;

    private final int charOffset;

    @Override
    public int compareTo(FoundText o) {
        return Integer.compare(this.lineOffset, o.getLineOffset());
    }
}
