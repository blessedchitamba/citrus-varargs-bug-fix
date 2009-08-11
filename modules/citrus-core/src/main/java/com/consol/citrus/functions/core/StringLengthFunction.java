package com.consol.citrus.functions.core;

import java.util.List;

import com.consol.citrus.exceptions.InvalidFunctionUsageException;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.functions.Function;

public class StringLengthFunction implements Function {

    public String execute(List<String> parameterList) throws CitrusRuntimeException {
        if (parameterList == null || parameterList.isEmpty()) {
            throw new InvalidFunctionUsageException("Function parameters must not be empty");
        }

        return Integer.valueOf((parameterList.get(0)).length()).toString();
    }

}
