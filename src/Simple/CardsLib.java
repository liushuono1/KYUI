package Simple;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import AuthModule.CardUtils;

public class CardsLib {
	
	static CardsLib cb=null;
    String cards="04c61fa3 (2╨еб╔20)\n04dbddc0 (2╨еб╔16)\n04fa28a3 (2╨еб╔4)\n0721713a (2╨еб╔33)\n0763703a (2╨еб╔17)\n14e01fa3 (2╨еб╔37)\n14e021a3 (2╨еб╔14)\n14ecddc0 (2╨еб╔17)\n1762703a (2╨еб╔49)\n24061ea3 (3╨еб╔106)\n249439a3 (2╨еб╔28)\n24a832a3 (2╨еб╔42)\n34a725a3 (2╨еб╔22)\n34d122a3 (2╨еб╔19)\n34f41da3 (2╨еб╔26)\n3788723a (2╨еб╔40)\n37b56d3a (2╨еб╔9)\n44932ba3 (2╨еб╔6)\n449e21a3 (2╨еб╔30)\n44a632a3 (2╨еб╔24)\n44bf37a3 (2╨еб╔32)\n54602fa3 (2╨еб╔31)\n549224a3 (2╨еб╔44)\n57446e3a (2╨еб╔5)\n748f2ca3 (2╨еб╔43)\n77456e3a (2╨еб╔10)\n77e5713a (2╨еб╔39)\n840f39a3 (2╨еб╔48)\n8442d7c0 (2╨еб╔50)\n9761723a (2╨еб╔25)\n97cf693a (2╨еб╔29)\na4431ea3 (2╨еб╔7)\na4a31da3 (2╨еб╔46)\na4a71da3 (2╨еб╔11)\na710723a (2╨еб╔8)\nb45336a3 (2╨еб╔36)\nb784713a (2╨еб╔23)\nc4841ea3 (2╨еб╔1)\nc490d5c0 (2╨еб╔╨еб╔47)\nc4a52ba3 (2╨еб╔41)\nc4ad23a3 (2╨еб╔45)\nc4ce37a3 (2╨еб╔2)\nc776703a (2╨еб╔27)\ne4002aa3 (2╨еб╔34)\ne4951da3 (2╨еб╔35)\ne4a21da3 (2╨еб╔12)\ne4f428a3 (2╨еб╔21)\ne74e6c3a (2╨еб╔38)\ne7d1703a (2╨еб╔3)\nf41c37a3 (2╨еб╔15)\nf4b123a3 (2╨еб╔13)\n"+"04012aa3 (3╨еб╔8)\n0760723a (3╨еб╔12)\n07bb6d3a (3╨еб╔18)\n07e4713a (3╨еб╔22)\n14072fa3 (3╨еб╔36)\n141edfc0 (3╨еб╔86)\n143c1da3 (3╨еб╔87)\n24061ea3 (3╨еб╔106)\n2426dfc0 (3╨еб╔79)\n243039a3 (3╨еб╔84)\n243534a3 (3╨еб╔10)\n243639a3 (3╨еб╔27)\n2494d5c0 (3╨еб╔104)\n24c633a3 (3╨еб╔60)\n24fe1ea3 (3╨еб╔21)\n278c693a (3╨еб╔58)\n27cb663a (3╨еб╔62)\n27eb713a (3╨еб╔93)\n27fd703a (3╨еб╔91)\n3766713a (3╨еб╔7)\n3767723a (3╨еб╔101)\n377b693a (3╨еб╔33)\n3787723a (3╨еб╔100)\n37a4723a (3╨еб╔28)\n448323a3 (3╨еб╔15)\n44b123a3 (3╨еб╔69)\n44e6d5c0 (3╨еб╔19)\n4751713a (3╨еб╔77)\n47ef703a (3╨еб╔34)\n47fa6f3a (3╨еб╔113)\n543339a3 (3╨еб╔3)\n54881ea3 (3╨еб╔90)\n549932a3 (3╨еб╔66)\n549e21a3 (3╨еб╔89)\n549f21a3 (3╨еб╔5)\n54a321a3 (3╨еб╔37)\n54ac23a3 (3╨еб╔6)\n54c824a3 (3╨еб╔9)\n54d3ecc0 (3╨еб╔44)\n6402dec0 (3╨еб╔73)\n648d39a3 (3╨еб╔105)\n64a725a3 (3╨еб╔68)\n6711713a (3╨еб╔55)\n67396b3a (3╨еб╔29)\n748dd5c0 (3╨еб╔78)\n748ed5c0 (3╨еб╔43)\n74ee1da3 (3╨еб╔24)\n77696b3a (3╨еб╔76)\n77f36f3a (3╨еб╔81)\n843439a3 (3╨еб╔82)\n84501da3 (3╨еб╔111)\n84d21ea3 (3╨еб╔65)\n84dff0c0 (3╨еб╔83)\n85f8eb00 (3╨еб╔99)\n8785703a (3╨еб╔85)\n87ae6d3a (3╨еб╔2)\n87fb703a (3╨еб╔30)\n945024a3 (3╨еб╔13)\n945b39a3 (3╨еб╔64)\n9715713a (3╨еб╔110)\na42e31a3 (3╨еб╔108)\na7446e3a (3╨еб╔72)\na7616f3a (3╨еб╔20)\nb4611da3 (3╨еб╔31)\nb4ca37a3 (3╨еб╔70)\nb4e51ea3 (3╨еб╔71)\nb4ed1ca3 (3╨еб╔59)\nc48c1da3 (3╨еб╔4)\nc49139a3 (3╨еб╔67)\nc4e121a3 (3╨еб╔74)\nc4e3ddc0 (3╨еб╔107)\nc4e7ddc0 (3╨еб╔112)\nc7706b3a (3╨еб╔11)\nc7f06b3a (3╨еб╔102)\nc7f66b3a (3╨еб╔17)\nd4072fa3 (3╨еб╔109)\nd40829a3 (3╨еб╔16)\nd44ed7c0 (3╨еб╔51)\nd4631da3 (3╨еб╔75)\nd4791ea3 (3╨еб╔95)\nd4831ea3 (3╨еб╔80)\nd4a71da3 (3╨еб╔61)\nd4c0d7c0 (3╨еб╔1)\nd4ca37a3 (3╨еб╔14)\nd4efddc0 (3╨еб╔96)\nd718723a (3╨еб╔26)\nd796703a (3╨еб╔57)\nd7a66d3a (3╨еб╔103)\nd7ea6d3a (3╨еб╔56)\ne4b21ea3 (3╨еб╔88)\ne4e038a3 (3╨еб╔23)\ne4e438a3 (3╨еб╔92)\ne7a46d3a (3╨еб╔25)\nf4972ba3 (3╨еб╔35)\nf4ac22a3 (3╨еб╔98)\nf4b523a3 (3╨еб╔63)\nf768693a (3╨еб╔42)\nf774703a (3╨еб╔32)\nf786663a (3╨еб╔94)\nf7ab6d3a (3╨еб╔97)\n"
    +"0791703a (5╨еб╔26new)\n1519eb00 (5╨еб╔18)\n2510eb00 (5╨еб╔20)\n2561ea00 (5╨еб╔24)\n3519eb00 (5╨еб╔30)\n353be800 (5╨еб╔unknown)\n354bea00 (5╨еб╔26)\n5518eb00 (5╨еб╔22)\n554cea00 (5╨еб╔19)\n67ee703a (5╨еб╔29)\n8512eb00 (5╨еб╔23new)\na507eb00 (5╨еб╔23)\nb41434a3 (5╨еб╔28)\nc4e538a3 (5╨еб╔27)\ne4c337a3 (5╨еб╔25)\ne527e900 (5╨еб╔21)\n";
    public Hashtable<String,String> cardlist= new Hashtable<String,String>();
    private CardsLib()
    {
    	String[] card =cards.split("\n");
    	for(int i=0;i<card.length;i++)
    	{
    		cardlist.put(card[i].split(" ")[0], card[i].split(" ")[1]);
    	}
    	
    }
    
    public String getInfo(String cardID)
    {
    	if(cardlist.get(cardID.toLowerCase())==null)
    		return "╢к©╗╡╩йгт╜╣Глщ©╗";
    	else
    		return cardlist.get(cardID.toLowerCase());
    }
    
    public static CardsLib getInstance()
    {
    	if(cb==null)
    	{
    		cb=new CardsLib();
    	}
    	return cb;
    }
    
    public static void main(String[] args)
    {
    	CardsLib cl=CardsLib.getInstance();
    	
    	JOptionPane.showMessageDialog(null,cl.getInfo(CardUtils.GetCardIDn().substring(0, 8)));
    }
}
