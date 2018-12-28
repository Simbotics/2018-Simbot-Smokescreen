package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;

public class IntakeHoldCube extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;

	public IntakeHoldCube() {
		super(RobotComponent.INTAKE);
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean calculate() {
		if (!this.sensorIn.getIntakeLightSensor()) {
			this.robotOut.setIntakeLeft(1.0);
			this.robotOut.setIntakeRight(1.0);
		} else {
			this.robotOut.setIntakeLeft(0.0);
			this.robotOut.setIntakeRight(0.0);
		}
		return false;

	}

	@Override
	public void override() {
		this.robotOut.setIntakeLeft(0.0);
		this.robotOut.setIntakeRight(0.0);
	}

}
