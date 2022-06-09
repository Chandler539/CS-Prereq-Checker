package prereqchecker;

import java.util.*;

public class Digraph {
    private int E;
    private int V;
    private HashMap<String, Integer> key = new HashMap<String, Integer>();
    private ArrayList<Course> revConnections = new ArrayList<Course>();
    private ArrayList<Course> connections = new ArrayList<Course>();
    private HashMap<String, Integer> revKeys = new HashMap<String, Integer>();
    private ArrayList<Link> allLinks = new ArrayList<Link>();

    // reads in input file and creates digraph
    public Digraph(String inputFile) {
        StdIn.setFile(inputFile);
        this.V = Integer.parseInt(StdIn.readString());

        for (int i = 0; i < V; i++) {
            String word = StdIn.readString();
            key.put(word, i);

            Course course = new Course(word);
            connections.add(course);
        }

        this.E = Integer.parseInt(StdIn.readString());

        for (int i = 0; i < E; i++) {
            String word = StdIn.readString();
            String prereq = StdIn.readString();
            Link l = new Link(word, prereq);
            allLinks.add(l);
            int k = key.get(word);
            connections.get(k).add(prereq);
        }
    }

    public Digraph(ArrayList<String> arr, Digraph adj) {
        for (int i = 0; i < arr.size(); i++) {
            String word = arr.get(i);
            revKeys.put(word, i);

            Course course = new Course(word);
            revConnections.add(course);
        }

        int n = adj.getAllLinks().size();

        for (int i = 0; i < n; ++i) {
            String prereq = adj.getAllLinks().get(i).getPrereq();
            if (revKeys.containsKey(prereq)) {
                String word = adj.getAllLinks().get(i).getWord();
                if (revKeys.containsKey(word)) {
                    int k = revKeys.get(prereq);
                    revConnections.get(k).add(word);
                }
            }
        }
    }

    public ArrayList<Link> getAllLinks() {
        return allLinks;
    }

    public void getOutput(String outputFile) {
        StdOut.setFile(outputFile);

        for (int i = 0; i < V; i++) {
            connections.get(i).getOutput();
        }
    }

    public void getRevOutput(String outputFile) {
        StdOut.setFile(outputFile);

        for (int i = 0; i < revConnections.size(); i++) {
            revConnections.get(i).getOutput();
        }
    }





    // 2 prereq

    public boolean readPrereqFile(String pFile) {
        StdIn.setFile(pFile);
        String c1 = StdIn.readString();
        String c2 = StdIn.readString();

        return !isValidPrereq(c2, c1);
    }

    public boolean isValidPrereq(String course, String prereq) {
        Course c = connections.get(key.get(course));
        ArrayList<String> arr = c.getConnections();
        if (arr.isEmpty())
            return false;
        for (int i = 0; i < arr.size(); ++i) {
            String curr = arr.get(i);
            if (curr.equals(prereq))
                return true;
            if (isValidPrereq(arr.get(i), prereq))
                return true;
        }
        return false;
    }

    // for eligible #3
    public void eligibleOut(String eFile, String oFile) {
        ArrayList<String> taken = setEligibleArray(eFile);
        flagsMultiple(taken);
        StdOut.setFile(oFile);

        for (Course c : connections) {
            if (checkEligibility(c))
                StdOut.println(c.getID());
        }
    }

    public ArrayList<String> setEligibleArray(String eFile) {
        ArrayList<String> arr = new ArrayList<String>();
        StdIn.setFile(eFile);

        int n = StdIn.readInt();
        for (int i = 0; i < n; i++) {
            int k = key.get(StdIn.readString());
            arr.add(connections.get(k).getID());
        }
        return arr;
    }

    public void flagsMultiple(ArrayList<String> arr) {
        for (String s : arr) {
            flags(s);
        }
    }

    public void flags(String course) {
        Course c = connections.get(key.get(course));
        c.setflags(true);
        ArrayList<String> arr = c.getConnections();
        if (arr.isEmpty())
            return;
        for (int i = 0; i < arr.size(); ++i) {
            flags(arr.get(i));
        }
        return;
    }

    public boolean checkEligibility(Course c) {
        ArrayList<String> arr = c.getConnections();
        if (c.getflags() == true)
            return false;
        for (String s : arr) {
            if (connections.get(key.get(s)).getflags() == false)
                return false;
        }
        return true;
    }
    // for need to take class

    public void needToTakeOutput(ArrayList<String> taken, String target, String outputFile) {
        flagsMultiple(taken);
        flagsrequires(target);
        connections.get(key.get(target)).setrequire(false);

        StdOut.setFile(outputFile);

        for (Course c : connections) {
            if (c.getrequire()) {
                StdOut.println(c.getID());
            }
        }
    }

    public void flagsrequires(String target) {
        Course c = connections.get(key.get(target));
        if (c.getflags() == false)
            c.setrequire(true);
        ArrayList<String> arr = c.getConnections();
        if (arr.isEmpty())
            return;
        for (int i = 0; i < arr.size(); ++i) {
            flagsrequires(arr.get(i));
        }
        return;
    }

    public ArrayList<Course> getConnectionsArray() {
        return connections;
    }

    // for SchedulePlan
    public ArrayList<String> needToTakeOutput(ArrayList<String> taken, String target) {
        ArrayList<String> arr = new ArrayList<String>();
        flagsMultiple(taken);
        flagsrequires(target);

        for (Course c : connections) {
            if (c.getrequire()) {
                arr.add(c.getID());
            }
        }
        return arr;
    }

    public void setSemester(int index, int level) {
        if (revConnections.get(index).getLevel() < level)
            revConnections.get(index).setLevel(level);
        else
            return;
        if (revConnections.get(index).getConnections().isEmpty())
            return;
        for (String s : revConnections.get(index).getConnections()) {
            int k = revKeys.get(s);
            setSemester(k, level + 1);
        }
        return;
    }

    public void setSemester2(String outputFile){
        for (int i=0; i<revConnections.size(); i++){
            setSemester(i, 1);
        }

        int n = maxLevel();

        StdOut.setFile(outputFile);
        if(n>0)
        StdOut.print(n-1);
        else
        StdOut.print(n);
        StdOut.println();

        for (int i=1; i<n; ++i){
            for (int j=0; j<revConnections.size(); ++j){
                if (revConnections.get(j).getLevel()==i)
                StdOut.print(revConnections.get(j).getID() + " ");
            }
            StdOut.println();
        } 
    }

    public int maxLevel(){
        int max = 0;
        for (int i=0; i<revConnections.size(); ++i){
            if (revConnections.get(i).getLevel()>max)
            max=revConnections.get(i).getLevel();
        }
        return max;
    }
}
