package org.pipservices.runtime.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagProcessorTest {

    @Test
    public void testNormalizeTag() throws Exception {
        String tag = "#tag_2";
        String normalizedTag = TagProcessor.normalizeTag(tag);
        assertEquals("tag2", normalizedTag);
    }

    @Test
    public void testNormalizeComplexTag() throws Exception {
        String tag = "_____#___T_____A____G_2_______";
        String normalizedTag = TagProcessor.normalizeTag(tag);
        assertEquals("TAG2", normalizedTag);
    }

    @Test
    public void testCompressTag() throws Exception {
        String tag = " #tag_2";
        String normalizedTag = TagProcessor.compressTag(tag);
        assertEquals("tag2", normalizedTag);
    }

    @Test
    public void testCompressComplexTag() throws Exception {
        String tag = "  #  __T  __A  __G _ 2   ";
        String normalizedTag = TagProcessor.compressTag(tag);
        assertEquals("tag2", normalizedTag);
    }

    @Test
    public void testExtractHashTags() throws Exception {
        String[] endResults = new String[]{"tag1", "tag2", "tag3", "tag4", "tag5"};
        String jsonString = " {\n" +
                "                \"tags\": [\"Tag 1\", \"tag_2\", \"TAG3\"],\n" +
                "                \"name\": \"Text with tag1 #Tag1\",\n" +
                "                \"description\": \"Text with #tag_2,#tag3-#tag4 and #TAG__5\"\n" +
                "            }";

        String[] hashTags = TagProcessor.extractHashTags(jsonString, new String[]{"name", "description"});

        assertArrayEquals(endResults, hashTags);
    }

    @Test
    public void testExtractHashTags2() throws Exception {
        String[] endResults = new String[]{"tag1", "tag2", "tag3", "tag4", "tag5"};
        String jsonString = " {\n" +
                "                \"tags\": [\"Tag 1\", \"tag_2\", \"TAG3\"],\n" +
                "                \"name\": {\n" +
                "                    \"short\": \"Just a name\",\n" +
                "                    \"full\": \"Text with tag1 #Tag1\"\n" +
                "                },\n" +
                "                \"description\": {\n" +
                "                    \"en\": \"Text with #tag_2,#tag3,#tag4 and #TAG__5\",\n" +
                "                    \"ru\": \"Текст с #tag_2,#tag3 и #TAG__5\"\n" +
                "                }\n" +
                "            }";

        String[] hashTags = TagProcessor.extractHashTags(jsonString, new String[]{"name", "description"});

        assertArrayEquals(endResults, hashTags);
    }
}