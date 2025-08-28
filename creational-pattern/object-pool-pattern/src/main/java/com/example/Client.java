package com.example;

/**
 * The Object pool design pattern is used with Singleton Design Pattern and required thread safety
 * while acquiring and releasing the resource
 * 
 * Advantages:
 * Reduce the overhead of creating and destroying objects(generally resource intensive objects)
 * Reduce the latency, as it uses pre initialized objects
 * Prevent Resource exhaustion by managing the number of resource intensive objects creation
 * 
 * Disadvantages:
 * Resource leakage can happen if object not handeled properly and not being returned to the pool
 * Required more memory because of managing the pool
 * Pool management requires thread safety, which is addinonal overhead
 * Adds application complexity because of pool management
 * 
 */
public class Client {
    public static void main( String[] args ){
        DBConnectionManager connectionManager = DBConnectionManager.getInstance();

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
