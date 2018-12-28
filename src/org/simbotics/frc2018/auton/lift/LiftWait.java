package org.simbotics.frc2018.auton.lift;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;

public class LiftWait extends AutonCommand {

	public LiftWait() {
		super(RobotComponent.LIFT);
	}

	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub

	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

}
