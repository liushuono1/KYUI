package Simple;

import java.util.LinkedList;
import java.util.List;

import KYUI.KYUtils;

public class test {
	
	public static void main(String[] args)
	{
		List tl=new LinkedList();  //测试模式
		tl.add("老师1");
		tl.add("老师2");
		tl.add("老师3");
		
		FeedBackObj x =new OptionFeedBack(tl){
			public void dutyFinishing()
			{
				System.err.println("OUTOUTOUT");
				//finishing();//联动反馈面板按钮，
			}
		};
		
		
		System.out.println(KYUtils.ObjectToByte(x));
	}

}
