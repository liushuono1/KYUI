package Simple;

/** 
* Title: BlobPros.java 
* Project: test 
* Description: 把图片存入mysql中的blob字段，并取出 
* Call Module: mtools数据库中的tmp表 
* File: C:downloadsluozsh.jpg 
* Copyright: Copyright (c) 2003-2003 
* Company: uniware 
* Create Date: 2002.12.5 
* @Author: ChenQH 
* @version 1.0 版本* 
* 
* Revision history 
* Name Date Description 
* ---- ---- ----------- 
* Chenqh 2003.12.5 对图片进行存取 
* 
* note: 要把数据库中的Blob字段设为longblob 
* 
*/

//package com.uniware; 

import java.io.*;
import java.sql.*;

public class AttachUtil {
	private static final String URL = "jdbc:mysql://10.144.123.63:3306/mtools?user=wind&password=123&useUnicode=true";
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private File file = null;

	public AttachUtil() {
	}

	/**
	 * 向数据库中插入一个新的BLOB对象(图片)
	 * 
	 * @param infile
	 *            要输入的数据文件
	 * @throws java.lang.Exception
	 */
	public void blobInsert(String infile) throws Exception {
		FileInputStream fis = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			conn = DriverManager.getConnection(URL);

			file = new File(infile);
			fis = new FileInputStream(file);
			// InputStream fis = new FileInputStream(infile);
			pstmt = conn.prepareStatement("insert into tmp(descs,pic) values(?,?)");
			pstmt.setString(1, file.getName()); // 把传过来的第一个参数设为文件名
			// pstmt.setBinaryStream(2,fis,(int)file.length());
			// //这种方法原理上会丢数据，因为file.length()返回的是long型
			pstmt.setBinaryStream(2, fis, fis.available()); // 第二个参数为文件的内容
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("[blobInsert error : ]" + ex.toString());
		} finally {
			// 关闭所打开的对像//
			pstmt.close();
			fis.close();
			conn.close();
		}
	}

	/**
	 * 从数据库中读出BLOB对象
	 * 
	 * @param outfile
	 *            输出的数据文件
	 * @param picID
	 *            要取的图片在数据库中的ID
	 * @throws java.lang.Exception
	 */

	public void blobRead(String outfile, int picID) throws Exception {
		FileOutputStream fos = null;
		InputStream is = null;
		byte[] Buffer = new byte[4096];

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			conn = DriverManager.getConnection(URL);
			pstmt = conn.prepareStatement("select pic from tmp where id=?");
			pstmt.setInt(1, picID); // 传入要取的图片的ID
			rs = pstmt.executeQuery();
			rs.next();

			file = new File(outfile);
			if (!file.exists()) {
				file.createNewFile(); // 如果文件不存在，则创建
			}
			fos = new FileOutputStream(file);
			is = rs.getBinaryStream("pic");
			int size = 0;
			/*
			 * while(size != -1) { size = is.read(Buffer); //从数据库中一段一段的读出数据
			 * //System.out.println(size); if(size != -1) //-1表示读到了文件末
			 * fos.write(Buffer,0,size); }
			 */
			while ((size = is.read(Buffer)) != -1) {
				// System.out.println(size);
				fos.write(Buffer, 0, size);
			}

		} catch (Exception e) {
			System.out.println("[OutPutFile error : ]" + e.getMessage());
		} finally {
			// 关闭用到的资源
			fos.close();
			rs.close();
			pstmt.close();
			conn.close();
		}
	}

	public static void main(String[] args) {
		try {

			AttachUtil blob = new AttachUtil();
			// blob.blobInsert("C:Downloadsluozsh1.jpg");
			blob.blobRead("c:/downloads/1.jpg", 47);
		} catch (Exception e) {
			System.out.println("[Main func error: ]" + e.getMessage());
		}
	}
}