package org.company.springliquibase.model.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableUserResponse {
    private List<UserResponse> userList;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int lastPageNumber;
    private boolean hasNextPage;
}
