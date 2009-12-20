package de4m;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

class db{
	
Connection conn;
Statement st;
ResultSet rsimport;
ArrayList<String> importmeALentry = new ArrayList<String>();
String status = "";
String thecolor = "";
int theentry;
Object importentry;
boolean allclear = true;
	
public void dbConnect(String host, String port, String user, String pass){
    try{Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/mangos?user="+user+"&password="+pass);
        st = conn.createStatement();
        status = "Connection successfully established!";
        thecolor = "22AA11";}
	catch (Exception e){status = "Connection failed:\n" + e; thecolor = "BB0000";}}

public Object[] dbLookup(String importme){
    try{rsimport = st.executeQuery("SELECT entry, name FROM item_template WHERE name LIKE '%"+importme+"%'");
    	ArrayList<String> importmeAL = new ArrayList<String>();
    	importmeALentry.clear();
		while (rsimport.next()){importmeALentry.add(rsimport.getString("entry"));
			importmeAL.add(rsimport.getString("entry") + " - " + rsimport.getString("name"));}
		Object[] importmeAR = new Object[importmeAL.size()];
		for (int i = 0 ; i < importmeAL.size() ; i++){importmeAR[i] = importmeAL.get(i);}
		return importmeAR;}
	catch (Exception exc){status = "Import failed:\n"+exc; return null;}}

public void setimportentry(int yup){importentry=importmeALentry.get(yup);
	try{rsimport = st.executeQuery("SELECT * FROM item_template WHERE entry="+importentry); rsimport.next();}
	catch (Exception e){status = "Import failed:\n"+e;}
}

public String dbImport(String dbfield){
	try{return rsimport.getString(dbfield);}
	catch(Exception e){return "error";}
}

public int dbInt(String dbfield){
	try{return rsimport.getInt(dbfield);}
	catch(Exception e){return 0;}
}

public void exportprep(int entry){
	try{theentry = entry;
    st.executeUpdate("DROP TEMPORARY TABLE IF EXISTS temp_ic_item_template");
    st.executeUpdate("CREATE TEMPORARY TABLE temp_ic_item_template LIKE item_template");
    st.executeUpdate("INSERT INTO temp_ic_item_template SELECT * FROM item_template WHERE ENTRY="+importentry.toString());
    st.executeUpdate("UPDATE temp_ic_item_template SET "+"ENTRY="+entry);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport(String field, String data){
	try{st.executeUpdate("UPDATE temp_ic_item_template SET "+field+"="+data);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport(String field, int data){
	try{st.executeUpdate("UPDATE temp_ic_item_template SET "+field+"="+data);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport2(String field, String data){
	try{st.executeUpdate("UPDATE temp_ic_item_template SET "+field+"='"+data+"'");}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void exportfinalize(String name, String entry){
	try{st.executeUpdate("INSERT INTO item_template SELECT * FROM temp_ic_item_template WHERE ENTRY",theentry);
    st.executeUpdate("DROP TEMPORARY TABLE temp_ic_item_template");
    status = name + " successfully injected to database with ID " + entry;}
    catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public boolean check(){return allclear;}
public String getstatus(){return status;}
public String getcolor(){return thecolor;}
	
}