package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.subsystems.Intake;
import org.simbotics.frc2018.subsystems.Intake.IntakeState;

public class IntakeAntiJam extends AutonCommand {

	private RobotOutput robotOut;
	private double output;
	private Intake intake;

	public IntakeAntiJam() {
		this(1.0);
	}

	public IntakeAntiJam(double maxOutput) {
		super(RobotComponent.INTAKE);
		this.output = maxOutput;
		this.intake = Intake.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setUsingSensors(true, true, false);

	}

	@Override
	public boolean calculate() {

		this.intake.setAutoOutput(this.output);

		this.intake.calculate();

		return this.intake.getIntakeState() == IntakeState.HAS_CUBE;

	}

	@Override
	public void firstCycle() {
		this.intake.firstCycle();
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setDesiredState(IntakeState.INTAKE);
		this.intake.setUsingSensors(true, true, false);
	}

	@Override
	public void override() {
		this.robotOut.setIntakeLeft(0.0);
		this.robotOut.setIntakeRight(0.0);
	}
}
