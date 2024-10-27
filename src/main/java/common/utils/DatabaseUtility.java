package common.utils;

import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.UserSettings;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {
    private Session sshSession;
    private Connection dbConnection;
    public DSLContext globalContext;
    private final BasicUtility util = new BasicUtility();

    //<editor-fold desc="DB Connection">
    public void dbConnect(String dbName) {
        HikariDataSource dataSource;
        String url = "jdbc:mysql://${domain}:${port}/${dbName}",
                user = UserSettings.runOnStage ? UserSettings.stageDbUser : "root",
                password = UserSettings.runOnStage ? UserSettings.stageDbPassword : "root", dbPort = "3306";
        HikariConfig config = new HikariConfig();
        if (UserSettings.runOnStage) {
            int localPort = util.getFreePort();
            sshSession = util.sshSessionStart(UserSettings.stageDbDockerIp, localPort, 3306);
            dbPort = Integer.toString(localPort);
        }
        url = url.replace("${domain}", "127.0.0.1").replace("${port}", dbPort).replace("${dbName}", dbName);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(3);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(10000);
        config.setMaxLifetime(15000);
        dataSource = new HikariDataSource(config);
        try {
            dbConnection = DriverManager.getConnection(url, user, password);
            globalContext = DSL.using(dataSource, SQLDialect.MYSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbDisconnect() {
        try {
            dbConnection.close();
            if (sshSession != null) {
                util.sshSessionStop(sshSession);
            }
        } catch (Exception e) {
            System.err.println("Error on closing DB connection: ");
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Data">

    public Result<org.jooq.Record> getDbDataResult(String tableName, Condition condition) {
        dbConnect(SettingsHelper.getDatabaseName());
        if (condition == null) {
            condition = DSL.noCondition();
        }
        System.out.println(globalContext.select().from(tableName).where(condition).getQuery());
        Result<org.jooq.Record> result = globalContext.select().from(tableName).where(condition).fetch();
        dbDisconnect();
        return result;
    }

    public Result<org.jooq.Record> getDbDataResultInnerJoin(String[] fieldsSelect, String tableFromName, String tableJoinName, Condition joinCondition, Condition whereCondition) {
        dbConnect(SettingsHelper.getDatabaseName());
        Field<?>[] fields = new Field<?>[fieldsSelect.length];
        for (int i = 0; i < fieldsSelect.length; i++) {
            fields[i] = DSL.field(fieldsSelect[i]); // Dynamic field name and type
        }
        if (whereCondition == null) {
            whereCondition = DSL.noCondition();
        }
        System.out.println(globalContext.select(fields).from(tableFromName).innerJoin(tableJoinName).on(joinCondition).where(whereCondition).getQuery());
        Result<org.jooq.Record> result = globalContext.select(fields).from(tableFromName).innerJoin(tableJoinName).on(joinCondition).where(whereCondition).fetch();

        dbDisconnect();
        return result;
    }

    public org.jooq.Record getDbDataRecord(String tableName, Condition condition, int recordNumber) {
        dbConnect(SettingsHelper.getDatabaseName());
        Result<org.jooq.Record> result = globalContext.select().from(tableName).where(condition).fetch();
        dbDisconnect();
        if (result.isNotEmpty()) {
            return result.get(recordNumber);
        } else {
            return null;
        }
    }

    public org.jooq.Record getDbDataRecordFirst(String tableName, Condition condition) {
        dbConnect(SettingsHelper.getDatabaseName());
        Result<org.jooq.Record> result = globalContext.select().from(tableName).where(condition).fetch();
        dbDisconnect();
        if (result.isNotEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public int updateDbRecord(String tableName, String field, String newValue, Condition condition) {
        dbConnect(SettingsHelper.getDatabaseName());
        int rowsAffected = globalContext.update(DSL.table(tableName)).set(DSL.field(field), newValue).where(condition).execute();
        dbDisconnect();
        return rowsAffected;
    }


    //</editor-fold>

}
