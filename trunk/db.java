package de4m;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;

class db{
	
Connection conn;
Statement st;
ResultSet rsimport;
ArrayList<String> importmeALentry = new ArrayList<String>();
ArrayList<String> taybulls = new ArrayList<String>();
String status = "";
String thecolor = "";
String thetable = "";
int theentry;
Object importentry;
boolean allclear = true;
	
public void dbConnect(String host, String port, String user, String pass, String base){
    try{Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+base+"?user="+user+"&password="+pass);
        st = conn.createStatement();
        status = "Connection successfully established!";
        thecolor = "22AA11";}
	catch (Exception e){status = "Connection failed:\n" + e; thecolor = "BB0000";}}

public String[] dbTables(){
	try{DatabaseMetaData dmd = conn.getMetaData();
    ResultSet rs1 = dmd.getTables(null,null,"%",null);
    taybulls.clear();
    while (rs1.next()) {taybulls.add(rs1.getString(3));}
    String tables[] = new String[taybulls.size()];
    for (int i = 0 ; i < taybulls.size() ; i++){
    	tables[i] = taybulls.get(i);}
	return tables;}
	catch (Exception e){status = "Table listing failed:\n"+e; return null;}
}

public void setTable(int selection){
	thetable=taybulls.get(selection);
}

public Object[] dbLookup(String importme){
    try{rsimport = st.executeQuery("SELECT entry, name FROM "+thetable+" WHERE name LIKE '%"+importme+"%'");
    	ArrayList<String> importmeAL = new ArrayList<String>();
    	importmeALentry.clear();
		while (rsimport.next()){importmeALentry.add(rsimport.getString("entry"));
			importmeAL.add(rsimport.getString("entry") + " - " + rsimport.getString("name"));}
		Object[] importmeAR = new Object[importmeAL.size()];
		for (int i = 0 ; i < importmeAL.size() ; i++){importmeAR[i] = importmeAL.get(i);}
		return importmeAR;}
	catch (Exception exc){status = "Import failed:\n"+exc; return null;}}

public void setimportentry(int yup){importentry=importmeALentry.get(yup);
	try{rsimport = st.executeQuery("SELECT * FROM "+thetable+" WHERE entry="+importentry); rsimport.next();}
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
    st.executeUpdate("DROP TEMPORARY TABLE IF EXISTS temp_deform_"+thetable+"");
    st.executeUpdate("CREATE TEMPORARY TABLE temp_deform_"+thetable+" LIKE "+thetable+"");
    st.executeUpdate("INSERT INTO temp_deform_"+thetable+" SELECT * FROM "+thetable+" WHERE ENTRY="+importentry.toString());
    st.executeUpdate("UPDATE temp_deform_"+thetable+" SET ENTRY="+entry);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport(String field, String data){
	String apost = "";
	if (field == "name" | field == "description"){apost = "'";}
	try{st.executeUpdate("UPDATE temp_deform_"+thetable+" SET "+field+"="+apost+data+apost);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport(String field, int data){
	try{st.executeUpdate("UPDATE temp_deform_"+thetable+" SET "+field+"="+data);}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void dbExport2(String field, String data){
	try{st.executeUpdate("UPDATE temp_deform_"+thetable+" SET "+field+"='"+data+"'");}
	catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void exportfinalize(String name, String entry){
	try{st.executeUpdate("INSERT INTO "+thetable+" SELECT * FROM temp_deform_"+thetable+" WHERE ENTRY="+theentry);
    st.executeUpdate("DROP TEMPORARY TABLE temp_deform_"+thetable+"");
    status = name + " successfully injected to database with ID " + entry;}
    catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public void exportfile (String name, String entry, String fileloc){
	try{FileWriter writeout = new FileWriter(fileloc);
		BufferedWriter out = new BufferedWriter(writeout);
        ResultSet exportset = st.executeQuery("SELECT * FROM temp_deform_"+thetable+"");
        exportset.next();
        out.write("insert into "+thetable+" VALUES (");
        for (int i = 1 ; i <= exportset.getMetaData().getColumnCount() ; i++){
        out.write("'"+exportset.getString(i));
        if(i<exportset.getMetaData().getColumnCount()){out.write("', ");}}
        out.write("');");
        out.close();
        status = name + " exported to file:\n" + fileloc + "\nWith ID " + entry;
	}catch(Exception e){status = "Export failed:\n"+e;allclear=false;}
}

public boolean check(){return allclear;}
public String getstatus(){return status;}
public String getcolor(){return thecolor;}

}