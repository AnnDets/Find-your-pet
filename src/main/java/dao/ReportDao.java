package dao;

import models.Report;
import models.User;

import java.util.ArrayList;

public abstract class ReportDao  implements DAO{
    @Override
    public abstract boolean create(Object object);

    @Override
    public abstract boolean update(Object object);

    @Override
    public abstract boolean delete(Object object);

    @Override
    public abstract Object read(Object object);

    @Override
    public abstract ArrayList<Object> readAll();

    public abstract boolean addColors(Report report, String[] colors);
    public abstract boolean addPhotos(Report report, String[] photos);
    public abstract boolean addMarks(Report report, String[] marks);
    public abstract Report getById(int id);
    public abstract ArrayList<Report> getReportsByFilters(String color, String specialMark,
                                                          String location, String breed);
}
