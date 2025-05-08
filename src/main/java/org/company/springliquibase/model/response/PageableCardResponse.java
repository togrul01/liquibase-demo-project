package org.company.springliquibase.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableCardResponse {
    private List<CardResponse> cardList;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int lastPageNumber;
    private boolean hasNextPage;
}