package org.example;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CreatingOptionsExample {
    public static void main(String[] args) throws ParseException {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("t", false, "display current time");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("t")) {
            // print the date and time
        } else {
            // print the date
        }

        final Options options2 = new Options();
        options2.addOption(new Option("d", "debug", false, "Turn on debug."));
        options2.addOption(new Option("e", "extract", false, "Turn on extract."));
        options2.addOption(new Option("o", "option", true, "Turn on option with argument."));

        // add c option
        options2.addOption("c", true, "country code");

        // get c option value
        String countryCode = cmd.getOptionValue("c");

        if (countryCode == null) {
            // print default date
        } else {
            // print date for country specified by countryCode
        }
    }
}
