package Simple;

/** 
* Title: BlobPros.java 
* Project: test 
* Description: ��ͼƬ����mysql�е�blob�ֶΣ���ȡ�� 
* Call Module: mtools���ݿ��е�tmp�� 
* File: C:downloadsluozsh.jpg 
* Copyright: Copyright (c) 2003-2003 
* Company: uniware 
* Create Date: 2002.12.5 
* @Author: ChenQH 
* @version 1.0 �汾* 
* 
* Revision history 
* Name Date Description 
* ---- ---- ----------- 
* Chenqh 2003.12.5 ��ͼƬ���д�ȡ 
* 
* note: Ҫ�����ݿ��е�Blob�ֶ���Ϊlongblob 
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
	 * �����ݿ��в���һ���µ�BLOB����(ͼƬ)
	 * 
	 * @param infile
	 *            Ҫ����������ļ�
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
			pstmt.setString(1, file.getName()); // �Ѵ������ĵ�һ��������Ϊ�ļ���
			// pstmt.setBinaryStream(2,fis,(int)file.length());
			// //���ַ���ԭ���ϻᶪ���ݣ���Ϊfile.length()���ص���long��
			pstmt.setBinaryStream(2, fis, fis.available()); // �ڶ�������Ϊ�ļ�������
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("[blobInsert error : ]" + ex.toString());
		} finally {
			// �ر����򿪵Ķ���//
			pstmt.close();
			fis.close();
			conn.close();
		}
	}

	/**
	 * �����ݿ��ж���BLOB����
	 * 
	 * @param outfile
	 *            ����������ļ�
	 * @param picID
	 *            Ҫȡ��ͼƬ�����ݿ��е�ID
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
			pstmt.setInt(1, picID); // ����Ҫȡ��ͼƬ��ID
			rs = pstmt.executeQuery();
			rs.next();

			file = new File(outfile);
			if (!file.exists()) {
				file.createNewFile(); // ����ļ������ڣ��򴴽�
			}
			fos = new FileOutputStream(file);
			is = rs.getBinaryStream("pic");
			int size = 0;
			/*
			 * while(size != -1) { size = is.read(Buffer); //�����ݿ���һ��һ�εĶ�������
			 * //System.out.println(size); if(size != -1) //-1��ʾ�������ļ�ĩ
			 * fos.write(Buffer,0,size); }
			 */
			while ((size = is.read(Buffer)) != -1) {
				// System.out.println(size);
				fos.write(Buffer, 0, size);
			}

		} catch (Exception e) {
			System.out.println("[OutPutFile error : ]" + e.getMessage());
		} finally {
			// �ر��õ�����Դ
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