package org.company.springliquibase.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableUserResponse {
    List<UserResponse> userResponses;
    int lastPageNumber;
    long totalElements;
    boolean hasNextPage;
}
