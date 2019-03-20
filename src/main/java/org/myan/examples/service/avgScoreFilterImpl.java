package org.myan.examples.service;

import org.myan.examples.bean.Student;

/**
 * Created by myan on 2019/3/12.
 */
public class avgScoreFilterImpl implements FilterIntf {
    @Override
    public boolean filter(Student student) {
        return student.getAvgScore() > 70;
    }
}
