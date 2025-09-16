package org.unibl.etf.incidentservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranslationRequest {
    private String text;
    private String sourceLang;
    private String targetLang;
}
