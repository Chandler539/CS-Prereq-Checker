package prereqchecker;

import java.util.*;

public class Course {
    private String id;
    
    private boolean require;
    private boolean flags;
    private ArrayList<String> prereqs = new ArrayList<String>();
    private int level;

    public Course(String id) {
        this.id = id;
        this.require = false;
        this.flags = false;
        this.level = 0;
    }

    public void add(String id) {
        prereqs.add(id);
    }

    public String getID(){
        return id;
    }

    public void getOutput() {
        StdOut.print(id + " ");
        for (int i = 0; i < prereqs.size(); ++i) {
            StdOut.print(prereqs.get(i) + " ");
        }
        StdOut.println();
    }

    public ArrayList<String> getConnections(){
            return prereqs;
    }

    public void setflags(boolean b){
        flags = b;
    }

    public boolean getflags(){
        return flags;
    }

    public void setrequire(boolean b){
        this.require = b;
    }

    public boolean getrequire(){
        return require;
    }

    public void setLevel(int n){
        this.level = n;
    }

    public int getLevel(){
        return level;
    }

}
