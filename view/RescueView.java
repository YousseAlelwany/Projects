package view;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.CommandCenter;
import exceptions.DisasterException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.PoliceUnit;
import model.units.Unit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RescueView extends JFrame implements ActionListener{
	private JButton game = new JButton();
	private JPanel units;
	private JPanel grid;
	private JPanel info;
	private JPanel bottomP;
	private JButton nxt = new JButton();
	private JButton currentCycle = new JButton();
	private CommandCenter c = new CommandCenter();
	private JButton [][] BGrid = new JButton[12][12];
	private ArrayList unitsA = new ArrayList();
	public RescueView() throws Exception
	{
		setTitle("Rescue Simulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setBounds(0, 0, 1280, 720);
		this.grid = new JPanel();
		this.grid.setLayout(new GridLayout(10, 10));
		this.grid.setPreferredSize(new Dimension(960, 720));
		this.setExtendedState(this.MAXIMIZED_BOTH);
		
//		for(int i = 1; i <101; i++)
//		{
//			int x;
//			int y;
//			x = i / 11;
//			y = i % 11;
//			JButton t = new JButton();
//			BGrid [x][y] = t;
//			t.setLabel("" + x + "," + y);
//			this.grid.add(t, BorderLayout.CENTER);
//		}
		for(int i = 0 ; i < 10; i++)
		{
			for(int j = 0 ; j < 10 ; j++)
			{
				JButton t = new JButton();
				BGrid[i][j] = t;
				t.addActionListener(this);
				t.setLabel("" + i + "," + j);
				t.setToolTipText("" + i + "," + j);
				//t.setIcon(new ImageIcon("ground.jpg"));
				this.grid.add(t, BorderLayout.CENTER);				
			}
		}
		add(this.grid, BorderLayout.CENTER);
		this.units = new JPanel();
		this.units.setPreferredSize(new Dimension(500, getHeight()));
		add(this.units, BorderLayout.EAST);
		this.info = new JPanel();
		this.info.setPreferredSize(new Dimension(500, getHeight()));
		//Add action listener w/ this to buttons to perform actions
		nxt.addActionListener(this);
		nxt.setActionCommand("nextCycle");
		nxt.setLabel("Next Cycle");
		//this.info.add(nxt, BorderLayout.NORTH);
		add(this.info, BorderLayout.WEST);
		this.currentCycle.setLabel(Integer.toString(c.getEngine().getCurrentCycle()));
		//this.info.add(currentCycle, BorderLayout.CENTER);
		this.bottomP =new JPanel();
		bottomP.setLayout(new FlowLayout());
		bottomP.setSize(new Dimension(this.getWidth(), 120));
		bottomP.add(currentCycle);
		bottomP.add(nxt);
		for(int u = 0 ; u < c.getEmergencyUnits().size(); u++)
		{
			Unit x = c.getEmergencyUnits().get(u);
			JButton tu = new JButton();
			if(x instanceof Ambulance)
			{
				tu.setText("Ambulance");
				tu.setActionCommand("ambulance");
			}
			if(x instanceof FireTruck)
			{
				tu.setText("Fire Truck");
				tu.setActionCommand("fireTruck");
			}
			if(x instanceof GasControlUnit)
			{
				tu.setText("Gas Control Unit");
				tu.setActionCommand("gcu");
			}
			if(x instanceof Evacuator)
			{
				tu.setText("Evacuator");
				tu.setActionCommand("evacuator");
			}
			if(x instanceof DiseaseControlUnit)
			{
				tu.setText("Disease Control Unit");
				tu.setActionCommand("dcu");
			}
			tu.setPreferredSize(new Dimension(500,75));
			tu.setFont(new Font("Times New Roman", Font.BOLD, 20));
			tu.setBackground(Color.GREEN);
			unitsA.add(tu);
			units.add(tu);
		}
		add(bottomP, BorderLayout.SOUTH);
		game.setName("Death Count: ");
		bottomP.add(game);
		game.setFont(new Font("Times New Roman", Font.BOLD, 20));
		nxt.setFont(new Font("Times New Roman", Font.BOLD, 20));
		currentCycle.setFont(new Font("Times New Roman", Font.BOLD, 20));
		game.setPreferredSize(new Dimension(300, 50));
		nxt.setPreferredSize(new Dimension(300, 50));
		currentCycle.setPreferredSize(new Dimension(300, 50));
		game.setLabel("Death Count: 0");
		game.setBackground(Color.RED);
		setVisible(true);
		revalidate();
		repaint();
	}
	public static void main(String[] args) throws Exception {
		RescueView r = new RescueView();
	}

	public void actionPerformed(ActionEvent e)
	{
		//Add "if (e.getActionCommand() == "*action command string here*")" to set the button action
		// don't forget to set action command for the button
		game.setLabel("Death Count: " + Integer.toString(c.getEngine().calculateCasualties()));
		if (e.getActionCommand() == "nextCycle")
		{	
			info.removeAll();
			try
			{
				c.getEngine().nextCycle();
			}
			catch (DisasterException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.currentCycle.setLabel(Integer.toString(c.getEngine().getCurrentCycle()));
//			units.removeAll();
//			for(int pp = 0 ; pp < unitsA.size(); pp++)
//				units.add((JButton)(unitsA.get(pp)));
			for(int m = 0; m < c.getEngine().getExecutedDisasters().size();m++)
			{
				if(c.getEngine().getExecutedDisasters().get(m).getStartCycle() == c.getEngine().getCurrentCycle()) 
				{
					
					JLabel x = null;
					if(c.getEngine().getExecutedDisasters().get(m) instanceof Fire)
						x = new JLabel("Fire on building at position: " + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getX() + "," + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getY());
					if(c.getEngine().getExecutedDisasters().get(m) instanceof GasLeak)
						x = new JLabel("GasLeak on building at position: " + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getX() + "," + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getY());
					if(c.getEngine().getExecutedDisasters().get(m) instanceof Collapse)
						x = new JLabel("Collapse on building at position: " + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getX() + "," + ((ResidentialBuilding)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getLocation().getY());
					if(c.getEngine().getExecutedDisasters().get(m) instanceof Infection) 	
						x = new JLabel("Infection on: " + ((Citizen)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getName()  );
					if(c.getEngine().getExecutedDisasters().get(m) instanceof Injury)	
						x = new JLabel("Injury on: " + ((Citizen)(c.getEngine().getExecutedDisasters().get(m).getTarget())).getName() );
					x.setFont(new Font("Times New Roman", Font.BOLD, 32));
					units.add(x);
					units.setVisible(true);
				}
			}
			for(int kj = 0;kj < c.getEngine().getCitizens().size();kj++ )
			{
				if(c.getEngine().getCitizens().get(kj).getState() == CitizenState.DECEASED) 
				{
					JLabel r = new JLabel("Citizen dead at " + c.getVisibleBuildings().get(kj).getLocation().getX() + "," + c.getVisibleBuildings().get(kj).getLocation().getY());
					units.add(r);
					units.setVisible(true);
					r.setFont(new Font("Times New Roman", Font.BOLD, 32));
				
				}	
			}
			for (int i =0; i<c.getVisibleBuildings().size(); i++)
			{
				//Interface :))
				int x = c.getVisibleBuildings().get(i).getLocation().getX();
				int y = c.getVisibleBuildings().get(i).getLocation().getY();
				JButton t  = new JButton();
				t.setLabel("a building");
				t.setIcon(new ImageIcon("building.png"));
				t.addActionListener(this);
				t.setActionCommand("building");
				t.setName(""+x+y);
				BGrid[x][y] = t;
				this.grid.removeAll();
				for(int k = 0 ; k < 10; k++)
				{
					for(int l = 0 ; l < 10 ; l++)
					{
						if(k == x && l == y)
						{
							t.setLabel(null);
							t.setToolTipText("" + x + "," + y);
							this.grid.add(t);
						}
						else
						{
							for(int b = 0 ; b < 10; b++)
							{
								for(int c = 0 ; c < 10 ; c++ )
								{
									this.grid.add(BGrid[b][c]);
								}
							}
						}
					}
				}
				this.grid.revalidate();
				this.grid.repaint();
				this.revalidate();
				this.repaint();
				
			}
			System.out.println(c.getVisibleBuildings().size() + "buildings");
			for (int i =0; i<c.getVisibleCitizens().size(); i++)
			{
				//Interface :))
				int x = c.getVisibleCitizens().get(i).getLocation().getX();
				int y = c.getVisibleCitizens().get(i).getLocation().getY();
				JButton t  = new JButton();
				t.setLabel("a citizen");
				t.setIcon(new ImageIcon("citizen.png"));
				t.addActionListener(this);
				t.setActionCommand("citizen");
				t.setName(""+x+y);;
				BGrid[x][y] = t;
				this.grid.removeAll();
				for(int k = 0 ; k < 10; k++)
				{
					for(int l = 0 ; l < 10 ; l++)
					{
						if(k == x && l == y)
						{
							t.setLabel(null);
							t.setToolTipText("" + x + "," + y);
							this.grid.add(t);
						}
						else
						{
							for(int bbb = 0 ; bbb < 10; bbb++)
							{
								for(int cv = 0 ; cv < 10 ; cv++ )
								{
									this.grid.add(BGrid[bbb][cv]);
								}
							}
						}
					}
				}
				this.grid.revalidate();
				this.grid.repaint();
				this.revalidate();
				this.repaint();
				
			}
			
			for(int k = 0; k < c.getEmergencyUnits().size(); k ++)
			{
				int x = c.getEmergencyUnits().get(k).getLocation().getX();
				int y = c.getEmergencyUnits().get(k).getLocation().getY();
				JButton t  = new JButton();
				t.setLabel("a unit");
				//t.setIcon(new ImageIcon("citizen.png"));
				t.addActionListener(this);
				t.setActionCommand("unit");
				t.setName(""+x+y);
				t.setLabel("a unit");
				BGrid[x][y] = t;
				this.grid.removeAll();
				for(int q = 0 ; q < 10; q++)
				{
					for(int l = 0 ; l < 10 ; l++)
					{
						if(q == x && l == y)
						{
							//t.setLabel(null);
							t.setToolTipText("" + x + "," + y);
							this.grid.add(t);
						}
						else
						{
							for(int bbb = 0 ; bbb < 10; bbb++)
							{
								for(int cv = 0 ; cv < 10 ; cv++ )
								{
									this.grid.add(BGrid[bbb][cv]);
								}
							}
						}
					}
				}
			}
			
			this.grid.revalidate();
			this.grid.repaint();
			this.revalidate();
			this.repaint();
			
			System.out.println(c.getVisibleCitizens().size() + "citizens");
			
			
			if(c.getEngine().checkGameOver())
			{
				System.out.println("game over");
			}
		}
		
		if(e.getActionCommand() == "building")
		{
			JButton tmp = ((JButton)e.getSource());
			String st = tmp.getName();
			int x = Integer.parseInt(st.substring(0, 1));
			int y = Integer.parseInt(st.substring(1, 2));
			for(int i = 0; i < c.getVisibleBuildings().size(); i++)
			{
				ResidentialBuilding t = c.getVisibleBuildings().get(i);
				if(t.getLocation().getX() == x && t.getLocation().getY() == y)
				{
					helpB(t);
				}
			}
	
		}
		if(e.getActionCommand() == "citizen")
		{
			JButton tmp = ((JButton)e.getSource());
			String st = tmp.getName();
			int x = Integer.parseInt(st.substring(0, 1));
			int y = Integer.parseInt(st.substring(1, 2));
			for(int i = 0; i < c.getVisibleCitizens().size(); i++)
			{
				Citizen t = c.getVisibleCitizens().get(i);
				if(t.getLocation().getX() == x && t.getLocation().getY() == y)
				{
					helpC(t);
				}
			}
		}
		if(e.getActionCommand() == "unit")
		{
			JButton tmp = ((JButton)e.getSource());
			String st = tmp.getName();
			int x = Integer.parseInt(st.substring(0, 1));
			int y = Integer.parseInt(st.substring(1, 2));
			for(int i = 0; i < c.getEmergencyUnits().size(); i++)
			{
				Unit t = c.getEmergencyUnits().get(i);
				if(t.getLocation().getX() == x && t.getLocation().getY() == y)
				{
					helpU(t);
				}
			}
		}
		
	}
	
	private void helpU(Unit t)
	{
		JTextArea text = new JTextArea();
		text.setEditable(false);
		String txt = "";
		txt += "Unit id: "+t.getUnitID() + "\n";
		if(t instanceof Evacuator)
		{
			txt += "Unit type: Evacuator"+ "\n";
			txt += "Number of passengers: " + ((Evacuator) t).getPassengers().size()+ "\n";
			for(int kek = 0 ; kek < ((Evacuator) t).getPassengers().size() ; kek++)
			{
				Citizen tad = ((Evacuator) t).getPassengers().get(kek);
				txt += "Location: " +  tad.getLocation().getX() + ", " + tad.getLocation().getY() + "\n" ;
				txt += "Name: " + tad.getName()+"\n";
				txt += "Age: " + tad.getAge() + "\n";
				txt += "National ID: " + tad.getNationalID() + "\n";
				txt += "HP: " + tad.getHp() + "\n";
				txt += "Blood Loss: " + tad.getBloodLoss() +"\n";
				txt += "Toxicity: " + tad.getToxicity() + "\n";
				txt += "The Citizen is: " + tad.getState() +"\n";
				if (tad.getDisaster() != null) {
					if (tad.getDisaster() instanceof Injury) {
						txt += "The Citizen has an injury!";
					}
					if (tad.getDisaster() instanceof Infection) {
						txt += "The Citizen has an infection!";
					}
				}
			}
		}
		if(t instanceof Ambulance)
		{
			txt+= "Unit type: Ambulance" + "\n";
		}
		if(t instanceof DiseaseControlUnit)
		{
			txt+= "Unit type: DiseaseControlUnit"+ "\n";
		}
		if(t instanceof FireTruck)
		{
			txt+= "Unit type: FireTruck"+ "\n";
		}
		if(t instanceof GasControlUnit)
		{
			txt+= "Unit type: Gas Control Unit"+ "\n";
		}
		txt += "Unit Location: "+ t.getLocation().getX() + ", " + t.getLocation().getY()+ "\n";
		txt += "Steps per cycle: "+t.getStepsPerCycle() + "\n";
		txt += "Unit state: "+t.getState() + "\n";
		if(t.getTarget() instanceof ResidentialBuilding)
		{
			ResidentialBuilding tm = (ResidentialBuilding) t.getTarget();
			txt += "Target Building" + "\n";
			txt += "Target location: " + tm.getLocation().getX() + ", " + tm.getLocation().getY()+ "\n";
		}
		if(t.getTarget() instanceof Citizen)
		{
			Citizen tm = (Citizen) t.getTarget();
			txt += "Target citizen" + "\n";
			txt += "Target location: " + tm.getLocation().getX() + ", " + tm.getLocation().getY()+ "\n";
		}
		if(t.getTarget() == null)
		{
			txt += "The Unit has no target";
		}
		text.setText(txt);
		info.add(text);
		revalidate();
		repaint();
		
	}
	private void helpB(ResidentialBuilding b)
	{
		info.removeAll();
		//JFrame info = new JFrame();
		//info.setBounds(200, 200, 250, 250);
		//info.setVisible(true);
		info.setLayout(new GridLayout(10, 1));
		JLabel location = new JLabel("Location: " + b.getLocation().getX() + ", " + b.getLocation().getY());
      	info.add(location);
  		JLabel st = new JLabel("Structural Integrity: " + b.getStructuralIntegrity());
  		info.add(st);
  		JLabel fo = new JLabel ("Foundation Damage: " + b.getFoundationDamage());
  		info.add(fo);
  		JLabel fi = new JLabel ("Fire Damage: " + b.getFireDamage());
  		info.add(fi);
  		JLabel gg = new JLabel ("Gas Level: " + b.getFireDamage());
  		info.add(gg);
  		JLabel occ = new JLabel ("Number of Occupants: " + b.getOccupants().size());
  		info.add(occ);
		if (b.getDisaster()!=null) {
			if (b.getDisaster() instanceof Fire) {
	      	JLabel df = new JLabel ("Disaster: Fire");
      		info.add(df);
      		//setVisible(true);
      		}
			if (b.getDisaster() instanceof Collapse) {
		      	JLabel dc = new JLabel ("Disaster: Collapse");
	      		info.add(dc);
	      		//setVisible(true);
				}
			if (b.getDisaster() instanceof GasLeak) {
			 	JLabel dg = new JLabel ("Disaster: GasLeak");
	      		info.add(dg);
	      		//setVisible(true);
			}
			}
		
		revalidate();
		repaint();
	}
	
	private void helpC(Citizen c) {
		info.removeAll();
		//info.setLayout(new GridLayout(10, 1));
		JTextArea text = new JTextArea(2, 12);
		text.setEditable(false);
		String txt = "";
		txt += "Location: " +  c.getLocation().getX() + ", " + c.getLocation().getY() + "\n" ;
		txt += "Name: " + c.getName()+"\n";
		txt += "Age: " + c.getAge() + "\n";
		txt += "National ID: " + c.getNationalID() + "\n";
		txt += "HP: " + c.getHp() + "\n";
		txt += "Blood Loss: " + c.getBloodLoss() +"\n";
		txt += "Toxicity: " + c.getToxicity() + "\n";
		txt += "The Citizen is: " + c.getState() +"\n";
		if (c.getDisaster() != null) {
			if (c.getDisaster() instanceof Injury) {
				txt += "The Citizen has an injury!";
			}
			if (c.getDisaster() instanceof Infection) {
				txt += "The Citizen has an infection!";
			}
		}
//		JLabel loc = new JLabel("Location: " + c.getLocation().getX() + ", " + c.getLocation().getY());
//		info.add(loc);
//	    JLabel citizen = new JLabel (("Name : " + c.getName() +"  Age : " + c.getAge()) + "  National ID : " + c.getNationalID() );
//	    info.add(citizen);
//		JLabel h = new JLabel("HP : " + c.getHp());
//		info.add(h);
//		JLabel bl = new JLabel("Blood Loss: " + c.getBloodLoss());
//		info.add(bl);
//		JLabel to = new JLabel("Toxicity: " + c.getToxicity());
//		info.add(to);
//		JLabel st = new JLabel("The Citizen is: " + c.getState());
//		info.add(st);
//		if (c.getDisaster() != null) {
//			if (c.getDisaster() instanceof Injury) {
//				JLabel ij = new JLabel("The Citizen has an injury!");
//				info.add(ij);
//			}
//			if (c.getDisaster() instanceof Infection) {
//				JLabel inf = new JLabel("The Citizen has an infection!");
//				info.add(inf);
//			}
//		}
		text.setText(txt);
		info.add(text);
		revalidate();
		repaint();
	}
}

