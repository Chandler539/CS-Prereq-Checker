
package prereqchecker;

public class Link {
   
    private String prereqs;
    private String words;

    public Link (String w, String p){
        this.words = w;
        this.prereqs = p;
    }

    public String getWord(){
        return words;
    }

    public String getPrereq(){
        return prereqs;
    }
}
