package org.project.Frontend;

import org.project.service.InstructorRole;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartFrame extends JFrame {

    private final String title;
    private final String xAxis;
    private final String yAxis;

    public ChartFrame(String title, String xAxis, String yAxis) {
        super(title);
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public void plotCourseAverageQuiz(int courseId, InstructorRole service) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        service.getCdb().loadCourses().stream()
                .filter(c -> c.getCourseId() == courseId)
                .findFirst()
                .ifPresent(course -> {
                    course.getLessons().forEach(lesson -> {
                        double avg = service.getAverageQuizScoreForCourse(courseId);
                        dataset.addValue(avg, "Average", lesson.getTitle());
                    });
                });
        JFreeChart chart = ChartFactory.createBarChart(title, xAxis, yAxis, dataset, PlotOrientation.VERTICAL, false, true, false);
        setContentPane(new ChartPanel(chart));
    }

    public void plotCourseCompletion(int courseId, InstructorRole service) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        double completion = service.getCourseCompletionPercentage(courseId);
        dataset.setValue("Completed", completion);
        dataset.setValue("Not Completed", 100 - completion);
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        setContentPane(new ChartPanel(chart));
    }

    public void plotLessonCompletion(int courseId, InstructorRole service) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> completionMap = service.getLessonCompletionPerStudent(courseId);
        completionMap.forEach((student, count) -> dataset.addValue(count, "Lessons Completed", student));
        JFreeChart chart = ChartFactory.createBarChart(title, xAxis, yAxis, dataset, PlotOrientation.VERTICAL, false, true, false);
        setContentPane(new ChartPanel(chart));
    }
}
