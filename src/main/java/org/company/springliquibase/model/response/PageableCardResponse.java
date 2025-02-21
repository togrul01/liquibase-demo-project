package org.company.springliquibase.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.smartcardio.Card;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableCardResponse {
    private List<CardResponse> cardList;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer lastPageNumber;

    private Long totalElements;

    private boolean hasNextPage;
}