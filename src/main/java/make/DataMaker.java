package make;

import com.sun.jndi.ldap.Connection;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import jdbc.BaseDao;
import jdbc.ConnectionFactory;

import javax.management.ObjectName;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataMaker extends ConnectionFactory {
    Random rand = new Random();
    Calendar cale = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void mkEmployee() {
        String names = "郝晓博黎泰清郑欣德彭英锐龙阳波陈华翰赖修筠江睿思姚德华蔡景铄江雅健余天纵梁星辰";
        List<String> list = new ArrayList<>(names.length() / 3);
        for (int i = 0; i < names.length(); i += 3) {
            list.add(names.substring(i, i + 3));
        }
        String[] pos = {"教员", "班主任", "就业专员"};
        List<Object> args = new ArrayList<>(list.size() * 2);

        for (String name : list) {
            args.add(name);
            args.add(pos[rand.nextInt(pos.length)]);
        }
        final String SQL = "insert into employee(empName,empPos)values(?,?)";
        BaseDao.addBatch(SQL, 2, 30, args.toArray());
    }

    public void mkProduct() {
        final String SQL = "insert into productinfo(product)values(?)";
        BaseDao.addBatch(SQL, 1, 2,
                new String[]{"大数据", "云计算"}).getData();
    }

    public void mkClass() {
        Object[] args = {"KB11", 1, "KB12", 1, "KB13", 1, "KY08", 2, "KY09", 2, "KY10", 2};
        final String SQL = "insert into classinfo(className,fkProductId)values(?,?)";
        BaseDao.addBatch(SQL, 2, args);
        ;
    }

    public void mk_relation_class_emp() {
        Object[] args = {1, 1, 2, 1, 3, 1, 4, 2, 5, 2, 6, 2, 7, 3, 8, 3, 9, 3, 10, 4, 11, 4, 12, 4, 13, 5, 14, 5, 15, 5, 16, 6, 17, 6, 18, 6};
        final String SQL = "insert into relation_class_emp(fkEmpId,fkClassId)values(?,?)";
        System.out.println(BaseDao.addBatch(SQL, 2, 18, args).getData());

    }

    public void mk_subject() {
        Object[] args = {"云计算部署", 2, "云计算与网络安全", 2, "Web开发实战", 2, "Python开发向导", 2, "大型网站架构与自动化运维", 2, "Docker容器与虚拟化技术", 2,
                "数据库应用Web服务器群集", 2, "Linux网络服务与shell脚本攻略", 2, "Linux系统管理", 2, "网络原理与应用", 2};
        Object[] args1 = {"Java 面向对象程序开发及实战", 1, "Java 高级特性编程及实战", 1, "MySQL 数据库应用技术及实战", 1, "Java Web 应用技术及实战", 1,
                "SSM 轻量级框架应用实战", 1, "SSH 框架企业级应用实战", 1, "基于 Hadoop 与 Spark 的大数据开发实战", 1, "微服务实战（Dubbox+Spring Boot+Docker）", 1, "Spring Cloud 微服务分布式架构开发实战", 1};
        final String SQL = "insert into subjectinfo(subject,fkProductId)values(?,?)";
        System.out.println(BaseDao.addBatch(SQL, 2, 9, args1).getData());

    }

    public void mkStudent() {
        //String names = "江睿思姚德华蔡景铄江雅健余天纵梁星辰萧冠宇金祺福夏华藏田高扬魏鸿博金鸿德周光临龙浩壤周奇迈于项禹林文栋孙天禄谢元青康绍元夏浩瀚钟高阳孔文星曾振海顾修齐康伟才尹宏恺郑泰河武华荣夏宇荫吕俊风王阳成石浩然熊嘉颖周经武常伟毅曹博达胡奇文曹德水石高杰蔡天罡卢俊誉许飞雨崔和蔼魏晟睿杨鹏程毛英杰黄波光康志行黎志泽段茂德周子琪余兴庆曾炫明秦正祥孙鸿云陆乐意武奇迈马承泽王乐游范鹏鲸易星光周高原夏星洲邹良策戴子明赵宏富常乐邦赖雅昶沈天成谭嘉瑞夏彭魄曾弘懿何成和周德佑林康复阎庆生廖绍元阎建树易文德邵飞鸾林奇伟蒋咏歌郑鹏鲲沈新翰常彭魄石开朗康修杰邵建木孔瑾瑜阎茂勋吕正谊毛茂典何国安金阳序赵康泰徐俊艾高修永廖飞鸣曹鸿达田弘深傅光济林永寿宋嘉誉夏永福徐飞星白蕴藉万星宇郭高超漕文斌阎元凯孙景龙石博涛曾锐泽龚俊材杜嘉誉萧乐天段茂实范承平方和泽秦建华白德本易雅畅段安易林烨熠杜英杰于俊艾邱星纬易高芬唐建本王天逸吴涵忍文元良黄安平陆烨伟乔展鹏刘景铄万涵润钟泽宇潘俊茂王志诚郑嘉庆熊黎昕";
        String names = "朱晴芳阎蓓莉乔芳蕤邹书双钱平露钱泓茹许佳文曹凡霜钱绿蕊史任真刘清秋廖绢子傅青寒宋笑珊秦晓筠任平绿陆生文贾平婉武红丽黄从菡袁三姗常三诗易永茹吕晓蓝唐珊珊陈婉淑马迷伤贺水晶董玲丽林新洁刘端静汪昕雨康燕平范芦雪龙丽思龙浩丽孟菊霞邱梓欣江端懿范红梅孟静秀刘愉婉漕燕萍侯仲舒秦忻愉马珠星武惠玲曾一南李歆美钟娴雅罗丽莉张清一侯映阳毛忠燕夏今雨夏映蓉胡凡白苏含玉康雅宁胡清嘉史怿悦彭忆丹吕语芙曹珠轩尹思菱卢婉娜吕晓燕段心宜王希恩袁访云金从南蒋薇歌梁谷玉冯凝琴潘玮琪谢英华许素怀彭爱萍胡怀蕊万怡婷王依珊陆未央邹晴丽梁映安朱青筠徐贞怡薛松雨段迎天阎紊文郭欣然苏半梦谭玉华常雄英丁婉容黎恩霈汤香馨熊悠素吕冷雁赵尔安廖梦晨";
        final int N = names.length() / 3;
        Object[] args = new Object[N * 4];
        String[] genders = {"男", "女"};
        for (int i = 0, j = 0; j < N; j++) {
            args[i++] = names.substring(j * 3, (j + 1) * 3);
            args[i++] = 22 + rand.nextInt(14);
            args[i++] = genders[rand.nextInt(2)];
            args[i++] = 1 + rand.nextInt(6);
        }
        final String SQL = "insert into studentinfo(stuName,stuAge,stuGender,fkClassId)values(?,?,?,?)";
        BaseDao.addBatch(SQL, 4, 30, args);
    }

    public void mkJob() {
        final int stuCount = 180;
        Object[] args = new Object[stuCount * 4];
        String[] companyName = {"华为技术有限公司"
                , "深圳市腾讯计算机系统有限公司"
                , "阿里巴巴(中国)有限公司"
                , "北京百度网讯科技有限公司"
                , "中国通信服务股份有限公司"
                , "海尔集团"
                , "京东集团"
                , "中兴通讯股份有限公司"
                , "完美世界股份有限公司"
                , "卡斯柯信号有限公司"
                , "中软国际有限公司"
                , "网易(杭州)网络有限公司"
                , "中国信息通信科技集团有限公司"
                , "南瑞集团有限公司"
                , "北京久其软件股份有限公司"
                , "亚信科技(中国)有限公司"
                , "信雅达系统工程股份有限公司"
                , "用友网络科技股份有限公司"
                , "中科软科技股份有限公司"
                , "佳都集团有限公司"
                , "华云数据控股集团有限公司"
        };
        for (int i = 0, stuNum = 1; i < stuCount * 4; ) {
            args[i++] = stuNum++;
            args[i++] = companyName[rand.nextInt(21)];
            args[i++] = rand.nextInt(20000) + 14000;
            args[i++] = "2021-" + (rand.nextInt(2) + 5) + "-" + (rand.nextInt(30) + 1);
        }
        final String SQL = "insert into jobinfo(fkStuId,company,salary,jobDate)values(?,?,?,?)";
        System.out.println(BaseDao.addBatch(SQL, 4, stuCount, args).getData());
    }

    /*public void mkScore() {
        final String SQL = "insert into scoreinfo (fkStuId,fkTestId,score) values(?,?,?)";
        Integer[] scores = new Integer[5130];
        for (int i = 1, t = 0; i <= 180; i++) {
            if (i <= 30) {
                for (int j = 0; j < 9; j++) {
                    scores[t++] = i;
                    scores[t++] = 1 + j;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            } else if (i <= 60) {
                for (int j = 0; j < 9; j++) {
                    scores[t++] = i;
                    scores[t++] = 10 + j;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            } else if (i <= 90) {
                for (int j = 0; j < 9; j++) {
                    scores[t++] = i;
                    scores[t++] = 19 + j;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            } else if (i <= 120) {
                for (int j = 0; j < 10; j++) {
                    scores[t++] = i + 1;
                    scores[t++] = 28 + j;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            } else if (i <= 150) {
                for (int j = 0; j < 10; j++) {
                    scores[t++] = i;
                    scores[t++] = j + 38;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            } else {
                for (int j = 0; j < 10; j++) {
                    scores[t++] = i;
                    scores[t++] = j + 48;
                    scores[t++] = rand.nextInt(70) + 31;
                }
            }

        }
        System.out.println(BaseDao.addBatch(SQL, 3, 100, scores).getData());
    }*/

    public void MkClassEmpRelation() {
        //按岗位提取员工empId
        String[] poses = {"教员", "班主任", "就业专员"};
        Map<String, List<Integer>> mapEmp = new HashMap<>(3);
        String SQL = "select empId from employee where empPos=?";
        for (String pos : poses) {
            mapEmp.put(pos, BaseDao.queryOneColumn(Integer.class, SQL, pos).getData());
        }
        //按是否毕业提取班级classId信息
        Boolean[] bools = {true, false};
        SQL = "select classId from classinfo where endDateEQUAL '9999-9-9'";
        Map<Boolean, List<Integer>> mapClass = new HashMap<>(2);
        for (Boolean bool : bools) {
            mapClass.put(bool, BaseDao.queryOneColumn(Integer.class
                    , SQL.replace("EQUAL", bool ? "!=" : "=")).getData());
        }
        //遍历所有班级，为每个班级添加员工信息关联
        SQL = "insert into relation_class_emp(fkEmpId,fkClassId)values(?,?)";
        //伪造批量参数值信息
        List<Object> args = new ArrayList<>(mapClass.get(true).size() * 3 +
                mapClass.get(false).size() * 2);
        List<Integer> teacher = mapEmp.get("教员");
        List<Integer> tutor = mapEmp.get("班主任");
        List<Integer> job = mapEmp.get("就业专员");
        for (Map.Entry<Boolean, List<Integer>> e : mapClass.entrySet()) {
            Boolean graduated = e.getKey();
            for (Integer classId : e.getValue()) {
                args.add(teacher.get(rand.nextInt(teacher.size())));
                args.add(classId);
                args.add(tutor.get(rand.nextInt(tutor.size())));
                args.add(classId);
                if (graduated) args.add(job.get(rand.nextInt(job.size())));
            }
        }
        System.out.println(BaseDao.addBatch(SQL, 10,
                args.toArray()).getData());
    }

    private List<String> date(int[] vs, int dayDiff) {
        List<String> dates = new ArrayList<>(vs[1]);
        cale.set(vs[2], vs[3], vs[4]);
        for (int i = 0; i < vs[1]; i++) {
            cale.add(Calendar.DATE, dayDiff);
            dates.add(sdf.format(cale.getTime()));
        }
        return dates;
    }

    public void mkTest() {
        Map<String, int[]> mapClass = new HashMap<>();
        mapClass.put("KB11", new int[]{1, 9, 2020, 8, 13});
        mapClass.put("KB12", new int[]{2, 3, 2021, 3, 18});
        mapClass.put("KB13", new int[]{3, 0, 2021, 4, 28});
        mapClass.put("KY08", new int[]{4, 10, 2020, 5, 8});
        mapClass.put("KY09", new int[]{5, 10, 2020, 6, 2});
        mapClass.put("KY10", new int[]{6, 5, 2021, 3, 3});

        Map<String, Integer> mapPro = new HashMap<>();
        mapPro.put("KB", 1);
        mapPro.put("KY", 2);
        Map<String, List<Integer>> mapSub = new HashMap<>();
        String SQL = "select subjId from subjectinfo where fkProductId=? order by subjId";
        for (Map.Entry<String, Integer> e : mapPro.entrySet()) {
            mapSub.put(e.getKey(), BaseDao.queryOneColumn(
                    Integer.class, SQL, e.getValue()).getData());
        }
        Map<String, Integer> mapDate = new HashMap<>(2);
        mapDate.put("KB", 210 / 9);
        mapDate.put("KY", 180 / 10);
        List<Object> args = new ArrayList<>();
        for (Map.Entry<String, int[]> e : mapClass.entrySet()) {
            String name = e.getKey().substring(0, 2);
            int[] vs = e.getValue();
            int fkClassId = vs[0], subNum = vs[1];
            List<String> dates = date(vs, mapDate.get(name));
            List<Integer> subs = mapSub.get(name).subList(0, subNum);
            for (int i = 0; i < subs.size(); i++) {
                args.add(fkClassId);
                args.add(subs.get(i));
                args.add(dates.get(i));
            }
        }
        SQL = "insert into testinfo(fkClassId,fkSubjId,testDate)values(?,?,?)";
        System.out.println(BaseDao.addBatch(SQL, 19,
                args.toArray()).getData());
    }

    public class Testinfo {
        private Integer testId;
        private Integer fkClassId;

        public Integer getTestId() {
            return testId;
        }

        public void setTestId(Integer testId) {
            this.testId = testId;
        }

        public Integer getFkClassId() {
            return fkClassId;
        }

        public void setFkClassId(Integer fkClassId) {
            this.fkClassId = fkClassId;
        }
    }

    public void mkScore() {
        StringBuilder sql = new StringBuilder();
        List<Integer> stuIds, testIds;
        List<Object> args = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            sql.delete(0, sql.length());
            sql.append("select stuId from studentinfo where fkClassId=?");
            stuIds = BaseDao.queryOneColumn(
                    Integer.class, sql.toString(), i).getData();
            sql.delete(0, sql.length());
            sql.append("select testId from testinfo where fkClassId=?");
            testIds = BaseDao.queryOneColumn(
                    Integer.class, sql.toString(), i).getData();

            if (null == testIds) continue;
            ;
            final int U = 61, R = 58, B = 40;
            for (Integer testId : testIds) {
                for (Integer stuId : stuIds) {
                    args.add(stuId);
                    args.add(testId);
                    int score = rand.nextInt(U);
                    args.add(score >= R ? null : B + score);
                }
            }
        }
        sql.delete(0, sql.length());
        StringBuilder append = sql.append("insert into scoreinfo(fkStuId,fkTestId,score) values(?,?,?)");
        System.out.println(BaseDao.addBatch(sql.toString(), 200, args.toArray()).getData());
    }

    private List<File> sqlFiles(File dir) {
        List<File> files = new ArrayList<>();
        if (dir.isFile()) {
            if (dir.getName().endsWith(".sql"))
                files.add(dir);
        } else {
            File[] arr = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File path) {
                    return path.getName().endsWith(".sql");
                }
            });
            files.addAll(Arrays.asList(arr));
        }
        return files;
    }

    private String REGEX_INSERT = "INSERT INTO .*? VALUES (\\(.*?\\));";

    private final Pattern P_INSERT = Pattern.compile(REGEX_INSERT);

    public void submit(File file, ExecutorService pool) {
        pool.submit(() -> {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                StringBuilder sql = new StringBuilder();
                String line = null;
                while (null != (line = br.readLine())) {
                    Matcher matcher = P_INSERT.matcher(line);
                    if (!matcher.matches()) continue;
                    if (sql.length() == 0) {
                        sql.append(line);
                        sql.deleteCharAt(sql.length() - 1);
                    } else {
                        sql.append(",");
                        sql.append(matcher.group(1));
                    }
                }
                if (sql.length() > 0) {
                    System.out.println(BaseDao.exeUpdate(sql.toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(br);
            }
        });
    }

    public void exeSQlFormatFile(String path) throws Exception {
        File dir = new File(path);
        if (!dir.exists())
            throw new Exception(path + "不存在异常");
        List<File> files = sqlFiles(dir);
        if (!files.isEmpty()) {
            ExecutorService es = Executors.newFixedThreadPool(
                    files.size() > 10 ? 10 : files.size());
            for (File file : files) {
                submit(file, es);
            }
            es.shutdown();
        }
    }

}
