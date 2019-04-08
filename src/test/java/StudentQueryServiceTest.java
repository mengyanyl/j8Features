import org.junit.Before;
import org.junit.Test;
import org.myan.examples.Utils;
import org.myan.examples.bean.Student;
import org.myan.examples.service.FilterIntf;
import org.myan.examples.service.StudentQueryService;
import org.myan.examples.service.cnNameFilterImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

/**
 * Created by myan on 2019/3/12.
 */
public class StudentQueryServiceTest {

    private List<Student> studentList = new ArrayList();

    private StudentQueryService studentQueryService;

    @Before
    public void init(){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for(int i=0; i<10; ++i){
            Student s = new Student();
            if ((i % 2) == 0) {
                s.setCnName("王" + i);
            }else{
                s.setCnName("张" + i);
            }
            s.setAge(i);
            s.setClassName("班级-" + i);
            //平均分60-90间
            s.setAvgScore(new BigDecimal(50 + Math.random() * ((90 - 60) + 1)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            studentList.add(s);
        }
        studentQueryService = new StudentQueryService(studentList);
    }

    @Test
    public void testFilterByCNName(){
        List<Student> list = studentQueryService.getStudentByCNName("王2");
        System.out.println(list.get(0).getCnName() + "; " + list.get(0).getAvgScore());
    }

    @Test
    public void testFilterByAvgScore(){
        List<Student> list = studentQueryService.getStudentByAvgScore(60d);
        System.out.println(list.size());
    }

    @Test
    public void testFilterByCondition(){
        FilterIntf filter = new cnNameFilterImpl();
        List<Student> list = studentQueryService.getStudentByCondition(filter);
        System.out.println(list.size());
    }

    @Test
    public void testFilterByPassingCode(){
        List<Student> list = studentQueryService.getStudentByCondition(Student::isNotPassed);
        System.out.println(list.get(0).getAvgScore());
    }

    @Test
    public void testFilterByLamda(){
        List<Student> list = studentQueryService.getStudentByCondition((student) -> student.getAvgScore() < 60);
        System.out.println(list.size() + "; " + list.get(0).getAvgScore());
    }

    @Test
    public void testFilterAnonymous(){
        List<Student> list = studentQueryService.getStudentByCondition(new FilterIntf() {
            @Override
            public boolean filter(Student student) {
                return student.getAge()>12 && student.getAge()<14;
            }
        });
        System.out.println(list.size());
    }

    @Test
    public void testFilterByStreaming(){
        studentQueryService.getStudentList().stream()
                .filter(student -> student.getAge()>12 && student.getAge()<14).collect(toList());
    }

    @Test
    public void testThreadByLambda(){
        new Thread(() -> System.out.println("hello thread")).start();

        Runnable r = () -> System.out.println("hello thread2");
        new Thread(r).start();
    }

    @Test
    public void testExecTime() throws InterruptedException {
        long st = System.currentTimeMillis();
        for(int i=0; i<10; ++i){
            i += 1;
            Thread.sleep(500);
        }
        System.out.println(System.currentTimeMillis() - st);
    }

    @Test
    public void testTraceTime(){
        Utils.traceTime(() -> {
            for (int i=0; i<10; ++i){
                i = i + 1;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }, (cast -> System.out.println("exec time: " + cast/1000.00 + "s")));
    }

    public void testSorByAvgScore(){
        studentQueryService.getStudentList().sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Double.compare(o1.getAvgScore(), o2.getAvgScore());
            }
        });

        studentQueryService.getStudentList().sort((o1, o2) -> {
            return Double.compare(o1.getAvgScore(), o2.getAvgScore());
        });

        Comparator<Student> comparator = comparingDouble((Student s) -> s.getAvgScore());
        studentQueryService.getStudentList().sort(comparingDouble(s -> s.getAvgScore()));

        studentQueryService.getStudentList().sort(comparingDouble(Student::getAvgScore));
    }

    public void testLambdaInPractice(){
        //得到平局分数大于60的学生，按照名字排序
        List<Student> list = studentQueryService.getStudentList().stream().filter(s -> s.getAvgScore() > 60)
                .sorted(comparing(Student::getCnName))
                .collect(toList());

        //返回所有学生名字符串，名字排序后，用“，”分割
        String str = studentQueryService.getStudentList().stream().map(s -> s.getCnName())
                .sorted()
                .distinct()
                .reduce("", (a, b) -> a + b + ", ");

        //是否存在年龄小于18岁的学生
        boolean isExist = studentQueryService.getStudentList().stream().anyMatch(s -> s.getAge() < 18);

        //打印年龄小于18岁的学生姓名
        studentQueryService.getStudentList().stream().filter(s -> s.getAge() < 18)
                .map(Student::getCnName)
                .forEach(System.out::println);

        //得到最高的平均分
        Optional<Double> highestAvgScore = studentQueryService.getStudentList().stream()
                .map(Student::getAvgScore)
                .reduce(Double::max);
        //Optional<T>可以有个默认值，在没有最大值的情况下
        highestAvgScore.orElse(1d);

        //得到平均分最小的学生记录
        studentQueryService.getStudentList().stream()
                .reduce((a, b) -> a.getAvgScore() < b.getAvgScore() ? a : b);
        //得到平均分最小的学生记录 更好的操作
        studentQueryService.getStudentList().stream()
                .min(comparing(Student::getAvgScore));
    }
}
