package Simple;

import java.util.LinkedList;
import java.util.List;

import KYUI.KYUtils;

public class test {
	
	public static void main(String[] args)
	{
		List tl=new LinkedList();  //����ģʽ
		tl.add("��ʦ1");
		tl.add("��ʦ2");
		tl.add("��ʦ3");
		
		FeedBackObj x =new OptionFeedBack(tl){
			public void dutyFinishing()
			{
				System.err.println("OUTOUTOUT");
				//finishing();//����������尴ť��
			}
		};
		
		
		System.out.println(KYUtils.ObjectToByte(x));
	}

}
