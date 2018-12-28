package org.simbotics.frc2018.auton.util;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.SensorInput;

/**
 *
 * @author Michael
 */
public class AutonWaitUntilElevatorHeight extends AutonCommand {

	private double height;
	private boolean goingDown;
	private SensorInput sensorIn;

	public AutonWaitUntilElevatorHeight(double height, boolean goingDown) {
		super(RobotComponent.UTIL);
		this.height = height;
		this.goingDown = goingDown;
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
		if (this.goingDown) {
			if (this.sensorIn.getElevatorHeight() > this.height) {
				return false;
			} else {
				return super.checkAndRun();
			}
		} else {
			if (this.sensorIn.getElevatorHeight() < this.height) {
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
