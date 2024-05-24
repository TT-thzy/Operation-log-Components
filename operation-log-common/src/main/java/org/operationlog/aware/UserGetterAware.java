package org.operationlog.aware;

import org.springframework.beans.factory.Aware;

import java.util.Optional;
import java.util.function.Supplier;


public interface UserGetterAware extends Aware {


    void setUserGetter(Supplier<Optional<Long>> userGetter);

}
