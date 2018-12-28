package org.simbotics.frc2018;

import org.simbotics.frc2018.auton.AutonControl;
import org.simbotics.frc2018.io.Dashboard;
import org.simbotics.frc2018.io.DriverInput;
import org.simbotics.frc2018.io.Logger;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.teleop.TeleopControl;
import org.simbotics.frc2018.util.Debugger;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	private RobotOutput robotOut;
	private DriverInput driverInput;
	private SensorInput sensorInput;
	private TeleopControl teleopControl;
	private Logger logger;
	private Dashboard dashboard;
	private boolean pushToDashboard = true;
	private boolean usingWrist = true; 

	@Override
	public void robotInit() {
		Debugger.defaultOn();
		this.dashboard = Dashboard.getInstance();
		if (this.pushToDashboard) {
			RobotConstants.pushValues();
		}
		SmartDashboard.putBoolean("USING WRIST", this.usingWrist);
		this.robotOut = RobotOutput.getInstance();
		this.driverInput = DriverInput.getInstance();
		this.sensorInput = SensorInput.getInstance();
		this.teleopControl = TeleopControl.getInstance();
		this.logger = Logger.getInstance();
		LiveWindow.disableAllTelemetry();

	}

	@Override
	public void disabledInit() {
		this.robotOut.stopAll();
		this.teleopControl.disable();
		this.logger.close();
	}

	@Override
	public void disabledPeriodic() {
		this.sensorInput.update();
		this.dashboard.updateAll();
		AutonControl.getInstance().updateModes();

	}

	@Override
	public void autonomousInit() {
		AutonControl.getInstance().initialize();
		this.sensorInput.reset();
		this.sensorInput.resetAutonTimer();
		this.logger.openFile();
		this.robotOut.configureSpeedControllers();
		this.robotOut.setDriveRampRate(0, 20);

	}

	@Override
	public void autonomousPeriodic() {
		this.sensorInput.update();
		this.dashboard.updateAll();
		AutonControl.getInstance().runCycle();
		this.logger.logAll();
	}

	public void testInit() {
		
	}

	public void testPeriodic() {
		
	}

	@Override
	public void teleopInit() {
		this.robotOut.configureSpeedControllers();
		this.teleopControl.initialize();
	}

	@Override
	public void teleopPeriodic() {
		this.sensorInput.update();
		this.dashboard.updateAll();
		this.teleopControl.runCycle();

	}

}
