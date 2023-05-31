/*
 * Copyright (C) Red Gate Software Ltd 2010-2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.util;

import static org.flywaydb.core.internal.reports.html.HtmlReportGenerator.generateHtml;

import java.io.File;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.output.CompositeResult;
import org.flywaydb.core.api.output.HtmlResult;

public class HtmlUtils {
    public static String toHtmlFile(String filename, CompositeResult<HtmlResult> results, Configuration config) {
        String fileContents = generateHtml(results, config);

        File file = new File(filename);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContents);
            return file.getCanonicalPath();
        } catch (Exception e) {
            throw new FlywayException("Unable to write HTML to file: " + e.getMessage());
        }

    }

    public static String getFormattedTimestamp(HtmlResult result) {
        if (result == null || result.getTimestamp() == null) {
            return "--";
        }
        return result.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String htmlEncode(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        htmlEncode(sb, input, 0, input.length());
        return sb.toString();
    }
    
    private static final String QUOT = "&quot;";

    private static final String AMP = "&amp;";

    private static final String APOS = "&#x27;";

    private static final String LT = "&lt;";

    private static final String EQUAL = "&#x3D;";

    private static final String GT = "&gt;";

    private static final String BACK_TICK = "&#x60;";
    
    private static void htmlEncode(StringBuilder a, CharSequence csq, int start, int end) {
        csq = csq == null ? "null" : csq;
        for (int i = start; i < end; i++) {
            char c = csq.charAt(i);
            switch (c) {
                case '"' : { // 34
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(QUOT);
                    break;
                }
                case '&' : { // 38
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(AMP);
                    break;

                }
                case '\'' : { // 39
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(APOS);
                    break;
                }
                case '<' : { // 60
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(LT);
                    break;
                }
                case '=' : { // 61
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(EQUAL);
                    break;
                }
                case '>' : { // 62
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(GT);
                    break;
                }
                case '`' : { // 96
                    a.append(csq, start, i);
                    start = i + 1;
                    a.append(BACK_TICK);
                    break;
                }
            }
        }
        a.append(csq, start, end);
    }
}