package Simple;
	import java.io.*;
	public class folderread{
	    public static void main(String[] args) {
	        String fileName="C:\\Users\\SHUO8\\Desktop\\mfocgui_lite\\#5\\新建文件夹\\";
	        File f=new File(fileName);
	        print(f);
	    }
	    public static void print(File f){
	        if(f!=null){
	            if(f.isDirectory()){
	                File[] fileArray=f.listFiles();
	                if(fileArray!=null){
	                    for (int i = 0; i < fileArray.length; i++) {
	                        //递归调用
	                        print(fileArray[i]);
	                    }
	                }
	            }
	            else{
	                System.out.print(f.getName().replace("dumpfile ","").replace(" 1K.dump", "")
	                		.replace("-", "号楼")+'\\'+"n");
	            }
	        }
	    }
	}
