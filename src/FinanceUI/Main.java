package FinanceUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class Main extends JFrame {
  JComboBox combo = new JComboBox();

  public Main() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    for(int i=2000;i<3000;i++){
    	combo.addItem(new Integer(i));
    }
    
    combo.setEditable(true);
    System.out.println("#items=" + combo.getItemCount());

    combo.addActionListener(new ActionListener() {
      @Override
	public void actionPerformed(ActionEvent e) {
        System.out.println("Selected index=" + combo.getSelectedIndex()
            + " Selected item=" + combo.getSelectedItem());
      }
    });

    getContentPane().add(combo);
    pack();
    setVisible(true);
    setLocationRelativeTo(null);
  }

  public static void main(String arg[]) {
    new Main();
  }
}