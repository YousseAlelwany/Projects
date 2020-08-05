package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws UnitException{
		
			if(this instanceof Evacuator && (r.getDisaster() instanceof GasLeak || r.getDisaster() instanceof Fire)) {
				throw new CannotTreatException(this, r, "This target can not be treated");
			}
		
			if(this instanceof Ambulance || this instanceof DiseaseControlUnit)
			{
				if(r instanceof ResidentialBuilding)
					throw new IncompatibleTargetException(this, r, "Target incompatible with Unit");
			}
			if(this instanceof Evacuator || this instanceof FireTruck || this instanceof GasControlUnit)
			{
				if(r instanceof Citizen)
					throw new IncompatibleTargetException(this, r, "Target incompatible with Unit");
			}
			if(this.canTreat(r) == false)
				throw new CannotTreatException(this, r, "This target can not be treated");
			
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	public boolean canTreat(Rescuable r)
	{
		if(r.getDisaster() == null)
			return false;
		if(r instanceof Citizen) {
			if(((Citizen) (r)).getBloodLoss() > 0 && this instanceof Ambulance)
				return true;
			if(((Citizen) (r)).getToxicity() > 0 && this instanceof DiseaseControlUnit)
				return true;
			
			return false;
		}
		if(r instanceof ResidentialBuilding) {
			if(((ResidentialBuilding) (r)).getFireDamage() > 0 && this instanceof FireTruck)
				return true;
			if(((ResidentialBuilding) (r)).getGasLevel() > 0 && this instanceof GasControlUnit)
				return true;
			if(((ResidentialBuilding) (r)).getStructuralIntegrity() == 100 && this instanceof Evacuator)
				return true;
			if(((ResidentialBuilding) (r)).getStructuralIntegrity() == 100 && this instanceof Evacuator) {
				return true;
			}
			
			
			
			return false;
		}
		return false;
	}
}
