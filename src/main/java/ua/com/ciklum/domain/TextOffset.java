package ua.com.ciklum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TextOffset {

    private int offset;

    private List<String> content;

}
