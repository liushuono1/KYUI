package Simple;
	import java.io.*;
	public class folderread{
	    public static void main(String[] args) {
	        String fileName="C:\\Users\\SHUO8\\Desktop\\mfocgui_lite\\#5\\�½��ļ���\\";
	        File f=new File(fileName);
	        print(f);
	    }
	    public static void print(File f){
	        if(f!=null){
	            if(f.isDirectory()){
	                File[] fileArray=f.listFiles();
	                if(fileArray!=null){
	                    for (int i = 0; i < fileArray.length; i++) {
	                        //�ݹ����
	                        print(fileArray[i]);
	                    }
	                }
	            }
	            else{
	                System.out.print(f.getName().replace("dumpfile ","").replace(" 1K.dump", "")
	                		.replace("-", "��¥")+'\\'+"n");
	            }
	        }
	    }
	}
