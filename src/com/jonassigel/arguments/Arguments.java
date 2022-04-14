package com.jonassigel.arguments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class Arguments {

    public static Map<String, String> getPariedArguments(String[] args, String... identifiers) {
        Map<String, String> resultingArgs = new HashMap<>();
        Arrays.stream(identifiers).forEach(i -> resultingArgs.putIfAbsent(i, ""));

        for (int i = 0; i < args.length - 1; i++) {
            if (resultingArgs.values().contains(args[i])) {
                resultingArgs.put(args[i], args[i + 1]);
                i++;
            }
        }
        return resultingArgs;
    }

    public static HashSet<String> getPresentArguments(String[] args, String... identifiers) {
        HashSet<String> contained = new HashSet<>();
        for (String arg : args) {
            for (String ident : identifiers) {
                if (arg != null && arg.equals(ident)) {
                    contained.add(ident);
                    break;
                }
            }
        }
        return contained;
    }

}
