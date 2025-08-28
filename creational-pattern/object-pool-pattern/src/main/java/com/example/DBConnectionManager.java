package com.example;

import java.util.ArrayList;
import java.util.List;

public class DBConnectionManager {
    private List<DBConnection> freeConnectionPool = new ArrayList<>();
    private List<DBConnection> inUseConnectionPool = new ArrayList<>();

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 6;

    DBConnectionManager() {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            freeConnectionPool.add(new DBConnection());
        }
    }

    public DBConnection getDbConnection() {
        if (freeConnectionPool.isEmpty() && inUseConnectionPool.size() < MAX_POOL_SIZE) {
            freeConnectionPool.add(new DBConnection());
            System.out.println("Creating new connection and putting into free pool, size "
                    + freeConnectionPool.size());
        } else if (freeConnectionPool.isEmpty() && inUseConnectionPool.size() >= MAX_POOL_SIZE) {
            System.err.println("can not create new db connection, max limit is reached");
            return null;
        }
        DBConnection connection = freeConnectionPool.remove(freeConnectionPool.size() - 1);
        inUseConnectionPool.add(connection);
        System.out.println("Romoving connection from free pool, size " + freeConnectionPool.size());
        return connection;
    }

    public void releaseConnection(DBConnection dbConnection) {
        inUseConnectionPool.remove(dbConnection);
        System.out.println("Romoving connection from use pool, size " + inUseConnectionPool.size());
        freeConnectionPool.add(dbConnection);
        System.out.println("Adding db connection back to free pool, size " + freeConnectionPool.size());
    }

}
