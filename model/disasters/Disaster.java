package model.disasters;

import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active;
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	}
	public void strike() throws DisasterException{
		/*try {
			if(this.getTarget() instanceof Citizen) {
			if(((Citizen)getTarget()).getState()==CitizenState.DECEASED)
				throw new CitizenAlreadyDeadException("Citizen already dead");	
					}
			}
			catch(CitizenAlreadyDeadException e)
			{
				System.out.println(e);
			}*/
		target.struckBy(this);
		active=true;
	}
}
