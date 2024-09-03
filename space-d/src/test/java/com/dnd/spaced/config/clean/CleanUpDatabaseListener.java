package com.dnd.spaced.config.clean;

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
        List<EntityType<?>> entityTypes = findEntityTypes(em);

        clean(em, entityTypes);
    }

    private EntityManager findEntityManager(TestContext testContext) {
        return testContext.getApplicationContext()
                          .getBean(EntityManager.class);
    }

    private List<EntityType<?>> findEntityTypes(EntityManager em) {
        return em.getMetamodel()
                 .getEntities()
                 .stream()
                 .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                 .toList();
    }

    private void clean(EntityManager em, List<EntityType<?>> entityTypes) {
        em.flush();

        StringBuilder sb = new StringBuilder("SET REFERENTIAL_INTEGRITY FALSE;");

        for (EntityType<?> entityType : entityTypes) {
            String tableName = calculateTableName(entityType);

            sb.append("TRUNCATE TABLE ")
              .append(tableName)
              .append(";");

            if (Long.class.equals(entityType.getIdType().getJavaType())) {
                sb.append("ALTER TABLE ")
                  .append(tableName)
                  .append(" ALTER COLUMN id RESTART WITH 1;");
            }
        }

        sb.append("SET REFERENTIAL_INTEGRITY TRUE;");

        em.createNativeQuery(sb.toString())
          .executeUpdate();
    }

    private String calculateTableName(EntityType<?> entityType) {
        Table tableAnnotation = entityType.getJavaType().getAnnotation(Table.class);

        if (tableAnnotation != null) {
            return tableAnnotation.name()
                                  .toLowerCase();
        }

        return convertToSnakeCase(entityType.getName());
    }

    private String convertToSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2")
                    .toLowerCase();
    }
}
