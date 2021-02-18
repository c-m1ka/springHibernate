<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>title crud spring hibernate freemarker index</title>
    </head>
    <body>
    <h2>Employee table</h2>
        <table border="1">
           <tr>
           <th>id</th>
           <th>name</th>
           <th>department</th>
           <th>delete</th>
           <th>edit</th>
           </tr>
            <#list employees as employee>
                      <tr>
                      <td> ${employee.id}</td>
                      <td> ${employee.name}</td>
                      <td> ${employee.department.name}</td>
                      <td><a href="/employee/${employee.id}/delete">X</a></td>
                      <td><a href= "/employee/${employee.id}/edit">edit</a></td>
                      </tr>
                    <#else>
                       Part executed when there are 0 record
                   </#list>
         </table>
         <form action="/addEmployee" method="post">
           <input type="text" name="name" placeholder="enter name employee">
           <select name="department">
            <#list departments as department>
             <option value="${department.id}">${department.name}</option>
             <#else>
             <option value="0">department is empty</option>
             </#list>
           </select>
           <p><input type="submit" value="Add employee"></p>
          </form>

         <br>
        <h2>Department table</h2>

       <table border="1">
            <tr>
                 <th>id</th>
                 <th>name</th>
                 <th>delete</th>
                 <th>edit</th>
            </tr>
               <#list departments as department>
                  <tr>
                  <td> ${department.id}</td>
                  <td> ${department.name}</td>
                  <td> <a href="/department/${department.id}/delete">X</a></td>
                  <td><a href="/department/${department.id}/edit">edit</a></td>
                  </tr>
                    <#else>
                     Part executed when there are 0 record
                </#list>
                </table>
           <form action="/addDepartment" method="post">
             <input type="text" name="name" placeholder="enter name of departments">
             <p><input type="submit" value="Add department"></p>
           </form>
        <br>
        <a href="/statistics">Check statistics</a>

    </body>
</html>