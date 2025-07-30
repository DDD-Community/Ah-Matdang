package be.ddd.infra.function;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class FullTextMatchFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions contributions) {
        contributions
                .getFunctionRegistry()
                .registerPattern(
                        "fulltext_match",
                        "MATCH(?1) AGAINST (?2 IN BOOLEAN MODE)",
                        contributions
                                .getTypeConfiguration()
                                .getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.DOUBLE));
    }
}
