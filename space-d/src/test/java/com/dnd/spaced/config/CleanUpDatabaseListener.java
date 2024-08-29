package com.dnd.spaced.config;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class CleanUpDatabaseListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        EntityManager em = findEntityManager(testContext);
        List<String> tableNames = calculateTableNames(em);

        clean(em, tableNames);
    }

    private EntityManager findEntityManager(TestContext testContext) {
        return testContext.getApplicationContext()
                          .getBean(EntityManager.class);
    }

    private List<String> calculateTableNames(EntityManager em) {
        return em.getMetamodel()
                 .getEntities()
                 .stream()
                 .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                 .map(this::calculateTableName)
                 .toList();
    }

    private String calculateTableName(EntityType<?> entityType) {
        Table tableAnnotation = entityType.getJavaType().getAnnotation(Table.class);

        if (tableAnnotation != null) {
            return tableAnnotation.name().toLowerCase();
        }

        return convertToSnakeCase(entityType.getName());
    }

    private String convertToSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }


    private void clean(EntityManager em, List<String> tableNames) {
        em.flush();

        StringBuilder sb = new StringBuilder("SET REFERENTIAL_INTEGRITY FALSE;");

        for (final String tableName : tableNames) {
            sb.append("TRUNCATE TABLE ")
              .append(tableName)
              .append(";");

            sb.append("ALTER TABLE ")
              .append(tableName)
              .append(" ALTER COLUMN id RESTART WITH 1;");
        }

        sb.append("SET REFERENTIAL_INTEGRITY TRUE;");

        em.createNativeQuery(sb.toString()).executeUpdate();
    }
}
