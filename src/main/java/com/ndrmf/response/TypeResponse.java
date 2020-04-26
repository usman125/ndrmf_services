package com.ndrmf.response;

import java.io.Serializable;
import java.util.List;

public class TypeResponse extends ServiceResponse implements Serializable {

    private List<String> typeNames;

    public List<String> getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }
}
