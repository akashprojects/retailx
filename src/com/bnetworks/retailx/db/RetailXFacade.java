package com.bnetworks.retailx.db;

import com.javaranch.common.Str;
import com.javaranch.db.* ;

import java.io.StringBufferInputStream;
import java.sql.* ;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map ;

public class RetailXFacade
{

    private static Implementation instance = new Implementation();

    public static class Implementation extends DBFacade
    {

        Implementation()
        {
            super("org.postgresql.Driver" , "jdbc:postgresql://localhost:5432/RetailX" , "postgres" , "123");
        }
        public long insertAndGetID( String table , Map data , String idColumnName ) throws SQLException
        {
            long id = 0 ;
            data.remove( idColumnName );
            data.put( '+' + idColumnName , "nextval( '" + table + "_seq')" );
            String s = insertAndPossiblyGetNewID( table , data , idColumnName );
            id = Str.toLong(s);
            return id ;
        }

        private String insertAndPossiblyGetNewID( String table , Map data , String idColumnName ) throws SQLException
        {
            String returnVal = null ;
            try
            {
                Connection con = getConnection();
                try
                {
                    returnVal = insertAndPossiblyGetNewID( con , table , data , idColumnName );
                }
                finally
                {
                    con.close();
                }
            }
            catch ( SQLException e )
            {
                throw e ;  // this is to avoid the exception message below
            }
            catch( Exception e )
            {
                throw new SQLException( "insert() connection fail: " + e );
            }
            return returnVal ;
        }

    }

    public static Implementation getInstance()
    {
        instance.doNotUseHighLowPattern();
        return instance ;
    }

    /** Force all methods that use this class to access the database a different way.
     *
     * If you want to do unit testing on classes that use this class, you can pass in a MockDBFacade.
     *
     * If you want to do functional (integration) testing with an active database and access it a different
     * way than this class is set up, you can pass in an instance of DBFacade that uses that way.
     *
     * Suppose these classes are all generated for use within EJB, but you want to write a utility that uses these
     * same classes outside of the EJB container, you can pass in a different DBFacade.
     */
    public static void setInstance( Implementation facade )
    {
        instance = facade ;
    }

    private RetailXFacade()
    {
    }

    /** Get a database connection.
     *
     * Gets a connection from the same place all of the methods here get a connection if
     * you don't provide one.  Of course, if you open it, you gotta close it!
     */
    public static Connection getConnection() throws Exception
    {
        return instance.getConnection();
    }

    /** Establish a connection to the database to see if any exceptions might be generated.
     *
     * Call this method in your startup code to see if everything is configured and working correctly.
     *
     * @throws Exception The types of exceptions thrown here can change depending the type of database access.
     */
    public static void testConnection() throws Exception
    {
        instance.testConnection();
    }

    /** Sometimes you just gotta do it in SQL.
     *
     * This method will at least handle the statements and result sets for you.
     *
     * @param sql A complete SQL statement
     */
    public static DBResults executeQuery( Connection con , String sql ) throws SQLException
    {
        return instance.executeQuery( con , sql );
    }

    /** Sometimes you just gotta do it in SQL.
     *
     * This method will at least handle the statements and result sets for you.
     *
     * @param sql A complete SQL statement
     */
    public static DBResults executeQuery( String sql ) throws SQLException
    {
        return instance.executeQuery( sql );
    }

    /** Sometimes you just gotta do it in SQL.
     *
     * This method will at least handle the statements for you.
     *
     * @param sql A complete SQL statement
     */
    public static void executeUpdate( Connection con , String sql ) throws SQLException
    {
        instance.executeUpdate( con , sql );
    }

    /** Sometimes you just gotta do it in SQL.
     *
     * This method will at least handle the statements for you.
     *
     * @param sql A complete SQL statement
     */
    public static void executeUpdate( String sql ) throws SQLException
    {
        instance.executeUpdate( sql );
    }

    /** Use the DBQuery object to do a search.
     *
     *   @param con A database connection. <p>
     *   @param q A predescribed query - see DBQuery for details. <p>
     *   @return contains zero objects if none are found.
     */
    public static DBResults search( Connection con , DBQuery q ) throws SQLException
    {
        return instance.search( con , q );
    }

    /** Find all the data in a table that matches up to this SQL where clause.
     *
     *  NOTE!  Use this method only if there is no alternative!  The purpose of these classes
     *  is to provide a non-SQL facade to the relational database because some databases
     *  are different than others.  If your whereClause SQL fragment uses any SQL that is
     *  proprietary to the database you are currently using, there is a good chance that it
     *  will not work when the software is ported! <p>
     *
     *  @param whereClause The SQL to use in selecting the rows - do not say "WHERE". <p>
     *  @param dataColumns The column names that have the data you want. <p>
     *  @return contains zero objects if none are found.
     */
    public static DBResults search( Connection con , String table , String whereClause , String[] dataColumns ) throws SQLException
    {
        return instance.search( con , table , whereClause , dataColumns );
    }

    /** Find all the data in a table that matches up to this SQL where clause.
     *
     *  NOTE!  Use this method only if there is no alternative!  The purpose of these classes
     *  is to provide a non-SQL facade to the relational database because some databases
     *  are different than others.  If your whereClause SQL fragment uses any SQL that is
     *  proprietary to the database you are currently using, there is a good chance that it
     *  will not work when the software is ported! <p>
     *
     *  @param whereClause The SQL to use in selecting the rows - do not say "WHERE". <p>
     *  @param dataColumns The column names that have the data you want. <p>
     *  @return contains zero objects if none are found.
     */
    public static DBResults search( String table , String whereClause , String[] dataColumns ) throws SQLException
    {
        return instance.search( table , whereClause , dataColumns );
    }

    /** Find all the data in a table where the text in a column exactly matches a string. <p>
     *
     * @param dataColumns The searchColumn names that have the data you want. <p>
     * @return contains zero objects if none are found.
     */
    public static DBResults search( Connection con , String table , String searchColumn , String searchText , String[] dataColumns ) throws SQLException
    {
        return instance.search( con , table , searchColumn , searchText , dataColumns );
    }

    /** Find all the data in a table where the text in a column exactly matches one of these strings. <p>
     *
     * @param dataColumns The searchColumn names that have the data you want. <p>
     * @return contains zero objects if none are found.
     */
    public static DBResults search( Connection con , String table , String searchColumn , String[] searchText , String[] dataColumns ) throws SQLException
    {
        return instance.search( con , table , searchColumn , searchText , dataColumns );
    }

    /** Use the DBQuery object to do a search.
     *
     * @return contains zero objects if none are found.
     */
    public static DBResults search( DBQuery q ) throws SQLException
    {
        return instance.search( q );
    }

    /** Find all the data in a table where the text in a column exactly matches a string.
     *
     * @param dataColumns The searchColumn names that have the data you want. <p>
     * @return contains zero objects if none are found.
     */
    public static DBResults search( String table , String searchColumn , String searchText , String[] dataColumns ) throws SQLException
    {
        return instance.search( table , searchColumn , searchText , dataColumns );
    }

    /** Find all the data in a table where the text in a column exactly matches one of these strings.
     *
     * @param dataColumns The searchColumn names that have the data you want. <p>
     * @return contains zero objects if none are found.
     */
    public static DBResults search( String table , String searchColumn , String searchText[] , String[] dataColumns ) throws SQLException
    {
        return instance.search( table , searchColumn , searchText , dataColumns );
    }

    /** Update all rows where the searchText matches the text found in column.
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( String table , String searchColumn , String searchText , Map data ) throws SQLException
    {
        instance.update( table , searchColumn , searchText , data );
    }

    /** Update all rows where the searchText matches the text found in column.
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( Connection con , String table , String column , String searchText , Map data ) throws SQLException
    {
        instance.update( con , table , column , searchText , data );
    }

    /** Update all rows where the text in a searchColumn exactly matches one of these strings. <p>
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( String table , String searchColumn , String[] searchText , Map data ) throws SQLException
    {
        instance.update( table , searchColumn , searchText , data );
    }

    /** Update all rows where the text in a searchColumn exactly matches one of these strings. <p>
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( Connection con , String table , String searchColumn , String[] searchText , Map data ) throws SQLException
    {
        instance.update( con , table , searchColumn , searchText , data );
    }

    /** Update all rows matching this where clause.
     *
     *  NOTE!  Use this method only if there is no alternative!  The purpose of these classes
     *  is to provide a non-SQL facade to the relational database because some databases
     *  are different than others.  If your whereClause SQL fragment uses any SQL that is
     *  proprietary to the database you are currently using, there is a good chance that it
     *  will not work when the software is ported! <p>
     *
     * @param whereClause The SQL to use in selecting the rows - do not say "WHERE".
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( String table , String whereClause , Map data ) throws SQLException
    {
        instance.update( table , whereClause , data );
    }

    /** Update all rows matching this where clause.
     *
     *  NOTE!  Use this method only if there is no alternative!  The purpose of these classes
     *  is to provide a non-SQL facade to the relational database because some databases
     *  are different than others.  If your whereClause SQL fragment uses any SQL that is
     *  proprietary to the database you are currently using, there is a good chance that it
     *  will not work when the software is ported! <p>
     *
     * @param whereClause The SQL to use in selecting the rows - do not say "WHERE".
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void update( Connection con , String table , String whereClause , Map data ) throws SQLException
    {
        instance.update( con , table , whereClause , data );
    }

    /** Delete all rows where the searchText matches the text found in column.
     */
    public static void delete( Connection con , String table , String searchColumn , String searchText ) throws SQLException
    {
        instance.delete( con , table , searchColumn , searchText );
    }

    /** Delete all rows where the searchText matches the text found in column.
     */
    public static void delete( String table , String searchColumn , String searchText ) throws SQLException
    {
        instance.delete( table , searchColumn , searchText );
    }

    /** Delete all rows metching the query.
     */
    public static void delete( Connection con , DBQuery q ) throws SQLException
    {
        instance.delete( con , q );
    }

    /** Delete all rows metching the query.
     */
    public static void delete( DBQuery q ) throws SQLException
    {
        instance.delete( q );
    }

    /** Create a new row in the table and populate the fields with the provided data.
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void insert( Connection con , String table , Map data ) throws SQLException
    {
        instance.insert( con , table , data );
    }

    /** Create a new row in the table and populate the fields with the provided data.
     *
     * @param data A key-value collection of searchColumn names (key) and data (value). If a searchColumn name starts with ">", that searchColumn will be processed as a stream (good for big Strings). <p>
     */
    public static void insert( String table , Map data ) throws SQLException
    {
        instance.insert( table , data );
    }

    public static long insertAndGetID( String table , Map data ) throws SQLException
    {
        return instance.insertAndGetID( table , data );
    }

    public static long insertAndGetID( Connection con , String table , Map data ) throws SQLException
    {
        return instance.insertAndGetID( con , table , data );
    }

    public static long insertAndGetID( String table , Map data , String idColumnName ) throws SQLException
    {
        return instance.insertAndGetID( table , data , idColumnName );
    }

    public static long insertAndGetID( Connection con , String table , Map data , String idColumnName ) throws SQLException
    {
        return instance.insertAndGetID( con , table , data , idColumnName );
    }

}
