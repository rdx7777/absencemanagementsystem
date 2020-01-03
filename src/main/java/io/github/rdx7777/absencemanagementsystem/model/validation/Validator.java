package io.github.rdx7777.absencemanagementsystem.model.validation;

import java.util.List;

public abstract class Validator {

    protected static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
        if (resultOfValidation != null) {
            resultList.add(resultOfValidation);
        }
    }

    protected static void addResultOfValidation(List<String> resultList, List<String> resultsOfValidationList) {
        resultList.addAll(resultsOfValidationList);
    }
}
