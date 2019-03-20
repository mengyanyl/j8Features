package org.myan.examples.service;

import org.myan.examples.bean.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myan on 2019/3/12.
 */
public class StudentQueryService {
    private List<Student> studentList = new ArrayList<>();

    public StudentQueryService(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Student> getStudentByCNName(String cnName){
        List<Student> result = new ArrayList<>();
        for (Student student:
             studentList) {
            if (student.getCnName().equals(cnName)){
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getStudentByAvgScore(double avgScore){
        List<Student> result = new ArrayList<>();
        for (Student student:
             studentList) {
            if (student.getAvgScore() > avgScore){
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getStudentByCondition(FilterIntf filterIntf){
        List<Student> result = new ArrayList<>();
        for (Student student:
                studentList) {
            if (filterIntf.filter(student)){
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getStudentList() {
        return studentList;
    }
}
