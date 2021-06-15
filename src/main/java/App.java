import java.sql.*;

public class App {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1、装载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2.1、3个常量字符串
        final String URL = "jdbc:mysql://localhost:3306/schooldb?useSSL=true&useUnicode=true&characterEncoding=utf8";
        final String USER = "root";
        final String PASS = "xyz123456";

        //2.2、 创建连接
        Connection con = DriverManager.getConnection(URL,USER,PASS);

        //3、与mysql交流

        //插入
        final String SQL = "insert into employee(empName,empPos)values(?,?),(?,?),(?,?)";
        PreparedStatement pst = con.prepareStatement(SQL);
        pst.setObject(1,"芮文文");
        pst.setObject(2,"班主任");
        pst.setObject(3,"郝丽娟");
        pst.setObject(4,"就业专员");
        pst.setObject(5,"陈海勇");
        pst.setObject(6,"教员");

        //删除
       /* final String SQL = "delete from employee where empId in(?,?)";
        PreparedStatement pst = con.prepareStatement(SQL);
        pst.setObject(1,2);
        pst.setObject(2,3);*/

        //修改
        /*final String SQL = "update employee set empPos =? where empId =?";
        PreparedStatement pst = con.prepareStatement(SQL);
        pst.setObject(1,"就业专员");
        pst.setObject(2,4);*/

        // 增删改
        // 向mysql服务执行SQL命令
        int rowsAffected = pst.executeUpdate();

        // 根据rowsAffected输出执行结果
        System.out.println(rowsAffected);

        // 查
        /*final String SQL = "select empId ,empName ,empPos  from employee";
        PreparedStatement pst = con.prepareStatement(SQL);*/
        /**
         * 结果集ResultSet
         *  MetaData 元数据(实体映射所依：反射)
         *  Data 结果数据
         */
        /*ResultSet set = pst.executeQuery();
        ResultSetMetaData md = set.getMetaData();
        final int CC = md.getColumnCount();
*/
        /*for (int i = 1; i <= CC; i++) {
            System.out.print(md.getColumnName(i)+"\t");
            System.out.print(md.getColumnLabel(i)+"\t");
            System.out.print(md.getColumnType(i)+"\t");
            System.out.println(md.getColumnTypeName(i));
        }*/

        //结果集中数据信息
        /*while (set.next()){
            System.out.print(set.getObject(1).toString()+"\t");
            System.out.print(set.getObject(2).toString()+"\t");
            System.out.println(set.getObject(3).toString());
        }*/

        //6、释放资源
        /*set.close();*/
        pst.close();
        con.close();


    }
}
