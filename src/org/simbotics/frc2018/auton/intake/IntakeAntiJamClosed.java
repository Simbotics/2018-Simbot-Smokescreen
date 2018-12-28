package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.subsystems.Intake;
import org.simbotics.frc2018.subsystems.Intake.IntakeState;

public class IntakeAntiJamClosed extends AutonCommand {

	private RobotOutput robotOut;
	private double maxOutput;
	private Intake intake;

	public IntakeAntiJamClosed() {
		this(1.0);
	}

	public IntakeAntiJamClosed(double maxOutput) {
		super(RobotComponent.INTAKE);
		this.intake = Intake.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.maxOutput = maxOutput;
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setDesiredState(IntakeState.INTAKE);
		this.intake.setUsingSensors(true, true, false);

	}

	@Override
	public void firstCycle() {
		this.robotOut.setIntakeOpen(false);
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setDesiredState(IntakeState.INTAKE);
		this.intake.setUsingSensors(false, true, true);
		this.intake.firstCycle();
	}

	@Override
	public boolean calculate() {

		this.intake.setAutoOutput(this.maxOutput);

		this.intake.calculate();

		return this.intake.getIntakeState() == IntakeState.HAS_CUBE;

	}

	@Override
	public void override() {
		this.robotOut.setIntakeLeft(0.0);
		this.robotOut.setIntakeRight(0.0);
	}
}
