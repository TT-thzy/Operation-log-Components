package org.operationlog.domain;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class MongoCollectionIndexKey {

    private String property;

    private Sort.Direction direction;
}
