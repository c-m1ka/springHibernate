<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>title crud spring hibernate freemarker</title>
    </head>
    <body>
    <h2>Employee table</h2>
        <table border="1">
           <tr>
           <th>id</th>
           <th>name</th>
           <th>department</th>
           <th>delete</th>
           </tr>
            <#list employees as employee>
                      <tr>
                      <td> ${employee.id}</td>
                      <td> ${employee.name}</td>
                      <td> ${employee.department.name}</td>
                      <td>
                          <a href="/employee/${employee.id}/delete">X</a>
                      </dt>
                      </tr>
                    <#else>
                       <tr>Part executed when there are 0 record<td>
                   </#list>
         </table>
         <form action="/addUpdateEmployee" method="post">
           <p><input type="text" name="id" placeholder="enter id">
           <input type="text" name="name" placeholder="enter name">
           <input type="text" name="department" placeholder="enter department">
           <p><input type="submit" value="Add/update employee"></p>
          </form>

         <br></br>
        <h2>Department table</h2>

       <table border="1">
            <tr>
                 <th>id</th>
                 <th>name</th>
                 <th>delete</th>
                 </tr>
               <#list departments as department>
                  <tr>
                  <td> ${department.id}</td>
                  <td> ${department.name}</td>
                  <td> <a href="/department/${department.id}/delete">X</a></dt>
                  </tr>
                    <#else>
                     <tr>Part executed when there are 0 record<td>
                </#list>
                </table>
           <form action="/addUpdateDepartment" method="post">
                      <p><input type="text" name="id" placeholder="enter id">
                      <input type="text" name="name" placeholder="enter name of departments">
                      <p><input type="submit" value="Add/update department"></p>
                     </form>
    </body>
</html>