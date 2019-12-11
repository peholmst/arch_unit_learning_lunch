package com.packagename.myapp.domain.model;

import java.io.Serializable;

public interface ValueObject extends Serializable {

    int hashCode();

    boolean equals(Object other);
}
