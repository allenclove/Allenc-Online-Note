package cn.alenc.sqlfathercopy.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: allenc
 * @Date: 2023/3/29 20:37
 * @Description: 
 */
public class rfTestMatchArgs1 {
    public static void main(String[] args) {
        String input = "package cn.alenc.sqlfathercopy.controller;\n" +
                "\n" +
                "import org.springframework.web.bind.annotation.GetMapping;\n" +
                "import org.springframework.web.bind.annotation.PathVariable;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "/**\n" +
                " * @Auther: allenc\n" +
                " * @Date: 2023/3/19 22:48\n" +
                " * @Description:\n" +
                " */\n" +
                "@RestController\n" +
                "@RequestMapping(\"/test\")\n" +
                "public class TestController {\n" +
                "\n" +
                "    @GetMapping(\"/sayhello/{name}\")\n" +
                "    @abc(message=\"hello\", test=\"hel34lo\")\n" +
                "    public String sayHello(@sdifj String name, int xixi, @test(\"sdfsd\") List<Gooda> gooda, Xisi xisi,Map<String, Gxxx> mapd) {\n" +
                "        return String.format(\"hello,%s!\", name);\n" +
                "    }\n" +
                "\n" +
                "    @abc(message=\"hellokjh\")\n" +
                "    @GetMapping(\"/sayhel4lo/{na4me}\")\n" +
                "    public String sayHello33(String name, @test (\"sdfsd\")  int xi3xi, List<Gooda> googda, Xisi xis6i) {\n" +
                "        return String.format(\"hello,%s!\", name);\n" +
                "    }\n" +
                "}";

        String output = processJavaFile(input);
        System.out.println(output);
    }

    public static String processJavaFile(String input) {
        // Define regex patterns to match @abc annotation and method signature
        Pattern abcPattern = Pattern.compile("@abc\\(message=\"(.*?)\".*?\\)");
        Pattern methodPattern = Pattern.compile("(public|private|protected)?\\s+(static\\s+)?\\w+\\s+(\\w+)\\s*\\((.*?)\\)\\s*\\{");

        // Split input string into lines
        String[] lines = input.split("\\r?\\n");

        // Loop through each line to find @abc annotations and method signatures
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Check if the line contains @abc annotation
            Matcher abcMatcher = abcPattern.matcher(line);
            if (abcMatcher.find()) {
                // Get the message attribute value from the @abc annotation
                String message = abcMatcher.group(1);

                // Find the method signature by searching for the next line that starts with "public", "private", or "protected"
                for (int j = i + 1; j < lines.length; j++) {
                    String nextLine = lines[j];

                    Matcher methodMatcher = methodPattern.matcher(nextLine);
                    if (methodMatcher.find()) {
                        // Get the method name and parameter list
                        String methodName = methodMatcher.group(3);
                        String parameterList = methodMatcher.group(4);

                        // 将所有的注解替换掉
                        parameterList = parameterList.replaceAll("(@.*?\\(.*? )", "");

                        // 将所有泛型替换掉
                        parameterList = parameterList.replaceAll("(<.*?>)", "");

                        // Split the parameter list into individual parameter names
                        String[] parameters = parameterList.split(",");

                        // Loop through each parameter and add "#" and "{}" around it
                        StringBuilder paramBuilder = new StringBuilder();
                        for (String param : parameters) {
                            String paramName = param.trim().split(" ")[1];
                            paramBuilder.append("{#").append(paramName).append("}, ");
                        }

                        // Remove the trailing comma and space from the parameter string
                        String paramString = paramBuilder.toString().trim().replaceAll(",$", "");

                        // Append the parameter string to the message attribute value
                        message += ", " + paramString;

                        // Replace the original message attribute value with the updated one
                        line = line.replaceFirst("(message=\".*?\")", "message=\"" + message + "\"");

                        // Break out of the loop since we've found the method signature
                        break;
                    }
                }
            }

            // Append the modified line to the output string
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
