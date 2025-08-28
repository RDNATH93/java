package com.example;

/**
 * Hello world!
 *
 */
public class Client {
    public static void main( String[] args ){
        DBConnectionManager connectionManager = new DBConnectionManager();
        DBConnection dbConnection1 = connectionManager.getDbConnection();
        DBConnection dbConnection2 = connectionManager.getDbConnection();
        DBConnection dbConnection3 = connectionManager.getDbConnection();
        DBConnection dbConnection4 = connectionManager.getDbConnection();
        DBConnection dbConnection5 = connectionManager.getDbConnection();
        DBConnection dbConnection6 = connectionManager.getDbConnection();

        connectionManager.getDbConnection();
        connectionManager.releaseConnection(dbConnection6);

    }
}
