package se.gotling.itach;

/**
 * Class to build comma separated IR-pulse values recognized by iTach module
 * @author gotling
 */
public class NexaLib {
	
	private static String baseCommand = "1,13,39,13,39,13,39,13,39,13,39,13,39,13,39,13,39,13,39,[DEVICE]"
			+ ",13,39,13,39,13,39,13,39,13,39,13,39,13,39,39,13,13,39,39,13,13,39,[STATE],13,427";
	private static String ON = "39,13";
	private static String OFF = "13,39";
	
        /**
         * Get the Nexa/Proove specific command for turning on or off device
         * @param device to be commanded
         * @param off set to true to turn off
         * @return commando that can be used by iTachLib
         */
	public static String buildCommand(int device, boolean off) {
		String command =  baseCommand.replace("[DEVICE]", getDevice(device));
		command = command.replace("[STATE]", getState(off));
		
		return command;
	}
	
        /**
         * Return hex (decimal?) code for device specified by id
         * @param id is value 1, 2 or 3 like Nexa counts
         * @return device id as comma separated hex values
         */
	private static String getDevice(int id) {
		switch (id) {
			case (1):
				return "13,39,13,39,13,39";
			case (2):
				return "39,13,13,39,13,39";
			case (3):
				return "13,39,13,39,39,13";
			default:
				return null;
		}
	}
	
        /**
         * Translates boolean to hex code
         * @param off set to true for negative command
         * @return state as comma separated hex values
         */
	private static String getState(boolean off) {
		if(off)
			return OFF;
		else
			return ON;
	}
}
