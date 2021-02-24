<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8" />
    <title>title crud spring hibernate freemarker edit employee</title>
</head>
<body>
<h2>Employee table</h2>
<table border="1">
    <tr>
        <th>id</th>
        <th>name</th>
        <th>department</th>
        <th>save</th>
    </tr>
    <form action="/editEmployee" method="post">
        <tr>
            <td><input type="text" name="employeeId" value="${employeeId}" readonly></td>
            <td>
                <input type="text" name="name" value="${employeeName}">
            </td>
            <td>
                <select name="department">
                    <option disabled>${departmentName}</option>
                    <#list departments as department>
                    <option value="${department.id}">${department.name}</option>
                </#list>
                </select>
            </td>
            <td>
                <input type="submit" value="save">
            </td>
        </tr>
    </form>
</table>
</body>
</html>