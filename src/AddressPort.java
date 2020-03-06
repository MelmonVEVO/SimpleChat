class AddressPort {
    static String getAddressOrPort(String[] args, String either) {
        try {
            for (int x = 0; x < args.length; x++) {
                if (args[x].equals(either)) {
                    return args[x + 1];
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (either.equals("-csp") || either.equals("-ccp")) {
            return "14001";
        }
        else {
            return "localhost";
        }
    }
}
