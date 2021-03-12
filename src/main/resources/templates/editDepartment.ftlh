<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8" />
    <title>title crud spring hibernate freemarker edit department</title>
</head>
<body>
<h2>Employee table</h2>
<table border="1">
    <tr>
        <th>id</th>
        <th>name</th>
        <th>save</th>
    </tr>
    <form action="/editDepartment" method="post">
        <tr>
            <td><input type="text" name="departmentId" value="${departmentId}" readonly></td>
            <td>
                <input type="text" name="name" value="${departmentName}">
            </td>
            <td>
                <input type="submit" value="save">
            </td>
        </tr>
    </form>
</table>
</body>
</html>