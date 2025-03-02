import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.border.LineBorder;

class Distributor extends JFrame implements ActionListener, ItemListener
{
	private JTabbedPane tabStaff;
	
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private int iResult;
	private Vector vecColumn,vecRow,vecData;
	private int cols;
	
	private DefaultTableModel dTable;
	private JTable tableInfo;
	
	private Container Picture;
	private ImageIcon iPicture,iHome;
	
	private int iID;
	private Long liPhNo;
	private String sName,sAdd,sInput;
	private JLabel lIntro,lID,lName,lAdd,lPhNo,lPicture,lNote;
	private JTextField tID,tName,tAdd,tPhNo;
	private JComboBox cID;
	private ButtonGroup bg;
	private JRadioButton rAdd,rUpdate,rRemove;
	private Font f1,f2,f3;
	private JPanel pTable,pManage;
	
	private JSeparator sep,sep1;
	private JButton bRefresh,bSubmit,bClear,bHome;
	
	private Variables var;

	public Distributor()
	{
		// creation d'objets de variables
		var = new Variables();
		
		// reglage des aspects du cadre
		setTitle("Distributor");
		show();
		setResizable(false);
		setLocation(var.d.width/10-getWidth()/2,var.d.height/10-getHeight()/2);
		setSize(805,540);
		setJMenuBar(var.mb);
		
		// initialiser et ajouter une image
		Picture = getContentPane();
		getContentPane().setLayout(null);
		iPicture = new ImageIcon("C://b3//Auto Spares//Images//Horizon.jpg");
		lPicture = new JLabel(iPicture);
		
		Picture.add(lPicture);
		lPicture.setBounds(0,0,800,500);
		
		lIntro = new JLabel("Distributor");
		lIntro.setFont(new Font("Times New Roman",Font.PLAIN,40));
		lIntro.setForeground(Color.WHITE);
		
		// la date
		lPicture.add(var.lDate);
		var.lDate.setBounds(600,20,60,25);
		var.lDate.setForeground(Color.WHITE);
		
		lPicture.add(var.tDate);
		var.tDate.setBounds(665,20,110,25);
		
		// connection avec la base de donnees
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con = DriverManager.getConnection("jdbc:odbc:Auto spare","","");
			st = con.createStatement();
		}
		catch(SQLException se)
		{
			JOptionPane.showMessageDialog(this, "Sorry, unable to connect to database !!");
			dispose();
		}
		
		// initialisation du tableau i.e. TAB "VIEW"
		bRefresh = new JButton("Refresh");
		bRefresh.setToolTipText("Refresh the above table");
		pTable = new JPanel();
		pTable.setLayout(null);
		try
		{
			st=con.createStatement();
			rs=st.executeQuery("select * from Distributor");
			ResultSetMetaData md = rs.getMetaData();
			cols = md.getColumnCount();
			vecColumn = new Vector();
			vecData = new Vector();
			for(int i=1;i<=cols; i++)
            {
            	vecColumn.addElement(md.getColumnName(i));
            }
            while(rs.next())
            {
            	vecRow = new Vector(cols);
            	for (int i=1;i<=cols;i++)
                {
                	vecRow.addElement(rs.getObject(i));
                }
                vecData.addElement(vecRow);
            }
		}
		catch(SQLException sqle)
		{
			JOptionPane.showMessageDialog(this,"Unable to connect to the Database !");			
		}
		tableInfo = new JTable(vecData,vecColumn);
		tableInfo.setAutoscrolls(true);
        JScrollPane scrollBar = new JScrollPane(tableInfo,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		pTable.add(scrollBar);
		scrollBar.setBounds(0,0,694,350);
		pTable.add(bRefresh);
		bRefresh.setBounds(2,355,100,40);
		
		// initialisation du TAB "MANAGE"
		f1 = new Font("Times New Roman",Font.PLAIN,20);
		f2 = new Font("Courier New",Font.PLAIN,15);
		f3 = new Font("Brush Script MT",Font.PLAIN,30);
		
		lID = new JLabel("ID");
		lID.setBounds(50,30,50,20);
		lID.setFont(f1);
		lName = new JLabel("Name");
		lName.setBounds(50,80,50,20);
		lName.setFont(f1);
		lAdd = new JLabel("Address");
		lAdd.setBounds(50,130,80,20);
		lAdd.setFont(f1);
		lPhNo = new JLabel("Phone no.");
		lPhNo.setBounds(50,180,120,20);
		lPhNo.setFont(f1);
		
		lNote = new JLabel("Please select an option from above !");
		lNote.setBounds(420,220,250,100);
		lNote.setFont(new Font("Times New Roman",Font.PLAIN,16));
		lNote.setForeground(Color.BLUE);
		
		tID = new JTextField();
		tID.setBounds(190,30,150,25);
		tID.setFont(f2);
		tName = new JTextField();
		tName.setBounds(190,80,150,25);
		tName.setFont(f2);
		tAdd = new JTextField();
		tAdd.setBounds(190,130,150,25);
		tAdd.setFont(f2);
		tPhNo = new JTextField();
		tPhNo.setBounds(190,180,150,25);
		tPhNo.setFont(f2);
		
		cID = new JComboBox();
		cID.setBounds(190,30,150,25);
		cID.setFont(f2);
		
		// ajout d'un type de rechange au Combo box
		try
		{	
			st=con.createStatement();
			rs=st.executeQuery("select * from Distributor");
			while(rs.next())
			{
				cID.addItem(rs.getString("ID"));
			}
		}
		catch(SQLException sql)
		{
			JOptionPane.showMessageDialog(this,"Unable to connect to Database");
			System.out.print(sql);
		}
		
		tID.setEnabled(false);
		tName.setEnabled(false);
		tAdd.setEnabled(false);
		tPhNo.setEnabled(false);
		
		bg = new ButtonGroup();
		
		rAdd = new JRadioButton("Add new Distributor");
		rAdd.setBounds(450,20,200,25);
		rAdd.setFont(f1);
		rUpdate = new JRadioButton("Update Distributor");
		rUpdate.setBounds(450,65,200,25);
		rUpdate.setFont(f1);
		rRemove = new JRadioButton("Remove Distributor");
		rRemove.setBounds(450,110,200,25);
		rRemove.setFont(f1);
		
		bg.add(rAdd);
		bg.add(rUpdate);
		bg.add(rRemove);
		
		// definir les boutons
		bSubmit = new JButton("Submit");
		bSubmit.setBounds(150,345,150,40);
		bSubmit.setFont(f3);
		
		bClear = new JButton("Clear");
		bClear.setBounds(400,345,150,40);
		bClear.setFont(f3);
		
		// initialisation du panneau et ajout des composants au IT
		pManage = new JPanel();
		pManage.setLayout(null);
		pManage.add(lID);	pManage.add(tID);	pManage.add(cID);
		pManage.add(lName);	pManage.add(tName);
		pManage.add(lAdd);	pManage.add(tAdd);
		pManage.add(lPhNo);	pManage.add(tPhNo);
		
		pManage.add(rAdd);
		pManage.add(rUpdate);
		pManage.add(rRemove);
		
		pManage.add(lNote);
		
		pManage.add(bSubmit);
		pManage.add(bClear);
		
		iHome = new ImageIcon("C://b3//Auto Spares//Images//Home icon.gif");
		bHome = new JButton(iHome);
		lPicture.add(bHome);
		bHome.setBounds(725,396,70,80);
		bHome.setToolTipText("Go to Home");
		
		sep = new JSeparator();
    	sep.setBounds(0,330,800,10);
 	    sep.setForeground(Color.LIGHT_GRAY);
		pManage.add(sep);
		
		sep1 = new JSeparator(JSeparator.VERTICAL);
		sep1.setBounds(400,0,1,330);
		sep1.setForeground(Color.LIGHT_GRAY);
		pManage.add(sep1);
		
		// initialiser TABS
		tabStaff = new JTabbedPane();
		tabStaff.setBorder(new LineBorder(new Color(255,0,0),0));
		tabStaff.addTab("View",pTable);
		tabStaff.addTab("Manage",pManage);
		
		lPicture.add(tabStaff);
		tabStaff.setBounds(20,52,700,425);
		lPicture.add(lIntro);
		lIntro.setBounds(315,15,200,40);

		setLayout(null);
		
		// ajouter un evenement LISTENER
		rAdd.addItemListener(this);
		rUpdate.addItemListener(this);
		rRemove.addItemListener(this);
		
		bRefresh.addActionListener(this);
		bSubmit.addActionListener(this);
		bClear.addActionListener(this);
		cID.addActionListener(this);
		bHome.addActionListener(this);
		
		// elements du menu
		var.miQuotInv.addActionListener(this);
		var.miOrder.addActionListener(this);
		var.miSpare.addActionListener(this);
		var.miStaff.addActionListener(this);
		var.miCustomer.addActionListener(this);
		var.miReports.addActionListener(this);
		var.miUser.addActionListener(this);
		var.miDistributor.addActionListener(this);
		var.miLogoff.addActionListener(this);
		var.miExit.addActionListener(this);
		
		addWindowListener(new WindowClass());
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		iResult=0;
		if(ae.getSource() == bRefresh)
		{
			dispose();
			new Distributor();
		}
		if(ae.getSource() == bSubmit)
		{
			if(rAdd.isSelected())
			{
				try
				{
					iID = Integer.parseInt(tID.getText());
					sName = tName.getText();
					sAdd = tAdd.getText();
					liPhNo = Long.parseLong(tPhNo.getText());
					boolean check = true;
					
					for(int i=0;i<sName.length();i++)
					{
						if(Character.isDigit(sName.charAt(i)))
						{
							check = false;
						}
					}
					
					if(check == true)
					{
						if((sName.equals(""))||(sAdd.equals(""))||(liPhNo == 0))
						{
							JOptionPane.showMessageDialog(this,"Please enter the details properly !");
						}
						else
						{	
							iResult = st.executeUpdate("insert into Distributor values("+iID+",'"+sName+"','"+sAdd+"',"+liPhNo+")");
							if(iResult==1)
							{
								// si la Requete marche bien
								JOptionPane.showMessageDialog(this,"Record saved successfully");
								reset();												
							}
							else
							{
								// si la Requete ne marche pas
								JOptionPane.showMessageDialog(this,"Record not saved successfully");
								dispose();
							}
						}
					}
					else
						JOptionPane.showMessageDialog(this,"Distributor name should not contain any digits !!");
				}
				catch(NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(this,"Please enter the valid data");
					clear();
				}
				catch(SQLException sql)
				{
					JOptionPane.showMessageDialog(this,"Unable to connect to Database");
					System.out.print("\n"+sql);			
				}
			}

			else if(rUpdate.isSelected())
			{
				try
				{
					iID = Integer.parseInt((String)cID.getSelectedItem());
					sName = tName.getText();
					sAdd = tAdd.getText();
					liPhNo = Long.parseLong(tPhNo.getText());
					boolean check1 = true;
					
					for(int i=0;i<sName.length();i++)
					{
						if(Character.isDigit(sName.charAt(i)))
						{
							check1 = false;
						}
					}
					
					if(check1 == true)
					{
						if((sName.equals(""))||(sAdd.equals(""))||(liPhNo == 0))
						{
							JOptionPane.showMessageDialog(this,"Please enter the details properly !");
						}
						else
						{
							iResult = st.executeUpdate("update Distributor set Name='"+sName+"',Address='"+sAdd+"',Phone="+liPhNo+" where ID="+iID+"");
							if(iResult==1)
							{
								// si la Requete marche bien
								JOptionPane.showMessageDialog(this,"Record updated successfully");
								reset();
							}
							else
							{
								// si la Requete ne marche pas
								JOptionPane.showMessageDialog(this,"Record not saved successfully");
								clear();
							}
						}
					}
					else
						JOptionPane.showMessageDialog(this,"Distributor name should not contain any digits !!");
				}
				catch(NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(this,"Please enter the valid data");
					clear();
				}
				catch(SQLException sql)
				{
					JOptionPane.showMessageDialog(this,"Unable to connect to Database");
					System.out.print(sql);
					clear();			
				}	
			}
			
			else if(rRemove.isSelected())
			{
				try
				{
					iID = Integer.parseInt(tID.getText());

					iResult = st.executeUpdate("delete * from Distributor where ID="+iID+"");
					if(iResult==1)
					{
						// si la Requete marche bien
						JOptionPane.showMessageDialog(this,"Record deleted successfully");
						reset();
					}
					else
					{
						// si la Requete ne marche pas
						JOptionPane.showMessageDialog(this,"The Distributor with provided ID does not exist");
						clear();
					}
				}
				catch(NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(this,"Please enter the valid data");
					clear();
				}
				catch(SQLException sql)
				{
					JOptionPane.showMessageDialog(this,"Unable to connect to database");
					System.out.print(sql);
					clear();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,"Please select an option !");
			}
		}
		
		if(ae.getSource() == cID)
		{
			try
			{	
				st=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				rs=st.executeQuery("select * from Distributor where ID="+(cID.getSelectedItem()));
				rs.first();
				tName.setText(""+rs.getString("Name"));
				tAdd.setText(rs.getString("Address"));
				tPhNo.setText(""+rs.getInt("Phone"));
			}
			catch(SQLException sql)
			{
				System.out.print(sql);
				JOptionPane.showMessageDialog(this,"Unable to connect to Database");			
			}
		}

		if(ae.getSource() == bClear)
		{
			clear();
		}
		
		// elenents du menu
		if(ae.getSource() == var.miQuotInv)
		{
			new Quotation();
			dispose();
		}
		if(ae.getSource() == var.miOrder)
		{
			new Order();
			dispose();
		}
		if(ae.getSource() == var.miSpare)
		{
			new Spare();
			dispose();
		}
		if(ae.getSource() == var.miStaff)
		{
			new Staff();
			dispose();
		}
		if(ae.getSource() == var.miCustomer)
		{
			new Customer();
			dispose();
		}
		if(ae.getSource() == var.miReports)
		{
			new Reports();
			dispose();
		}
		if(ae.getSource() == var.miUser)
		{
			new User();
			dispose();
		}
		if(ae.getSource() == var.miDistributor)
		{
			new Distributor();
			dispose();
		}
		if(ae.getSource() == var.miLogoff)
		{
			int reply = JOptionPane.showConfirmDialog(null,"Are you sure you want to log off?","",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				dispose();
				new Login();
			}
			else
			{
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}
		if(ae.getSource() == var.miExit)
		{
			int reply = JOptionPane.showConfirmDialog(null,"Are you sure you want to exit?","",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				dispose();
			}
			else
			{
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}
		
		if(ae.getSource() == bHome)
		{
			new Home();
			dispose();
		}
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getItem() == rAdd)
		{
			lNote.setText("<html>Please enter the details of the new<br>Distributor !</br></html>");
			clear();
			enable();
			tID.setEditable(false);
			tID.setVisible(true);
			cID.setVisible(false);
			try
			{	
				st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				rs=st.executeQuery("select * from Distributor");
				rs.last();
				tID.setText(""+(1+rs.getInt("ID")));
			}
			catch(SQLException sql)
			{
				JOptionPane.showMessageDialog(this,"Unable to connect to Database");
				System.out.print(sql);			
			}
		}
		if(ie.getItem() == rUpdate)
		{
			lNote.setText("<html>Please enter the new details of the<br>Distributor to be updated !</br></html>");
			tID.setVisible(false);
			cID.setVisible(true);
			clear();
			enable();
		}
		if(ie.getItem() == rRemove)
		{
			lNote.setText("<html>Please enter the ID of the<br>Distributor to be removed !</br></html>");
			lID.setEnabled(true);	
			tID.setEnabled(true);
			tID.setEditable(true);
			tID.setVisible(true);
			cID.setVisible(false);
			clear();
			disable();
		}
	}
	
	public void enable()
	{
		tID.setEnabled(true);
		tID.setEditable(true);
		tName.setEnabled(true);
		tAdd.setEnabled(true);
		tPhNo.setEnabled(true);
	}
	
	public void disable()
	{
		tName.setEnabled(false);
		tAdd.setEnabled(false);
		tPhNo.setEnabled(false);
	}
	
	public void clear()
	{
		if(rAdd.isSelected())
		{
				try
				{	
					st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					rs=st.executeQuery("select * from Distributor");
					rs.last();
					tID.setText(""+(1+rs.getInt("ID")));
				}
				catch(SQLException sql)
				{
					JOptionPane.showMessageDialog(this,"Unable to connect to Database");
					System.out.print(sql);			
				}
		}
		else
		{
			tID.setText("");
		}
		tName.setText("");
		tAdd.setText("");
		tPhNo.setText("");
	}
	
	public void reset()
	{
		bg.clearSelection();
		lNote.setText("Please select an option from above !");
		tID.setEnabled(false);
		disable();
		clear();
	}

	class WindowClass extends WindowAdapter
	{
		public void windowClosing(WindowEvent we)
		{
			int reply = JOptionPane.showConfirmDialog(null,"Are you sure you want to exit?","",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				dispose();
			}
			else
			{
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}
	}
		
	public static void main(String args[])
	{
		new Distributor();
	}
	
}