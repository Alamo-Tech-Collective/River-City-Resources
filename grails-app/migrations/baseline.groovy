databaseChangeLog = {

    changeSet(author: "brandon (generated)", id: "1765934616670-1") {
        createTable(tableName: "category") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "categoryPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "display_order", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(100)") {
                constraints(nullable: "false")
            }

            column(name: "active", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(500)")
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-2") {
        createTable(tableName: "contact") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "contactPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "phone", type: "VARCHAR(50)")

            column(name: "address", type: "VARCHAR(255)")

            column(name: "date_created", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "city", type: "VARCHAR(100)") {
                constraints(nullable: "false")
            }

            column(name: "resource_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "zip_code", type: "VARCHAR(255)")

            column(name: "email", type: "VARCHAR(255)")

            column(name: "state", type: "VARCHAR(2)") {
                constraints(nullable: "false")
            }

            column(name: "website", type: "VARCHAR(500)")
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-3") {
        createTable(tableName: "eligibility_requirement") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "eligibility_requirementPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(10)") {
                constraints(nullable: "false")
            }

            column(name: "resource_id", type: "BIGINT")

            column(name: "requirement", type: "VARCHAR(500)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-4") {
        createTable(tableName: "resource") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "resourcePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "services_offered", type: "LONGTEXT")

            column(name: "date_created", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime(6)") {
                constraints(nullable: "false")
            }

            column(name: "view_count", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "active", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "approval_status", type: "VARCHAR(8)") {
                constraints(nullable: "false")
            }

            column(name: "approved_date", type: "datetime(6)")

            column(name: "submitted_by_id", type: "BIGINT")

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rejection_reason", type: "VARCHAR(500)")

            column(name: "hours_of_operation", type: "VARCHAR(500)")

            column(name: "category_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "approved_by_id", type: "BIGINT")

            column(name: "featured", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "LONGTEXT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-5") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-6") {
        createTable(tableName: "user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "userPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "first_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-7") {
        createTable(tableName: "user_role") {
            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "user_rolePK")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "user_rolePK")
            }
        }
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-8") {
        addUniqueConstraint(columnNames: "name", constraintName: "UC_CATEGORYNAME_COL", tableName: "category")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-9") {
        addUniqueConstraint(columnNames: "name", constraintName: "UC_RESOURCENAME_COL", tableName: "resource")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-10") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UC_ROLEAUTHORITY_COL", tableName: "role")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-11") {
        addUniqueConstraint(columnNames: "username", constraintName: "UC_USERUSERNAME_COL", tableName: "user")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-12") {
        addForeignKeyConstraint(baseColumnNames: "submitted_by_id", baseTableName: "resource", constraintName: "FK68gikq0bssbgdqnh6ox8m5dc0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-13") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK859n2jvi8ivhui0rl0esws6o", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-14") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FKa68196081fvovjhkek5m97n3y", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-15") {
        addForeignKeyConstraint(baseColumnNames: "approved_by_id", baseTableName: "resource", constraintName: "FKkp5x03cbme7au4xsfabeucoye", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-16") {
        addForeignKeyConstraint(baseColumnNames: "resource_id", baseTableName: "contact", constraintName: "FKmo844ntu432ktqfs0n9eejsq5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "resource", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-17") {
        addForeignKeyConstraint(baseColumnNames: "resource_id", baseTableName: "eligibility_requirement", constraintName: "FKpncve4n9gktastst45klxqeph", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "resource", validate: "true")
    }

    changeSet(author: "brandon (generated)", id: "1765934616670-18") {
        addForeignKeyConstraint(baseColumnNames: "category_id", baseTableName: "resource", constraintName: "FKqi8wwpr89wbb0pdwsma5dhysj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "category", validate: "true")
    }
}
