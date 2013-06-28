package se.gotling.itachcontrol;

/**
 * Simple application to try out library and use stand alone
 * Currently hard coded host address
 * @author gotling
 */
public class ITachNexa {

	private static int port = 4998;

        /**
         * 
         * @param args[0] Host in format address[:port]. If no port is given
         * standard port 4998 is used
         * @param args[1] Device currently as a number from 1 to 3
         * @param args[2] State as a string, ON or OFF
         * @param args[3] Optional, repeat count, how many times a command 
         * should be sent, defaults to 3
         * @throws Exception 
         */
	public static void main(String[] args) throws Exception {
		System.out.println("iTachNexa by Marcus Gotling 2011-2013\n");

		if (checkArgs(args)) {
			ITachLib iTachLib;
                        
                        String host = args[0];
                        if (host.contains(":")) {
                            String[] hostPort = host.split(":");
                            host = hostPort[0];
                            port = Integer.parseInt(hostPort[1]);
                        }
                        
			if (args.length > 3) {
				iTachLib = new ITachLib(host, port, Integer.parseInt(args[3]));
			} else {
				iTachLib = new ITachLib(host, port, 3);
			}

			boolean state = args[2].toUpperCase().equals("ON") ? false : true;
			int device = Integer.parseInt(args[1]);

			String command = NexaLib.buildCommand(device, state);
                        
                        System.out.println("> " + command);
			String result = iTachLib.sendCommand(command);
                        System.out.println("< " + result);
		}
	}

        /**
         * Check command line arguments
         * @param args
         * @return true if arguments seems OK
         * TODO: Allow host, port and additional settings (@see iTachLib) to be passed
         */
	public static boolean checkArgs(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: iTachControl <host> <device> <ON/OFF> [repeat count]");
			return false;
		} else {
			try {
				int device = Integer.parseInt(args[1]);
				if (device < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Device must be a number greater than 0");
				return false;
			}

			if (!args[2].toUpperCase().equals("ON") && !args[2].toUpperCase().equals("OFF")) {
				System.out.println("State must be ON or OFF");
				return false;
			}

			return true;
		}
	}
}
