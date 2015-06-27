package test;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * IO和NIO文件操作
 * @author wing
 * @date 2012/7/22
 */
public class testIO {
    private static final int count = 400000;
	private static final int bufferSize = 400000;
 
    /**
     * 不使用Buffer包装的输出流写入文件
     */
    public static void writeIO(String fileName){
    	DataOutputStream mDos = null;
    	try {
			mDos = new DataOutputStream(new FileOutputStream(new File(fileName)));
			for(int i = 0; i < count; i++){
				mDos.writeInt(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mDos != null){
			try {
				mDos.close();
			} catch (IOException e) {
				System.out.println("Stream Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 不使用Buffer包装输入流读入文件
     */ 
    public static void readIO(String fileName){
    	DataInputStream mDis = null;
    	try {
			mDis = new DataInputStream(new FileInputStream(new File(fileName)));
			for(int i = 0; i < count; i++){
				mDis.readInt();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mDis != null){
			try {
				mDis.close();
			} catch (IOException e) {
				System.out.println("Stream Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 使用Buffer包装的输出流写入文件
     */
    public static void writeIOWithBuffer(String fileName){
    	DataOutputStream mDos = null;
    	try {
			mDos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fileName))));
			for(int i = 0; i < count; i++){
				mDos.writeInt(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mDos != null){
			try {
				mDos.close();
			} catch (IOException e) {
				System.out.println("Stream Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 使用Buffer包装输入流读入文件
     */ 
    public static void readIOWithBuffer(String fileName){
    	DataInputStream mDis = null;
    	try {
			mDis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(fileName))));
			for(int i = 0; i < count; i++){
				mDis.readInt();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mDis != null){
			try {
				mDis.close();
			} catch (IOException e) {
				System.out.println("Stream Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    
    /**
     * 使用NIO管道来进行数据写入
     */
    public static void writeNIO(String fileName){
    	FileChannel mFc = null;
    	try {
			mFc = new FileOutputStream(new File(fileName)).getChannel();
			IntBuffer mBuffer = IntBuffer.allocate(bufferSize);
			for(int i = 0; i < count; i++){
				mBuffer.put(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		}finally{
		   if(mFc != null){
			try {
				mFc.close();
			} catch (IOException e) {
				System.out.println("Channel Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 使用NIO管道来进行数据读取
     */ 
    public static void readNIO(String fileName){
    	FileChannel mFc = null;
    	try {
    		mFc = new FileInputStream(new File(fileName)).getChannel();
    		IntBuffer mBuffer = IntBuffer.allocate(bufferSize);
			for(int i = 0; i < count; i++){
				mBuffer.get();
			}		
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		}finally{
		   if(mFc != null){
			try {
				mFc.close();
			} catch (IOException e) {
				System.out.println("Channel Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 使用NIO管道来进行数据写入
     */
    public static void writeNIOWithRan(String fileName){
    	FileChannel mFc = null;
    	try {
			mFc = new RandomAccessFile(fileName, "rw").getChannel();
			IntBuffer mBuffer = mFc.map(MapMode.READ_WRITE, 0, 4 * bufferSize).asIntBuffer();
			for(int i = 0; i < count; i++){
				mBuffer.put(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mFc != null){
			try {
				mFc.close();
			} catch (IOException e) {
				System.out.println("Channel Close Exception:" + e.toString());
			}
		   }
		}
    }
    
    /**
     * 使用NIO管道来进行数据读取
     */ 
    public static void readNIOWithRan(String fileName){
    	FileChannel mFc = null;
    	try {
			mFc = new RandomAccessFile(fileName, "rw").getChannel();
			IntBuffer mBuffer = mFc.map(MapMode.READ_WRITE, 0, 4 * bufferSize).asIntBuffer();
			for(int i = 0; i < count; i++){
				mBuffer.get();
			}		
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found:" + e.toString());
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.toString());
		}finally{
		   if(mFc != null){
			try {
				mFc.close();
			} catch (IOException e) {
				System.out.println("Channel Close Exception:" + e.toString());
			}
		   }
		}
    }  
}