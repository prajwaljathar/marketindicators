<%-- <%@ page import="java.nio.file.Files" %>
<%@ page import="java.nio.file.Paths" %>

<html>
<head>
    <title>Display HTML Content</title>
</head>
<body>

<h1>HTML Content</h1>

<%
    String htmlFilePath = "C:\\Users\\admin\\Downloads\\StockApi\\demo (4)\\demo\\response.html"; // Replace with the actual path
    try {
        String htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath)));
        out.println(htmlContent);
    } catch (Exception e) {
        out.println("Error reading HTML content: " + e.getMessage());
    }
%>

</body>
</html> --%>
<%-- <!DOCTYPE html>
<html>
<head>
    <title>Hello Spring MVC</title>
</head>
<body>
    <h2>${message}</h2>
    <h3>f</h3>
</body>
</html> --%>
${message}

