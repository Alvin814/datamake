package jdbc;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import make.DataMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static class Employee {
        private Integer empId;
        private String empName;
        private String empPos;

        public Integer getEmpId() {
            return empId;
        }

        public void setEmpId(Integer empId) {
            this.empId = empId;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getEmpPos() {
            return empPos;
        }

        public void setEmpPos(String empPos) {
            this.empPos = empPos;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "empId=" + empId +
                    ", empName='" + empName + '\'' +
                    ", empPos='" + empPos + '\'' +
                    '}';
        }
    }

    public static class StuScore {
        private int scoreId;
        private int fkStuId;
        private int fkTestId;
        private int score;

        public int getScoreId() {
            return scoreId;
        }

        public int getFkStuId() {
            return fkStuId;
        }

        public int getFkTestId() {
            return fkTestId;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return "StuScore{" +
                    "scoreId=" + scoreId +
                    ", fkStuId=" + fkStuId +
                    ", fkTestId=" + fkTestId +
                    ", score=" + score +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        /*final  String SQL = "insert into employee(empName,empPos)values(?,?)";
        Object[] objs ={"李佳","教员"};
        DaoMessage<Integer> dm = BaseDao.exeUpdate(SQL, objs);
        System.out.println(dm.getStatus());
        System.out.println(dm.getData());*/

        /*final String SQL = "select * from employee";

        DaoMessage<List<Employee>> msg = BaseDao.exeQuery(Employee.class, SQL);
        System.out.println(msg.getStatus());
        List<Employee> list = msg.getData();
        for (Employee e : list) {
            System.out.println(e);
        }*/
        DataMaker dm = new DataMaker();
        //dm.mkScore();
        //dm.mkEmployee();\
        // dm.mkProduct();
        //dm.mkClass();
        //dm.mkStudent();
        //dm.mk_relation_class_emp();
        // dm.mk_subject();
        //dm.mkStudent();
        //dm.mkJob();
        //dm.mkScore();
        //dm.mkTest();
        /*DaoMessage<List<Integer>> msg = BaseDao.queryOneColumn(Integer.class, "select fkProductId from classInfo");
        for (Integer i : msg.getData()) {
            System.out.println(i);
        }*/

        String sql = "{call pro_score_page(?,?)}";
        Object[] params = {2, 5};
        Class[] cs = {Integer.class, StuScore.class};
        Map<Integer, Object> data = BaseDao.exePro(sql, cs, params).getData();
        for (Map.Entry<Integer, Object> e : data.entrySet()) {
            System.out.println(e.getKey());
            Object value = e.getValue();
            if (value instanceof List) {
                List list = (List) value;
                for (Object o : list) {
                    System.out.println(o);
                }
            } else {
                System.out.println(value);
            }
        }

    }
}
