package org.simbotics.frc2018.auton.util;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.SensorInput;

/**
 *
 * @author Michael
 */
public class AutonWaitUntilXPosition extends AutonCommand {

	private double x;
	private boolean goingPositive;
	private SensorInput sensorIn;

	public AutonWaitUntilXPosition(double x, boolean goingPositive) {
		super(RobotComponent.UTIL);
		this.x = x;
		this.goingPositive = goingPositive;
		this.sensorIn = SensorInput.getInstance();
	}

	@Override
	public void firstCycle() {
		// nothing
	}

	/*
	 * need to override checkAndRun so that it blocks even before going in to its
	 * "run seat"
	 */
	@Override
	public boolean checkAndRun() {
		if (this.goingPositive) {
			if (this.sensorIn.getDriveXPos() < this.x) {
				return false;
			} else {
				return super.checkAndRun();
			}
		} else {
			if (this.sensorIn.getDriveXPos() > this.x) {
				return false;
			} else {
				return super.checkAndRun();
			}
		}
	}

	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		// nothing to do

	}

}
