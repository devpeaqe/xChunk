package de.peaqe.xchunk.utils;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: ListUtils
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 17.07.2023 / 18:32
 *
 */

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public class ListUtils {

    private final String separator = ", ";

    public String listToString(List<UUID> uuidList) {
        StringJoiner joiner = new StringJoiner(separator);
        for (UUID uuid : uuidList) {
            joiner.add(uuid.toString());
        }
        return joiner.toString();
    }

    public List<UUID> stringToList(String input) {
        String[] uuidStrings = input.split(separator);
        return Arrays.stream(uuidStrings).map(UUID::fromString).toList();
    }

}
