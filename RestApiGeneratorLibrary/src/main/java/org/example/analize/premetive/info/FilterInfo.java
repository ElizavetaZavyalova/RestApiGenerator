package org.example.analize.premetive.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class FilterInfo {
    final String filterName;
    String example;
    String varName;
}
