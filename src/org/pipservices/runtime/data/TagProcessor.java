package org.pipservices.runtime.data;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

/**
 * Extracts from objects and processes search tags
 * @author Sergey Seroukhov, Ben O'Rourke
 * @version 1.0
 * @since 2016-05-01
 */
public class TagProcessor {

    private static final ObjectMapper _mapper = new ObjectMapper();
    private static final String HASH_TAG_REGEX = "#(\\w+)";
    private static final Pattern PATTERN = Pattern.compile(HASH_TAG_REGEX);
    private static final String NORMALIZE_REGEX = "(_|#)";
    private static final String COMPRESS_REGEX = "( |_|#)";
    private static final String SPLIT_REGEX = "( |,|;)";

    /**
     * Normalize a tag by replacing with REGEX: (_|#)
     *
     * @param tag The tag to normalize
     * @return A normalized tag
     */
    public static String normalizeTag(String tag) {
        return tag.trim().replaceAll(NORMALIZE_REGEX, "");
    }

    /**
     * Compress a tag by replacing with REGEX: ( |_|#)
     *
     * @param tag The tag to compress
     * @return A compressed tag
     */
    public static String compressTag(String tag) {
        return tag.replaceAll(COMPRESS_REGEX, "").toLowerCase(Locale.getDefault());
    }

    /**
     * Determines if two tags are equal based on length and contained characters
     *
     * @param tag1 The first tag
     * @param tag2 The second tag
     * @return True if tags are equal, false if tags are not equal
     */
    public static boolean equalTag(String tag1, String tag2) {
        if (tag1 == null && tag2 == null) {
            return true;
        }
        if (tag1 == null || tag2 == null) {
            return false;
        }
        if (tag1.isEmpty() && tag2.isEmpty()) {
            return true;
        }
        if (tag1.isEmpty() || tag2.isEmpty()) {
            return false;
        }
        return compressTag(tag1).compareTo(tag2) == 0;
    }

    /**
     * Normalize multiple tags contained in a single String using {@link org.pipservices.runtime.data.TagProcessor#normalizeTag(String)}
     *
     * @param tags The tags to normalize
     * @return A String array of normalized tags
     */
    public static String[] normalizeTags(String tags) {
        String[] splitTags = splitTags(tags);
        for (int i = 0; i < splitTags.length; i++) {
            splitTags[i] = normalizeTag(splitTags[i]);
        }
        return splitTags;
    }

    /**
     * Compress multiple tags contained in a single String using {@link org.pipservices.runtime.data.TagProcessor#compressTag(String)}
     *
     * @param tags The tags to compress
     * @return A String array of compressed tags
     */
    public static String[] compressTags(String tags) {
        String[] splitTags = splitTags(tags);
        for (int i = 0; i < splitTags.length; i++) {
            splitTags[i] = compressTag(splitTags[i]);
        }
        return splitTags;
    }

    private static String[] splitTags(String tags) {
        return tags.split(SPLIT_REGEX);
    }

    /**
     * Extracts hash tags from a JSON object based on user defined tag search fields
     *
     * @param jsonObject   The JSON object to parse
     * @param searchFields The user defined tag search fields
     * @return String array of compressed tags based on the user defined tag search fields
     */
    public static String[] extractHashTags(String jsonObject, String[] searchFields) {
        try {
            List<String> hashTags = new ArrayList<>();
            // convert JSON object to map for parsing
            Map<String, Object> jsonMap = _mapper.readValue(jsonObject, new TypeReference<Map<String, Object>>() {});

            for (String searchField : searchFields) {
                // get value associated with tag
                Object jsonValue = jsonMap.get(searchField);
                if (jsonValue != null) {
                    Matcher matcher = PATTERN.matcher(jsonValue.toString());
                    // find raw hash tags in the string based on regex
                    while (matcher.find()) {
                        String compressedTag = compressTag(matcher.group());
                        if (!hashTags.contains(compressedTag)) {
                            hashTags.add(compressedTag);
                        }
                    }
                }
            }
            String[] stringArray = new String[hashTags.size()];
            hashTags.toArray(stringArray);
            return stringArray;
        } catch (IOException e) {
            return new String[0];
        }
    }
}
