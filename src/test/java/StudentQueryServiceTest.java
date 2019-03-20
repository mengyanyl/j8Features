import org.junit.Before;
import org.junit.Test;
import org.myan.examples.Utils;
import org.myan.examples.bean.Student;
import org.myan.examples.service.FilterIntf;
import org.myan.examples.service.StudentQueryService;
import org.myan.examples.service.cnNameFilterImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
                .filter(student -> student.getAge()>12 && student.getAge()<14).collect(Collectors.toList());
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

}
