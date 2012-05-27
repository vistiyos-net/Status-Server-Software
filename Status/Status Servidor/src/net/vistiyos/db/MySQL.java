package net.vistiyos.db;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.vistiyos.config.Configuration;
import net.vistiyos.util.Log;

public class MySQL {
	
	private static Connection conexion = null;
	
	private static void getConexion(){
		if(conexion == null){
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conexion = (Connection) DriverManager.getConnection("jdbc:mysql://"+Configuration.getValue("server")+"/"+Configuration.getValue("database"), Configuration.getValue("user") , Configuration.getValue("password"));
			} catch (Exception e){
				Log.addLog(e.getMessage());
			}
		}
	}
	
	public static ArrayList<Entry<Integer,Float>> getCantidades(){
		getConexion();
		ArrayList<Entry<Integer,Float>> resultados = new ArrayList<Entry<Integer,Float>>();
		try{
			String SQL = "SELECT * FROM capacidad_productos";
			ResultSet rows = conexion.createStatement().executeQuery(SQL);
			while(rows.next()){
				Entry<Integer,Float> entry = new SimpleEntry<Integer, Float>(rows.getInt("idcapacidad"),rows.getFloat("capacidad"));
				resultados.add(entry);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return resultados;
	}
	
	public static String existeArticulo(String ean13){
		getConexion();
		String resultados = "";
		try{
			String SQL = "SELECT nombre,capacidad FROM capacidad_productos,productos WHERE productos.idcapacidad = capacidad_productos.idcapacidad AND productos.ean13 = '"+ean13+"'";
			ResultSet rows = conexion.createStatement().executeQuery(SQL);
			while(rows.next()){
				resultados+=rows.getString("nombre")+" - "+rows.getFloat("capacidad");
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return resultados;
	}
	
	public static boolean introducirArticulo(String nombre,String ean13,int idcapacidad){
		getConexion();
		try{
			String sql = "INSERT INTO productos (nombre,ean13,idcapacidad) VALUES('"+nombre+"','"+ean13+"',"+idcapacidad+")";
			conexion.createStatement().execute(sql);
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static Map<String,String> getDatosArticulo(String ean13) throws SQLException{
		getConexion();
		Map<String,String> datos = new HashMap<String,String>();
		try{
			ArrayList<String> columnas = new ArrayList<String>();
			String sql = "SELECT nombre,idcapacidad FROM productos WHERE ean13 = '"+ean13+"'";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			ResultSetMetaData rsmd = resultado.getMetaData();
			int numColumns = rsmd.getColumnCount();
			for (int i = 1 ; i < numColumns+1 ; i++) {
				columnas.add(rsmd.getColumnName(i));
			}
			if(resultado.next()){
				Iterator<String> it = columnas.iterator();
				while(it.hasNext()){
					String columna = it.next();
					datos.put(columna, resultado.getString(columna));
				}
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}

	public static boolean aumentarStock(String ean13){
		getConexion();
		try{
			String sql = "SELECT count(*) FROM almacen where idproducto in (SELECT idproducto FROM productos where ean13='"+ean13+"')";
			ResultSet rs = conexion.createStatement().executeQuery(sql);
			if(rs.next()){
				if(rs.getInt(1) != 0){
					sql = "UPDATE almacen SET cantidad=cantidad+1 where idproducto in (SELECT idproducto FROM productos where ean13='"+ean13+"')";
				}
				else{
					sql = "SELECT idproducto FROM productos where ean13='"+ean13+"'";
					ResultSet rs2 = conexion.createStatement().executeQuery(sql);
					int id = -1;
					if(rs2.next()){
						id = rs2.getInt(1);
					}
					sql = "INSERT INTO almacen (idproducto,cantidad) VALUES ("+id+",1)";
				}
			}
			conexion.createStatement().execute(sql);
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static ArrayList<Map<String,String>> getProductos(){
		getConexion();
		ArrayList<Map<String,String>> resultado = new ArrayList<Map<String,String>>();
		try{
			String sql = "SELECT * FROM productos,capacidad_productos,almacen where productos.idcapacidad=capacidad_productos.idcapacidad AND productos.idproducto = almacen.idproducto";
			ResultSet result = conexion.createStatement().executeQuery(sql);
			ArrayList<String> columnas = getColumnas(result);
			while(result.next()){
				Map<String,String> datos = new HashMap<String,String>();
				Iterator<String> it = columnas.iterator();
				while(it.hasNext()){
					String columna = it.next();
					datos.put(columna, result.getString(columna));
				}
				resultado.add(datos);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return resultado;
	}
	
	private static ArrayList<String> getColumnas(ResultSet result){
		ArrayList<String> columnas = new ArrayList<String>();
		try{
			ResultSetMetaData metaDatos = result.getMetaData();
			int numeroColumnas = metaDatos.getColumnCount();
			for (int i = 0; i < numeroColumnas; i++){ 
				columnas.add(metaDatos.getColumnLabel(i + 1)); 
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return columnas;
	}
	
	public static boolean actualizarProducto(String ean13, String nombre, String capacidad, String stock){
		String sql = "";
		try{
			sql ="select idcapacidad from capacidad_productos where capacidad='"+capacidad+"'";
			ResultSet rs = conexion.createStatement().executeQuery(sql);
			int idcapacidad = -1;
			if(rs.next()){
				idcapacidad = rs.getInt(1);
			}
			sql = "UPDATE productos set nombre='"+nombre+"',idcapacidad="+idcapacidad+" where ean13='"+ean13+"'";
			conexion.createStatement().execute(sql);
			sql = "SELECT idproducto FROM productos where ean13='"+ean13+"'";
			rs = conexion.createStatement().executeQuery(sql);
			int idproducto = -1; 
			if(rs.next()){
				idproducto = rs.getInt(1);
			}
			sql = "UPDATE almacen set cantidad="+stock+" where idproducto="+idproducto;
			conexion.createStatement().execute(sql);
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static boolean darDeAltaEmpleado(String dni,String nombre,String telefono){
		String sql = "";
		try{
			sql ="INSERT INTO camarero (dni,nombre,telefono) values ('"+dni+"','"+nombre+"','"+telefono+"')";
			conexion.createStatement().execute(sql);
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static ArrayList<Map<String,String>> getEmpleados(){
		getConexion();
		ArrayList<Map<String,String>> resultado = new ArrayList<Map<String,String>>();
		try{
			String sql = "SELECT * FROM camarero";
			ResultSet result = conexion.createStatement().executeQuery(sql);
			ArrayList<String> columnas = getColumnas(result);
			while(result.next()){
				Map<String,String> datos = new HashMap<String,String>();
				Iterator<String> it = columnas.iterator();
				while(it.hasNext()){
					String columna = it.next();
					datos.put(columna, result.getString(columna));
				}
				resultado.add(datos);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return resultado;
	}
	
	public static boolean actualizarEmpleado(String id,String dni,String nombre, String telefono){
		getConexion();
		String sql = "";
		try{
			sql ="UPDATE camarero set dni='"+dni+"',nombre='"+nombre+"',telefono='"+telefono+"' where idcamarero="+id;
			conexion.createStatement().execute(sql);
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static ArrayList<Entry<Integer,String>> getBarras(){
		getConexion();
		ArrayList<Entry<Integer,String>> datos = new ArrayList<Entry<Integer,String>>();
		try{
			String sql = "SELECT * FROM barras order by nombre ASC";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			while(resultado.next()){
				Entry<Integer,String> entry = new SimpleEntry<Integer, String>(resultado.getInt("idbarra"),resultado.getString("nombre"));
				datos.add(entry);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}
	
	public static Map<String,String> getAsignacion(String idcamarero){
		getConexion();
		Map<String,String> datos = new HashMap<String,String>();
		try{
			String sql = "SELECT * FROM asignacion_barras where idcamarero="+idcamarero;
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			ArrayList<String> columnas = MySQL.getColumnas(resultado);
			if(resultado.next()){
				Iterator<String> it = columnas.iterator();
				while(it.hasNext()){
					String columna = it.next();
					datos.put(columna, resultado.getString(columna));
				}
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}
	
	public static boolean insertarAsignacion(String nombre,int encargado,float salario,String observaciones,String barra){
		getConexion();
		try{
			String sql = "SELECT count(*),idcamarero FROM asignacion_barras where idcamarero in (SELECT idcamarero FROM camarero WHERE nombre='"+nombre+"')";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			if(resultado.next()){
				sql = "SELECT * FROM barras WHERE nombre like '"+barra+"'";
				ResultSet resultado2 = conexion.createStatement().executeQuery(sql);
				resultado2.next();
				if(resultado.getInt(1) == 0){
					sql = "SELECT idcamarero FROM camarero WHERE nombre='"+nombre+"'";
					ResultSet resultado3 = conexion.createStatement().executeQuery(sql);
					resultado3.next();
					sql = "INSERT INTO asignacion_barras (idcamarero,encargado,salario,observaciones,idbarra) values ("+resultado3.getInt(1)+","+encargado+","+salario+","+observaciones+","+resultado2.getInt(1)+")";
				}
				else{
					sql = "UPDATE asignacion_barras SET encargado="+encargado+",salario="+salario+",observaciones="+observaciones+",idbarra="+resultado2.getInt(1)+" WHERE idcamarero="+resultado.getInt(1);
				}
				conexion.createStatement().execute(sql);
			}
			return true;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	public static ArrayList<String> getTurnos(){
		getConexion();
		ArrayList<String> datos = new ArrayList<String>();
		try{
			String sql = "SELECT * FROM turnos ORDER BY idturno";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			while(resultado.next()){
				datos.add(resultado.getString(2));
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}

	public static ArrayList<Map<String,String>> getEntradas(String opcion) {
		getConexion();
		ArrayList<Map<String,String>> datos = new ArrayList<Map<String,String>>();
		try{
			String sql = "SELECT * FROM turnos,tipo_bebida,asignacion_entradas_turnos WHERE turnos.idturno = asignacion_entradas_turnos.idturno AND  asignacion_entradas_turnos.idbebida = tipo_bebida.idbebida AND turnos.nombre_turno LIKE '"+opcion+"'";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			ArrayList<String> columnas = MySQL.getColumnas(resultado);
			while(resultado.next()){
				Map<String,String> row = new HashMap<String,String>();
				Iterator<String> it = columnas.iterator();
				while(it.hasNext()){
					String columna = it.next();
					row.put(columna, resultado.getString(columna));
				}
				datos.add(row);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}

	public static ArrayList<String> getBebidas() {
		getConexion();
		ArrayList<String> datos = new ArrayList<String>();
		try{
			String sql = "SELECT * FROM tipo_bebida ORDER BY idbebida ASC";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			while(resultado.next()){
				datos.add(resultado.getString("nombre_bebida"));
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return datos;
	}

	public static void insertarEntrada(String nombre, String bebida,float precio,int num_bebidas,int turno) {
		getConexion();
		try{
			String sql = "SELECT * FROM tipo_bebida WHERE nombre_bebida LIKE '"+bebida+"'";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			if(resultado.next()){
				if(nombre.startsWith("*")){ //Ya existe
					sql = "UPDATE asignacion_entradas_turnos SET idbebida = "+resultado.getString("idbebida")+",precio="+precio+",num_bebidas="+num_bebidas+" WHERE nombre LIKE '"+nombre.substring(1)+"' AND idturno="+turno;					
				}
				else{ //No existe
					sql = "INSERT INTO asignacion_entradas_turnos (nombre,idturno,idbebida,precio,num_bebidas) VALUES ('"+nombre+"',"+turno+","+resultado.getString("idbebida")+","+precio+","+num_bebidas+")";
				}
			System.out.println(sql);
			conexion.createStatement().execute(sql);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
	}

	public static int getIdTurno(String opcion) {
		getConexion();
		try{
			String sql = "SELECT * FROM turnos WHERE nombre_turno LIKE '"+opcion+"'";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			if(resultado.next()){
				return resultado.getInt("idturno");
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return 0;
	}

	
	public static void activarTurno(int idTurno) {
		getConexion();
		try{
			String sql = "UPDATE turnos set activo = 0 WHERE idturno!='"+idTurno+"'";
			conexion.createStatement().execute(sql);
			sql = "UPDATE turnos set activo = 1 WHERE idturno='"+idTurno+"'";
			conexion.createStatement().execute(sql);
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
	}
	
	public static ArrayList<Entry<String,String>> getConfiguracion(){
		ArrayList<Entry<String,String>> resultados = new ArrayList<Entry<String,String>>();
		try{
			String SQL = "SELECT * FROM configuracion WHERE parametro NOT LIKE 'Admin_Password'";
			ResultSet rows = conexion.createStatement().executeQuery(SQL);
			while(rows.next()){
				Entry<String,String> entry = new SimpleEntry<String, String>(rows.getString("parametro"),rows.getString("valor"));
				resultados.add(entry);
			}
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}
		return resultados;
	}

	public static void actualizarConfiguracion(String parametro, String valor) {
		getConexion();
		try{
			String sql = "UPDATE configuracion SET valor='"+valor+"' WHERE parametro='"+parametro+"'";
			conexion.createStatement().execute(sql);
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
		}		
	}
	
	public static boolean isClave(String clave){
		getConexion();
		try{
			String sql = "SELECT valor FROM configuracion WHERE parametro LIKE 'Admin_Password'";
			ResultSet resultado = conexion.createStatement().executeQuery(sql);
			if(resultado.next()){
				if(resultado.getString(1).equals(getMD5(clave))){
					return true;
				}
				return false;
			}
			return false;
		}catch(SQLException ex){
			Log.addLog(ex.getMessage());
			return false;
		}
	}
	
	private static String getMD5(String clave){
		try{
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(clave.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while(hashtext.length() < 32 ){
				hashtext = "0"+hashtext;
			}
			return hashtext;
		}catch(NoSuchAlgorithmException ex){
			Log.addLog("No se puede utilizar MD5");
			return "";
		}
	}

}
