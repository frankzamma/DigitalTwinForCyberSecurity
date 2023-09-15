package it.unisa.zammarrelli.digitaltwinforcybersecurity.util;

import com.intellij.openapi.vfs.VirtualFile;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {
    public static String generate (List<Vulnerability> vulnerabilities){
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Report "+ LocalDate.now() +"</title>\n" +
                "</head>\n" +
                "<body>\n";

        int total = vulnerabilities.size();
        html += "Report Static Analysis " + LocalDate.now();
        html = html.concat("<p>Total of  vulnerabilities founded " + total +"</p>\n");

        List<VirtualFile> files =
                vulnerabilities.stream().map(Vulnerability::getVirtualFile).distinct().collect(Collectors.toList());
        html = html.concat("<ul>\n");
        for(VirtualFile file : files){
            List<Vulnerability> vulnerabilityList =
                    vulnerabilities.stream().filter(v->v.getVirtualFile().equals(file)).collect(Collectors.toList());
            html = html.concat("<li>\n" + file.getName());
            html = html.concat("\n<ol>\n");
            for (Vulnerability v: vulnerabilityList){
                html = html.concat("<li>" +v.getName());
                html = html.concat("<ul>\n");
                html = html.concat("<li>Line: " +v.getLine()+ ":</li>\n");
                html = html.concat("<li>Severity: " +v.getSeverity()+ ":</li>\n");
                html = html.concat("<li>Description: " +v.getDescription()+ ":</li>\n");
                html = html.concat("<li>Solution: " +v.getSolution()+ ":</li>\n");
                html = html.concat("<li>Example Code: " +v.getExampleSolutionCode()+ ":</li>\n");
                html = html.concat("</ul>\n");
                html =  html.concat("</li>\n");
            }

            html = html.concat("</ol>\n");
            html = html.concat("</li>\n");
        }
        html = html.concat("</ul>\n");
        html = html.concat("</body>\n</html>");

       return html;
    }
}
